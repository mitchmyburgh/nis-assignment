package keys;

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
        public static final int KEY_SIZE = 2046;
        public static final int PUBLIC_KEY_SERVER = 0;
        public static final int PRIVATE_KEY_SERVER = 1;
        public static final int PUBLIC_KEY_CLIENT = 2;
        public static final int PRIVATE_KEY_CLIENT = 3;
	// http://stackoverflow.com/questions/5789685/rsa-encryption-with-given-public-key-in-java
	public static void main (String [] args) throws Exception {
		/*KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
                        new BigInteger("d46f473a2d746537de2056ae3092c451", 16),
                        new BigInteger("11", 16));
                RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(
                        new BigInteger("d46f473a2d746537de2056ae3092c451", 16),  
                        new BigInteger("57791d5430d593164082036ad8b29fb1", 16));

                RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
                RSAPrivateKey privKey = (RSAPrivateKey)keyFactory.generatePrivate(privKeySpec);
                System.out.println(pubKey);
                System.out.println(privKey);*/
                // generate server key pair
                writeKeys("./keys/generated/server_pub.pem", "./keys/generated/server_priv.pem");
                // generate client key pair
                writeKeys("./keys/generated/client_pub.pem", "./keys/generated/client_priv.pem");
        }
        /**
        * Code adapted from http://stackoverflow.com/questions/29221947/unable-to-use-public-rsa-key-pem-file-created-with-bouncycastle-to-encrypt-fil
        */
        public static void writeKeys(String publicPath, String privatePath) {
            Security.addProvider(new BouncyCastleProvider());

            KeyPair keyPair = generateRSAKeyPair();
            PrivateKey priv = keyPair.getPrivate();
            PublicKey pub = keyPair.getPublic();
            writePemFile(priv, "PRIVATE KEY", privatePath);
            writePemFile(pub, "PUBLIC KEY", publicPath);
            System.out.println("Hello");
    }

    private static KeyPair generateRSAKeyPair(){
        KeyPairGenerator generator=null;
        try {
                generator = KeyPairGenerator.getInstance("RSA", "BC");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
            SecureRandom random = new SecureRandom();
            generator.initialize(KEY_SIZE, random);
            KeyPair keyPair = generator.generateKeyPair();
            return keyPair;
        }

         public static void writePemFile(Key key, String description,String filename) {

        PemObject pemObject = new PemObject(description, key.getEncoded());
        PemWriter pemWriter=null;
        try {
            pemWriter = new PemWriter(new OutputStreamWriter(
                    new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            pemWriter.writeObject(pemObject);
            pemWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }

    public static PublicKey readPublicKeyNative(String publicKeyPath) {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = null;
        PublicKey key = null;
        byte[] publicKeyFileBytes = null;

        try {
            publicKeyFileBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String KeyString = new String(publicKeyFileBytes);
        //System.out.println(KeyString);
        //System.out.println("FORMATTED:");
        KeyString = KeyString.replaceAll("-----BEGIN RSA PUBLIC KEY-----", "");
        KeyString = KeyString.replaceAll("-----END RSA PUBLIC KEY-----", "");
        KeyString = KeyString.replaceAll("[\n\r]", "");
        KeyString = KeyString.trim();
        //System.out.println(KeyString);

        byte[] encoded = Base64.getDecoder().decode(KeyString);

        // PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            factory = KeyFactory.getInstance("RSA");
            key = factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    public static PrivateKey readPrivateKeyNative(String privateKeyPath) {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = null;
        PrivateKey key = null;
        byte[] privateKeyFileBytes = null;

        try {
            privateKeyFileBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String KeyString = new String(privateKeyFileBytes);
        //System.out.println(KeyString);
        //System.out.println("FORMATTED:");
        KeyString = KeyString.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "");
        KeyString = KeyString.replaceAll("-----END RSA PRIVATE KEY-----", "");
        KeyString = KeyString.replaceAll("[\n\r]", "");
        KeyString = KeyString.trim();
        //System.out.println(KeyString);

        byte[] encoded = Base64.getDecoder().decode(KeyString);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            factory = KeyFactory.getInstance("RSA");
            key = factory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    public static String encrypt(String plaintext, int keyInt) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        switch (keyInt){
            case 0:
                PublicKey key = readPublicKeyNative("./keys/generated/server_pub.pem");
                break;
            case 1:
                PrivateKey key = readPrivateKeyNative("./keys/generated/server_priv.pem");
                break;
            case 2:
                PublicKey key = readPublicKeyNative("./keys/generated/client_pub.pem");
                break;
            case 4:
                PrivateKey key = readPrivateKeyNative("./keys/generated/client_priv.pem");
                break;
            default:
                PrivateKey key = null;
                System.out.println("Failed to find key");
                break;
        }
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes());

    }

    public static String decrypt(String ciphertext, int keyInt) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        switch (keyInt){
            case 0:
                PublicKey key = readPublicKeyNative("./keys/generated/server_pub.pem");
                break;
            case 1:
                PrivateKey key = readPrivateKeyNative("./keys/generated/server_priv.pem");
                break;
            case 2:
                PublicKey key = readPublicKeyNative("./keys/generated/client_pub.pem");
                break;
            case 4:
                PrivateKey key = readPrivateKeyNative("./keys/generated/client_priv.pem");
                break;
            default:
                PrivateKey key = null;
                System.out.println("Failed to find key");
                break;
        }
    }    


    /*public static void printMe (){
        System.out.println("hello");
    }*/
}