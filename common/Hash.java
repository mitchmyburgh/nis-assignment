package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* Modified from http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
*/
public class Hash {

	/*This method hash accepts plain text as a parameter and computes the hash of the plain text using MD5.
	The hash is computed by both the message sender and recepient to ensure that the message has not been modified
	*/
	public static String hash(String plainText)
	{
		System.out.println("=== Hash.hash() called ===");
		System.out.println("plaintext: "+plainText);
		String hash="";
		String data = plainText;
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data.getBytes());
			byte[] messageDigestMD5 = messageDigest.digest();
			StringBuffer stringBuffer = new StringBuffer();
			for (byte bytes : messageDigestMD5) {
				stringBuffer.append(String.format("%02x", bytes & 0xff));
			}
			hash=stringBuffer.toString();
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}
		System.out.println("hash: "+hash);
		System.out.println("=== Hash.hash() finished ===");
		return hash;
	}
	//The hashing algorithm is tested here
	public static void main(String[] args){
		String a=Hash.hash("Hello World I am Tumelo");
		System.out.println(a);
	}

}
