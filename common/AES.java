package common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
* Code adapted from http://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
*/
public class AES{

	/**
	* encrypt - encrypt plaintext
	* @param   String key - the key/password
	* @param   String initVector - the initialisation vector
	* @param   String value - the value to encrypt
	* @return  String - teh ciphertext
	*/
	public static String encrypt(String key, String initVector, String value) {
		System.out.println("=== AES.encrypt() called ===");
		System.out.println("Key: "+key+" Initialisation Vector: "+initVector);
		System.out.println("plaintext: "+value);
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //cipher with recommended algorithm
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes()); //encrypted plaintext
			System.out.println("encrypted string: "
			+ Base64.encodeBase64String(encrypted));
			System.out.println("=== AES.encrypt() finished ===");
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * decrypt - decrypt the plaintext
	 * @param   String key - the key/password
	 * @param   String initVector - the initialisation vector
	 * @param   String encrypted - the encrypted string
	 * @return  String - the plaintext
	 */
	public static String decrypt(String key, String initVector, String encrypted) {
		System.out.println("=== AES.decrypt() called ===");
		System.out.println("Key: "+key+" Initialisation Vector: "+initVector);
		System.out.println("ciphertext: "+encrypted);
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //cipher with recommended algorithm
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted)); //plaintext
			System.out.println("plaintext: "+ new String(original));
			System.out.println("=== AES.encrypt() finished ===");
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		String key = "Bar12345Bar12345"; // 128 bit key
		String initVector = "RandomInitVector"; // 16 bytes IV
		//test
		System.out.println(decrypt(key, initVector,
		encrypt(key, initVector, "Hello World")));
	}

}
