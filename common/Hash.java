package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	public static String hash(String plainText)
	{
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
		return hash;
	}
	public static void main(String[] args){
		String a=Hash.hash("Hello World I am tumelobjdfdffgjhiofgfgjiogjiofgjio");
		System.out.println(a);
		}

}
