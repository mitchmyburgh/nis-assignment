public class AES{

public static String encrypt(String key, String toEncrypt) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES", new BouncyCastleProvider());
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
}

public static String decrypt(String key, String encrypted) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES", new BouncyCastleProvider());
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
