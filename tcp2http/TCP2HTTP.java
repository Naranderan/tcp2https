import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//TODO: Need to change it as a thread-pooled server.
//TODO: Need to log in a file instead of System.out.
public class TCP2HTTP {
	
	private final static String destinationURL = "http://" + Constants.RPROXY_HOST + ":" + Constants.HTTP_PORT + Constants.RPROXY_URI;
	
	public static void main(String[] args) throws IOException {
		  
		  ServerSocket welcomeSocket = new ServerSocket(Constants.PROXY_TCP_PORT);
		  while (true) {
			   Socket connectionSocket = welcomeSocket.accept();
			   processIt(connectionSocket);
		  }
	}
	
	private static void processIt(final Socket soc) {
		    class OneShotTask implements Runnable {
		    	Socket socket;
		        OneShotTask(Socket s) { this.socket = soc;}
		        public void run() {
		        	try {
		        		System.out.println("*********Starting the convertion(TCP -> HTTP)***********");
		        		InputStream sin = new BufferedInputStream(socket.getInputStream());
		        		OutputStream sos = new BufferedOutputStream(socket.getOutputStream());
		        		
		        		HttpURLConnection httpConn = (HttpURLConnection) new URL(destinationURL).openConnection();
		        		httpConn.addRequestProperty(Constants.AUTH_HEADER_NAME, Constants.AUTH_HEADER_VALUE);
		        		httpConn.addRequestProperty(Constants.DFS_IP, socket.getLocalAddress().getHostAddress());
		        		httpConn.addRequestProperty(Constants.DFS_PORT, Integer.toString(socket.getLocalPort()));
		        		httpConn.setDoOutput(true);
		        		
		        		System.out.println("********* Piping socket req to http req *********");
		        		System.out.println("Source IP : " + socket.getRemoteSocketAddress() + " Source Port : " + socket.getPort());
		        		System.out.println("Dest IP : " + socket.getLocalAddress() + " Dest Port : " + socket.getLocalPort());
//		        		httpConn.setDoInput(true);
		        		OutputStream httpOut = httpConn.getOutputStream();
		        		
		        		CommonUtil.pipe(sin, httpOut);
		        		httpOut.close();
		        			
		        		System.out.println("********* Piping http res to soc res *********");
		        		System.out.println("Response code from HTTP server is " + httpConn.getResponseCode());
		        		InputStream httpIn = httpConn.getInputStream();
		        		printDateTime();
		        		CommonUtil.pipe(httpIn, sos);
		    			this.socket.shutdownOutput(); //This sends EOF in stream
		    			System.out.println("********* Convertion(TCP -> HTTP) completed ***********");
		        	}catch(Exception e){
		        		System.out.println(e);
		        	}
		        }
				private void printDateTime() {
					DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	        		Date dateobj = new Date();
	        		System.out.println(df.format(dateobj));
				}
		    }
		    Thread t = new Thread(new OneShotTask(soc));
		    t.start();
	}

}
