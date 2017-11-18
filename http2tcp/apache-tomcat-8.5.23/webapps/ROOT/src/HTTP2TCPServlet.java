import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainServlet
 */
public class HTTP2TCPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static int socketServerPort = 8888;
	private static final Logger LOGGER = Logger.getLogger("HTTP2TCPServlet");

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		convertHttp2Tcp(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		convertHttp2Tcp(request,response);
	}
	
	private void convertHttp2Tcp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
		LOGGER.log(Level.INFO, "*********Starting the convertion (HTTP -> TCP) ***********");
		LOGGER.log(Level.INFO, "Received Auth Header ::" + request.getHeader("Authorization"));
		int socketServerPort = Integer.valueOf(request.getHeader("DFS_PORT"));
		String socketServerIP = request.getHeader("DFS_IP").substring(1);	
		LOGGER.log(Level.INFO, "DFS IP: " + socketServerIP + "  DFS Port: " + socketServerPort);
	//	InetAddress socketServerAddr = InetAddress.getByAddress(socketServerIP.getBytes());
    		Socket clientSocket = new Socket(socketServerIP, socketServerPort);
		//clientSocket.setTcpNoDelay(true);
    		InputStream sIn = new BufferedInputStream(clientSocket.getInputStream());
		OutputStream sOut = new BufferedOutputStream(clientSocket.getOutputStream());
		
		InputStream httpIn = new BufferedInputStream(request.getInputStream());
		OutputStream httpOut = new BufferedOutputStream(response.getOutputStream());
		
		LOGGER.log(Level.INFO,"********* Piping http req to socket req *********");
		pipe(httpIn, sOut);
		//sOut.flush();// ##??
		clientSocket.shutdownOutput();
		//httpIn.close();

		LOGGER.log(Level.INFO,"********* Piping socket res to http res *********");
		pipe(sIn, httpOut);
		httpOut.close();
		
		LOGGER.log(Level.INFO,"********* Closing the resoureces *********");
		clientSocket.close();
		} catch(Exception e){
			LOGGER.log(Level.SEVERE, "Exception occured : ", e);
		}
		
	}
	
	private void pipe(InputStream is, OutputStream os) throws IOException {
	    byte[] buffer = new byte[2048];
		try {
			int read = 0;
             while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);//, offset, read);
		LOGGER.log(Level.INFO, "Message: " + new String(buffer));
             }
            os.flush();
        } catch(Exception e) {
             LOGGER.log(Level.WARNING,"Exception occurred while piping te streams :: " + e);
	    }
    }
}

