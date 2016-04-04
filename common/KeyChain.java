package common;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyChain {
  //instance variables
  public static final int KEY_SIZE = 2046;
  public static final int PUBLIC_KEY_SERVER = 0;
  public static final int PRIVATE_KEY_SERVER = 1;
  public static final int PUBLIC_KEY_CLIENT = 2;
  public static final int PRIVATE_KEY_CLIENT = 3;

  public static void main (String [] args) throws Exception {
    // generate server key pair
    writeKeys("./keys/generated/server_pub.pem", "./keys/generated/server_priv.pem");
    // generate client key pair
    writeKeys("./keys/generated/client_pub.pem", "./keys/generated/client_priv.pem");

    //test code
    System.out.println("=== Running Test ===");
    System.out.println(decrypt(encrypt("Hello world", PUBLIC_KEY_SERVER), PRIVATE_KEY_SERVER));
  }
  /**
  * Code adapted from http://stackoverflow.com/questions/29221947/unable-to-use-public-rsa-key-pem-file-created-with-bouncycastle-to-encrypt-fil
  */
 /**
  * writeKeys - write genreated pulic and private keys to their paths
  * @param  String publicPath - path for public key
  * @param  String privatePath - path for private key
  */
  public static void writeKeys(String publicPath, String privatePath) {
    System.out.println("Writing Keypair: "+publicPath+" and "+privatePath);
    Security.addProvider(new BouncyCastleProvider());

    KeyPair keyPair = generateRSAKeyPair(); //generate key pair
    PrivateKey priv = keyPair.getPrivate();
    PublicKey pub = keyPair.getPublic();
    writePemFile(priv, "PRIVATE KEY", privatePath); //write private key
    writePemFile(pub, "PUBLIC KEY", publicPath); //write public key
  }

  /**
   * generateRSAKeyPair -generate the key pair
   * @return KeyPair keyPair - the public/private key pair
   */
  private static KeyPair generateRSAKeyPair(){
    KeyPairGenerator generator=null;
    try {
      generator = KeyPairGenerator.getInstance("RSA", "BC"); //generate RSA key
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      e.printStackTrace();
    }
    SecureRandom random = new SecureRandom();
    generator.initialize(KEY_SIZE, random);
    KeyPair keyPair = generator.generateKeyPair(); //actually generate the keypair
    return keyPair;
  }

  /**
   * writePemFile - write the key to file
   * @param  Key key - the key
   * @param  String description - description of the key
   * @param  String filename - file name to save key to.
   */
  public static void writePemFile(Key key, String description,String filename) {
    PemObject pemObject = new PemObject(description, key.getEncoded());
    PemWriter pemWriter=null;
    try {
      pemWriter = new PemWriter(new OutputStreamWriter(
      new FileOutputStream(filename)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      pemWriter.writeObject(pemObject); //write key
      pemWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * readPublicKeyNative - read public key from file
   * @param  String publicKeyPath - path of key file
   * @return PublicKey key - the public key
   */
  public static PublicKey readPublicKeyNative(String publicKeyPath) {
    Security.addProvider(new BouncyCastleProvider());
    KeyFactory factory = null;
    PublicKey key = null;
    byte[] publicKeyFileBytes = null;

    try {
      publicKeyFileBytes = Files.readAllBytes(Paths.get(publicKeyPath)); //get key from file
    } catch (IOException e) {
      e.printStackTrace();
    }
    String KeyString = new String(publicKeyFileBytes); //remove description strings
    KeyString = KeyString.replaceAll("-----BEGIN PUBLIC KEY-----", "");
    KeyString = KeyString.replaceAll("-----END PUBLIC KEY-----", "");
    KeyString = KeyString.replaceAll("[\n\r]", "");
    KeyString = KeyString.trim();

    byte[] encoded = Base64.getDecoder().decode(KeyString);

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    try {
      factory = KeyFactory.getInstance("RSA");
      key = factory.generatePublic(keySpec); //get key from string
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return key;
  }

  /**
   * readPrivateKeyNative - read private key from file
   * @param  String privateKeyPath - the path of the key file
   * @return PrivateKey key - the key
   */
  public static PrivateKey readPrivateKeyNative(String privateKeyPath) {
    Security.addProvider(new BouncyCastleProvider());
    KeyFactory factory = null;
    PrivateKey key = null;
    byte[] privateKeyFileBytes = null;

    try {
      privateKeyFileBytes = Files.readAllBytes(Paths.get(privateKeyPath)); //read key
    } catch (IOException e) {
      e.printStackTrace();
    }
    String KeyString = new String(privateKeyFileBytes); //convert to string and trim descriptions
    KeyString = KeyString.replaceAll("-----BEGIN PRIVATE KEY-----", "");
    KeyString = KeyString.replaceAll("-----END PRIVATE KEY-----", "");
    KeyString = KeyString.replaceAll("[\n\r]", "");
    KeyString = KeyString.trim();

    byte[] encoded = Base64.getDecoder().decode(KeyString);

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    try {
      factory = KeyFactory.getInstance("RSA");
      key = factory.generatePrivate(keySpec); //get key from string
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return key;
  }

  /**
   * encrypt - encrypt plaintext using RSA
   * @param   String plaintext - the plaintext to be decrypted
   * @param   int keyInt - the id for getting the key
   * @return  String - the ciphertext
   */
  public static String encrypt(String plaintext, int keyInt) {
    System.out.println("=== KeyChain.encrypt() called ===");
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //using the recommended algorith,
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
    PublicKey pubKey = null;
    PrivateKey privKey = null;
    //get the correct public/private key
    switch (keyInt){
      case 0:
        pubKey = readPublicKeyNative("./keys/generated/server_pub.pem");
        System.out.println("=== Encrypting with Server Public Key ===");
        break;
      case 1:
        privKey = readPrivateKeyNative("./keys/generated/server_priv.pem");
        System.out.println("=== Encrypting with Server Private Key ===");
        break;
      case 2:
        pubKey = readPublicKeyNative("./keys/generated/client_pub.pem");
        System.out.println("=== Encrypting with Client Private Key ===");
        break;
      case 3:
        privKey = readPrivateKeyNative("./keys/generated/client_priv.pem");
        System.out.println("=== Encrypting with Client Private Key ===");
        break;
      default:
        System.out.println("Failed to find key");
        break;
    }
    //initialise the cipher with the key
    if (cipher != null && pubKey != null){
      try {
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      }
    } else if (cipher != null && privKey != null){
      try {
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      }
    } else {
      return ""; //failure
    }
    byte[] ciphertext = null;
    //encrypt the text
    try {
      ciphertext = cipher.doFinal(plaintext.getBytes());
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    byte[] ciphertextB64 = org.apache.commons.codec.binary.Base64.encodeBase64(ciphertext); //encodeBase64 the cipher text for ensuring no loss of data when converting to string
    System.out.println("Plaintext: " + plaintext);
    System.out.println("Ciphertext: " + new String(ciphertextB64));
    System.out.println("=== KeyChain.encrypt() finished ===");
    return new String(ciphertextB64);
  }

  /**
   * decrypt - decrypt ciphertext using RSA
   * @param   String ciphertext - the cipher text to be decrypted
   * @param   int keyInt - the id for getting the key
   * @return  String - the plaintext
   */
  public static String decrypt(String ciphertext, int keyInt) {
    System.out.println("=== KeyChain.decrypt() called ===");
    Cipher cipher = null;
    byte[] ciphertextByte = org.apache.commons.codec.binary.Base64.decodeBase64(ciphertext.getBytes()); //get bytes and decodeBase64 (reverse of encrypt)
    try {
      cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //use recommended algortith,
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
    PublicKey pubKey = null;
    PrivateKey privKey = null;
    // get correct public/private key
    switch (keyInt){
      case 0:
        pubKey = readPublicKeyNative("./keys/generated/server_pub.pem");
        System.out.println("=== Decrypting with Server Public Key ===");
        break;
      case 1:
        privKey = readPrivateKeyNative("./keys/generated/server_priv.pem");
        System.out.println("=== Decrypting with Server Private Key ===");
        break;
      case 2:
        pubKey = readPublicKeyNative("./keys/generated/client_pub.pem");
        System.out.println("=== Decrypting with Client Private Key ===");
        break;
      case 3:
        privKey = readPrivateKeyNative("./keys/generated/client_priv.pem");
        System.out.println("=== Decrypting with Client Private Key ===");
        break;
      default:
        System.out.println("Failed to find key");
        break;
    }
    //initialise cipher
    if (cipher != null && pubKey != null){
      try {
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      }
    } else if (cipher != null && privKey != null){
      try {
        cipher.init(Cipher.DECRYPT_MODE, privKey);
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      }
    } else {
      return "";
    }
    byte[] plaintext = null;
    //decrypt plaintext
    try {
      plaintext = cipher.doFinal(ciphertextByte);
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    System.out.println("Ciphertext: " + ciphertext);
    System.out.println("Plaintext: " + new String(plaintext));
    System.out.println("=== KeyChain.decrypt() finished ===");
    return new String(plaintext);
  }
}
