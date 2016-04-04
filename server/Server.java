package server;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import common.KeyChain;
import common.AES;
import common.Hash;
import common.Zipfile;

/**
 * Addpated from Keet, M. (2015). CSC3002F – Networks Assignment 2015: A simple client/server application
 */
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
		while (true){
			try {
				clientSocket= echoServer.accept();
				is = new DataInputStream(clientSocket.getInputStream());
				os = new PrintStream(clientSocket.getOutputStream());

				// process the recieved file
				while (true) {
					line = is.readLine();
					if (line == null){
						System.out.println("=== Connection Closed ===");
						break;
					} else {
						System.out.println("=== Recieved Message ===");
						System.out.println(line);
						//split encrypted message and encrypted key, seperated by <EncryptedKeyStartsHere>
						String[] parts = line.split("<EncryptedKeyStartsHere>");
						if (parts.length==2){ //check for key and message
							System.out.println("=== Encrypted Keys ===");
							System.out.println(parts[1]);
							String sessionKey = KeyChain.decrypt(parts[1], KeyChain.PRIVATE_KEY_SERVER);
							String[] keys = sessionKey.split("<InitialisatioVectorStartsHere>");
							if (keys.length == 2) { //check for key and initialisation vector
								System.out.println("=== Session Key and Initialisation Vector ===");
								System.out.println("Session Key: "+keys[0]+" and Initialisation Vector: "+keys[1]);

								System.out.println("=== Encrypted & Zipped Message ===");
								System.out.println(parts[0]);

								String zippedFile = AES.decrypt(keys[0],keys[1],parts[0]);

								System.out.println("=== Zipped Message ===");
								System.out.println(zippedFile);

								String plainTextAndHash = Zipfile.decompress(org.apache.commons.codec.binary.Base64.decodeBase64(zippedFile.getBytes()));
								System.out.println("=== Plain Text And Signed Hash ===");
								System.out.println(plainTextAndHash);
								parts = plainTextAndHash.split("<SignedHashStartsHere>");
								if (parts.length==2){ //check for plaintext and hash
									System.out.println("=== Signed Hash===");
									System.out.println(parts[1]);

									String recievedHash = KeyChain.decrypt(parts[1], KeyChain.PUBLIC_KEY_CLIENT);
									String calculatedHash = Hash.hash(parts[0]);

									System.out.println("=== Recieved Hash===");
									System.out.println(recievedHash);

									System.out.println("=== Calculated Hash===");
									System.out.println(calculatedHash);

									System.out.println("=== Plaintext Message ===");
									System.out.println(parts[0]);

									if (recievedHash.equals(calculatedHash)){ //check hash equal
										System.out.println("The content of the message is authenticated and the integrity has been verified");
										os.println("The content of the message is authenticated and the integrity has been verified");
									} else {
										System.out.println("Hash mismatch, message cannot be authenticated");
										os.println("Hash mismatch, message cannot be authenticated");
									}
								} else {
									System.out.println("Hash not found");
									os.println("Hash not found");
								}
							} else {
								System.out.println("Initialisation Vector not found");
								os.println("Initialisation Vector not found");
							}
						}
						else {
							System.out.println("Key not found");
							os.println("Key not found");
						}
					}

				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
