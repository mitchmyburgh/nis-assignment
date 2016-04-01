import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
	
	Socket clientSocket = null;
	DataInputStream is = null;
	PrintStream os = null;
	DataInputStream inputLine = null;
	
	/* 
	 * open a socket on port 2222.
	 */
	 
	 try { 
		clientSocket = new Socket ("localhost", 6969);
		os =  new PrintStream (clientSocket.getOutputStream());
		is = new DataInputStream (clientSocket. getInputStream());
		inputLine = new DataInputStream ( clientSocket.getInputStream());
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
			 
			 System.out.println(" The client started. Type any text. to quit it type 'ok'");
			 String responseLine;
			 os.println(inputLine.readLine());
			 while ((responseLine = is.readLine()) != null) {
				System.out.println(responseLine);
				if ( responseLine.indexOf("OK") != -1) {
					break;
					}
					os.println(inputLine.readLine());
					}
					
					
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
			 
	 
	 
