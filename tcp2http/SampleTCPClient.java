import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SampleTCPClient {
	
	public static void main(String argv[]) throws Exception {
		String fileName = "TestFile";
		
		Socket clientSocket = new Socket(Constants.PROXY_HOST, 50010);// Constants.PROXY_TCP_PORT);
	 
		//Writing data to socket.
		writeTheFileToSocket(fileName, clientSocket);
		clientSocket.shutdownOutput(); //To send EOF - If this method is not called then InputStream.read() may block in server side.

		//Reading data from socket.
		InputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
		byte[] buffer = new byte[Constants.BUFFER_SIZE];
		inFromServer.read(buffer);
		
		clientSocket.close();
	 
		System.out.println("Received from server : " + new String(buffer));
	}

	private static void writeTheFileToSocket(String fileName, Socket socket) throws Exception {
		File file = new File(fileName);
		InputStream fis = new BufferedInputStream(new FileInputStream(file));
		OutputStream sos = new BufferedOutputStream(socket.getOutputStream());
		CommonUtil.pipe(fis, sos);
		System.out.println("File is read from loation");
 }
}