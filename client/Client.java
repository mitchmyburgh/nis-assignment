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

public class Client {
	public static void main(String[] args) {
	
	Socket clientSocket = null;
	DataInputStream is = null;
	PrintStream os = null;
	DataInputStream inputLine = null;

	//KeyChain.printMe();
	
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
			 String responseLine = " The client started. Type any text. to quit it type 'ok'";
			 String message;
			 
			 //os.println(inputLine.readLine());
			do {
				System.out.println(responseLine);
				
				//Encrypt here
				message = inputLine.readLine();
				if (message.equals("ok")){
					break;
				}
				System.out.println("===== Message =====");
				System.out.println(message);
				String theHash=Hash.hash(message);
				System.out.println("Hash ="+theHash);
				String encryptedH = KeyChain.encrypt(theHash,KeyChain.PRIVATE_KEY_CLIENT);
				System.out.println("======="+encryptedH);
				String signedMessage=message+"<SignedHashStartsHere>"+encryptedH;
				byte[] zippedText=Zipfile.compress(signedMessage);
				System.out.println("hererererer = "+zippedText);
				String encryptedText=AES.encrypt("Bar12345Bar12345", "RandomInitVector", new String(org.apache.commons.codec.binary.Base64.encodeBase64(zippedText)));
				String encryptedKeys=KeyChain.encrypt("Bar12345Bar12345"+"<InitialisatioVectorStartsHere>"+"RandomInitVector",KeyChain.PUBLIC_KEY_SERVER);
				String output=encryptedText+"<EncryptedKeyStartsHere>"+encryptedKeys;
				System.out.println(output);

					os.println(output);
					}  while ((responseLine = is.readLine()) != null);
					
					
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
			 
	 
	 
