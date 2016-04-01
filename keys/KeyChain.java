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
                writeKeys("./server_pub.pem", "./server_priv.pem");
                // generate client key pair
                writeKeys("./client_pub.pem", "./client_priv.pem");
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
    /*public static void printMe (){
        System.out.println("hello");
    }*/
}