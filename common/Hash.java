package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* Modified from http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
*/
public class Hash {

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
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		System.out.println("hash: "+hash);
		System.out.println("=== Hash.hash() finished ===");
		return hash;
	}
	public static void main(String[] args){
		String a=Hash.hash("Hello World I am Tumelo");
		System.out.println(a);
	}

}
