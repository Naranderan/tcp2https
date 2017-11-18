import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//TODO: Need to change it as a multi-threaded/thread-pooled server.
public class SampleTCPServer {
	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(Constants.RPROXY_TCP_PORT);
		String destLocation = "ReceivedFile";

		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			InputStream inFromClient = new BufferedInputStream(connectionSocket.getInputStream());
			OutputStream outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
   
			Socket socket = connectionSocket;
			System.out.println("Source IP : " + socket.getRemoteSocketAddress() + " Source Port : " + socket.getPort());
    		System.out.println("Dest IP : " + socket.getLocalAddress() + " Dest Port : " + socket.getLocalPort());
    		System.out.println("Started the writing....");
    		
    		readTheFileFromSocket(destLocation, inFromClient);
			try{
				outToClient.write("Given data is written successfully. This is ACK.".getBytes());
				outToClient.flush();
				connectionSocket.shutdownOutput();
//				outToClient.close();
			}catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	private static boolean readTheFileFromSocket(String destLocation, InputStream inFromClient) throws IOException {
		OutputStream fos = new BufferedOutputStream(new FileOutputStream(destLocation));
		CommonUtil.pipe(inFromClient, fos);
		return true;
	}
}