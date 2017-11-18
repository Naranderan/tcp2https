import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//$Id$

public class HTTP2TCP {
	
	static final int socketServerPort = 8888;
	static final int httpListenPort = 7777;

	
	public static void main(String[] args) throws Exception {
		System.out.println("Level 1");
	    HttpServer server = HttpServer.create(new InetSocketAddress(httpListenPort), 0);
	    server.createContext("/http2tcp", new MyHandler());
	    server.setExecutor(null); // creates a default executor
	    server.start();
	  }

	  static class MyHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {
	    	System.out.println("Level 11");
	    	Socket clientSocket = new Socket("localhost", socketServerPort);
	    	//InputStream sIn = clientSocket.getInputStream();
    		OutputStream sOut = clientSocket.getOutputStream();
    		
    		System.out.println("Level 12");
    		InputStream httpIn = new BufferedInputStream(t.getRequestBody());
    		OutputStream httpOut = new BufferedOutputStream(t.getResponseBody());
    		
    		System.out.println("Level 13");
    		//System.out.println("Data in HTTP req :: " + httpIn.read());
    		pipe(httpIn, sOut);
    		System.out.println("Level 14");
    		//pipe(sIn, httpOut);
    		clientSocket.close();
    		httpIn.close();
    		httpOut.close();
	    }
	    
	    private void pipe(InputStream is, OutputStream os) throws IOException {
			byte[] buffer = new byte[4096];
			try {
				//while ((read = is.read(buffer)) != -1) {
					os.write(is.read(buffer));
				//}
			} catch(Exception e) {
				System.out.println(e);
			}
	    }
	 }
	
}
