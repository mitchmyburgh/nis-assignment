import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
	public static void main(String args[]) {
		ServerSocket echoServer = null;
		String line;
		DataInputStream is;
		PrintStream os;
		Socket clientSocket = null;
		int port = 6969;

		/*
		* Open server socket on port 6969
		*/
		try {
			echoServer = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
		}

		/*
		* Create a socket object from the Serevr Socket to listen to and accept connections. Open input and output streams;
		*/
		System.out.println("Server listening on port "+port+". <CTRL><C> to exit.");
		try {
			clientSocket= echoServer.accept();
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			/* Echo data back to client */
			while (true) {
				line = is.readLine();
				os.println("From server: "+line);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}