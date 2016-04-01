package common;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec; 
import java.security.NoSuchAlgorithmException;

public class AES{

static byte[] salt = {
    (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
    (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
};

public static SecretKey generateKeySpec(String pwd){
	char[] password=pwd.toCharArray();
	SecretKey secret=null;
	try{
	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
	SecretKey tmp = factory.generateSecret(spec);
	secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return secret;
}

public static String encrypt(String key, String toEncrypt) throws Exception {
    Key skeySpec = generateKeySpec(key);
    byte[] encryptedValue=null;
		try {
					
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
				byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
				encryptedValue = Base64.encodeBase64(encrypted);
    
		} 
		catch (Exception e) 
		{
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
    
    
    return new String(encryptedValue);
}

public static String decrypt(String key, String encrypted) throws Exception {
    Key skeySpec = generateKeySpec(key);
    byte[] original=null;
    	try {
					
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				byte[] decodedBytes = Base64.decodeBase64(encrypted.getBytes());
				original = cipher.doFinal(decodedBytes);
    
		} 
		catch (NoSuchAlgorithmException exception) 
		{
					// TODO Auto-generated catch block
					exception.printStackTrace();
		}
    
    return new String(original);
}

public static void main(String[] args){


		try {
					
				String e=encrypt("aaaaaaaaaaaaaaaa","Hello world");
				System.out.println(e);
				String d=decrypt("aaaaaaaaaaaaaaaa",e);
				System.out.println(d);
		} 
		catch (Exception e) 
		{
					// TODO Auto-generated catch block
					e.printStackTrace();
		}

}

}
