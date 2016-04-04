package client;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import common.KeyChain;
import common.Hash;
import common.AES;
import common.Zipfile;

/**
 * Addpated from Keet, M. (2015). CSC3002F – Networks Assignment 2015: A simple client/server application
 */
public class Client {
	public static void main(String[] args) {

		Socket clientSocket = null;
		DataInputStream is = null;
		PrintStream os = null;
		DataInputStream inputLine = null;

		/*
		* open a socket on port 6969.
		*/

		try {
			clientSocket = new Socket ("localhost", 6969);
			os =  new PrintStream (clientSocket.getOutputStream());
			is = new DataInputStream (clientSocket.getInputStream());
			//inputLine = new DataInputStream ( clientSocket.getInputStream());
			inputLine =  new DataInputStream (new BufferedInputStream(System.in));
		} catch (UnknownHostException e) {
			System.err.println(" Don't know about host");
		}
		catch (IOException e)
		{
			System.err.println("couldn't get I/O for the connection to host");
		}

		/*
		* after initialising then write data to tyhe socket we have opened a connection to on port 2222.
		*/



		if (clientSocket != null && os != null && is != null) {
			try{

				/*
				*
				*/

				//System.out.println(" The client started. Type any text. to quit it type 'ok'");
				String responseLine = "The client started. Type any text. To quit it type 'ok'";
				String message;
				String recievedMessage = null;

				//os.println(inputLine.readLine());
				do {
					if ((recievedMessage)!=null)
						System.out.println(recievedMessage);

					System.out.println(responseLine);

					//Encrypt here
					message = inputLine.readLine();
					if (message.equals("ok")){
						break;
					}
					System.out.println("=== Message ===");
					System.out.println(message);

					String theHash=Hash.hash(message);

					System.out.println("=== Hash ===");
					System.out.println(theHash);

					String encryptedH = KeyChain.encrypt(theHash,KeyChain.PRIVATE_KEY_CLIENT);

					System.out.println("=== Encrypted Hash ===");
					System.out.println(encryptedH);

					String signedMessage=message+"<SignedHashStartsHere>"+encryptedH;

					System.out.println("=== Signed Message ===");
					System.out.println(signedMessage);

					byte[] zippedText=Zipfile.compress(signedMessage);

					System.out.println("=== Zippped File ===");
					System.out.println(zippedText);

					String encryptedText=AES.encrypt("Bar12345Bar12345", "RandomInitVector", new String(org.apache.commons.codec.binary.Base64.encodeBase64(zippedText)));

					System.out.println("=== Encrypted File ===");
					System.out.println(encryptedText);

					System.out.println("=== Key and Initialisation Vector ===");
					System.out.println("Key: Bar12345Bar12345"+"Initialisation Vector: "+"RandomInitVector");

					String encryptedKeys=KeyChain.encrypt("Bar12345Bar12345"+"<InitialisatioVectorStartsHere>"+"RandomInitVector",KeyChain.PUBLIC_KEY_SERVER);

					System.out.println("=== Encrypted Keys ===");
					System.out.println(encryptedKeys);

					String output=encryptedText+"<EncryptedKeyStartsHere>"+encryptedKeys;

					System.out.println("=== Sent Bundle ===");
					System.out.println(output);

					os.println(output);
				} while ((recievedMessage=is.readLine())!=null);


				os.close();
				is.close();
				clientSocket.close();

			} catch (UnknownHostException e) {
				System.err.println("Try to connect to unknown host: " +e );
			} catch (IOException e){
				System.err.println("IOException: " + e);
			}
		}
	}
}
