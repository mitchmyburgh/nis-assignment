
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.codec.binary.Base64;

public class AES{

byte[] salt = {
    (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
    (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
};

public generateKeySpec(String password){
	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
	SecretKey tmp = factory.generateSecret(spec);
	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
}

public static String encrypt(String key, String toEncrypt) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
}

public static String decrypt(String key, String encrypted) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decodedBytes = Base64.decodeBase64(encrypted.getBytes());
    byte[] original = cipher.doFinal(decodedBytes);
    return new String(original);
}

public static void main(String[] args){
String e=encrypt("1","Hello world");
System.out.println(e);
String d=decrypt("1",e);
System.out.println(d);
}

}
