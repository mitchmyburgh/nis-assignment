import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import common.KeyChain;
import common.AES;
import common.Hash;
import common.Zipfile;


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
				System.out.println("==== Recieved Message ====");
				System.out.println(line);
				String[] parts = line.split("<EncryptedKeyStartsHere>");
				if (parts.length==2){
					String sessionKey = KeyChain.decrypt(parts[1], KeyChain.PRIVATE_KEY_SERVER);
					String[] keys = sessionKey.split("<InitialisatioVectorStartsHere>");
					System.out.println("Session Key: "+keys[0]+" and Initialisation Vector"+keys[1]);
					System.out.println("==== Encrypted & Zipped Message ====");
					System.out.println(parts[0]);
					String zippedFile = AES.decrypt(keys[0],keys[1],parts[0]);
					String plainTextAndHash = Zipfile.decompress(zippedFile.getBytes()); 
					parts = line.split("<SignedHashStartsHere>");
					String recievedHash = KeyChain.decrypt(parts[1], KeyChain.PUBLIC_KEY_CLIENT);
					String calculatedHash = Hash.hash(parts[0]);
					System.out.println("==== Plaintext Message =====");
					System.out.println(parts[0]);
					if (recievedHash.equals(calculatedHash)){
						System.out.println("The content of the message is authenticated\nand the integrity has been verified");
					}

				}
				else 
					System.out.println("Key not found");


			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
