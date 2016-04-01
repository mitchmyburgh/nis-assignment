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
    }
        /**
        * Code adapted from http://stackoverflow.com/questions/29221947/unable-to-use-public-rsa-key-pem-file-created-with-bouncycastle-to-encrypt-fil
        */
    public static void writeKeys(String publicPath, String privatePath) {
        System.out.println("Writing Keypair: "+publicPath+" and "+privatePath);
        Security.addProvider(new BouncyCastleProvider());

        KeyPair keyPair = generateRSAKeyPair();
        PrivateKey priv = keyPair.getPrivate();
        PublicKey pub = keyPair.getPublic();
        writePemFile(priv, "PRIVATE KEY", privatePath);
        writePemFile(pub, "PUBLIC KEY", publicPath);
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
        KeyString = KeyString.replaceAll("-----BEGIN PUBLIC KEY-----", "");
        KeyString = KeyString.replaceAll("-----END PUBLIC KEY-----", "");
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
        KeyString = KeyString.replaceAll("-----BEGIN PRIVATE KEY-----", "");
        KeyString = KeyString.replaceAll("-----END PRIVATE KEY-----", "");
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
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        PublicKey pubKey = null;
        PrivateKey privKey = null;
        switch (keyInt){
            case 0:
                pubKey = readPublicKeyNative("./keys/generated/server_pub.pem");
                System.out.println("Encrypting with Server Public Key");
                break;
            case 1:
                privKey = readPrivateKeyNative("./keys/generated/server_priv.pem");
                System.out.println("Encrypting with Server Private Key");
                break;
            case 2:
                pubKey = readPublicKeyNative("./keys/generated/client_pub.pem");
                System.out.println("Encrypting with Client Private Key");
                break;
            case 4:
                privKey = readPrivateKeyNative("./keys/generated/client_priv.pem");
                System.out.println("Encrypting with Client Private Key");
                break;
            default:
                System.out.println("Failed to find key");
                break;
        }
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
            return "";
        }
        byte[] ciphertext = null;
        try {
            ciphertext = cipher.doFinal(plaintext.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } 
        byte[] ciphertextB64 = org.apache.commons.codec.binary.Base64.encodeBase64(ciphertext);
        System.out.println("plaintext: " + plaintext);
        System.out.println("ciphertext: " + new String(ciphertextB64));
        return new String(ciphertextB64);
    }

    public static String decrypt(String ciphertext, int keyInt) {
        Cipher cipher = null;
        byte[] ciphertextByte = org.apache.commons.codec.binary.Base64.decodeBase64(ciphertext.getBytes());
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        PublicKey pubKey = null;
        PrivateKey privKey = null;
        switch (keyInt){
            case 0:
                pubKey = readPublicKeyNative("./keys/generated/server_pub.pem");
                System.out.println("Decrypting with Server Public Key");
                break;
            case 1:
                privKey = readPrivateKeyNative("./keys/generated/server_priv.pem");
                System.out.println("Decrypting with Server Private Key");
                break;
            case 2:
                pubKey = readPublicKeyNative("./keys/generated/client_pub.pem");
                System.out.println("Decrypting with Client Private Key");
                break;
            case 4:
                privKey = readPrivateKeyNative("./keys/generated/client_priv.pem");
                System.out.println("Decrypting with Client Private Key");
                break;
            default:
                System.out.println("Failed to find key");
                break;
        }
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
        try {
            plaintext = cipher.doFinal(ciphertextByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }  
        byte[] plaintextB64 = org.apache.commons.codec.binary.Base64.encodeBase64(plaintext);
        System.out.println("ciphertext: " + ciphertext);
        System.out.println("plaintext: " + new String(plaintext));
        return new String(plaintext);
    }    


    /*public static void printMe (){
        System.out.println("hello");
    }*/
}