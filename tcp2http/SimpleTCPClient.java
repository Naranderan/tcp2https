import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;


public class SimpleTCPClient {
	public static void main(String argv[]) throws Exception {
		String sentence = "Zoho Code - For the programmers, By the programmers...";

		//Socks proxy domain and port - In our case, US3 SOCKS proxy ip and port 
		SocketAddress proxyAddr = new InetSocketAddress("naran-3387", 1090);
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
		
		//Actual destination ip and port - In our case, it will be DFS server ips and ports.
		SocketAddress endpointAddr = new InetSocketAddress("192.168.225.91", 54310); 
		Socket clientSocket = new Socket(proxy);
		clientSocket.connect(endpointAddr);
/*		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(sentence + '\n');
		clientSocket.shutdownOutput();
		String modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
*/
		//Socket socket = new Socket("192.168.225.91", 54310);
		

		/*
		 * This is the HTTP connection so it won't use the socks proxy. 
		 */
/*
		URL url = new URL("http://web/");
		URLConnection conn = url.openConnection();
		byte[] buffer = new byte[102400];
		InputStream is = conn.getInputStream();
		is.read(buffer);
		System.out.println("HTTP Output :: \n" + new String(buffer));
*/
	}
}