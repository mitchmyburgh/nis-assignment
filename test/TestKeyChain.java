package test;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.File;

import common.KeyChain;

public class TestKeyChain {

  @Test
  public void testConfidentialityServer() {
     System.out.println("Testing common.KeyChain.encrypt() and common.KeyChain.decrypt()");
     //test produces correct encrypted string
     String key = "Bar12345Bar12345";
     String initVector = "RandomInitVector";
     String expectedOutput = "Hello World";
     //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
     PrintStream originalStream = System.out;
     PrintStream dummyStream    = new PrintStream(new OutputStream(){
         public void write(int b) {
         }
     });
     System.setOut(dummyStream);
     String output = KeyChain.encrypt("Hello World", KeyChain.PUBLIC_KEY_SERVER);
     String finalOutput = KeyChain.decrypt(output, KeyChain.PRIVATE_KEY_SERVER);
     System.setOut(originalStream);
     assertEquals(finalOutput, expectedOutput);
  }

  @Test
  public void testAuthenticationServer() {
     System.out.println("Testing common.KeyChain.encrypt() and common.KeyChain.decrypt()");
     //test produces correct encrypted string
     String key = "Bar12345Bar12345";
     String initVector = "RandomInitVector";
     String expectedOutput = "Hello World";
     //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
     PrintStream originalStream = System.out;
     PrintStream dummyStream    = new PrintStream(new OutputStream(){
         public void write(int b) {
         }
     });
     System.setOut(dummyStream);
     String output = KeyChain.encrypt("Hello World", KeyChain.PRIVATE_KEY_SERVER);
     String finalOutput = KeyChain.decrypt(output, KeyChain.PUBLIC_KEY_SERVER);
     System.setOut(originalStream);
     assertEquals(finalOutput, expectedOutput);
  }

  @Test
  public void testConfidentialityClient() {
     System.out.println("Testing common.KeyChain.encrypt() and common.KeyChain.decrypt()");
     //test produces correct encrypted string
     String key = "Bar12345Bar12345";
     String initVector = "RandomInitVector";
     String expectedOutput = "Hello World";
     //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
     PrintStream originalStream = System.out;
     PrintStream dummyStream    = new PrintStream(new OutputStream(){
         public void write(int b) {
         }
     });
     System.setOut(dummyStream);
     String output = KeyChain.encrypt("Hello World", KeyChain.PUBLIC_KEY_CLIENT);
     String finalOutput = KeyChain.decrypt(output, KeyChain.PRIVATE_KEY_CLIENT);
     System.setOut(originalStream);
     assertEquals(finalOutput, expectedOutput);
  }

  @Test
  public void testAuthenticationClient() {
     System.out.println("Testing common.KeyChain.encrypt() and common.KeyChain.decrypt()");
     //test produces correct encrypted string
     String key = "Bar12345Bar12345";
     String initVector = "RandomInitVector";
     String expectedOutput = "Hello World";
     //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
     PrintStream originalStream = System.out;
     PrintStream dummyStream    = new PrintStream(new OutputStream(){
         public void write(int b) {
         }
     });
     System.setOut(dummyStream);
     String output = KeyChain.encrypt("Hello World", KeyChain.PRIVATE_KEY_CLIENT);
     String finalOutput = KeyChain.decrypt(output, KeyChain.PUBLIC_KEY_CLIENT);
     System.setOut(originalStream);
     assertEquals(finalOutput, expectedOutput);
  }

  @Test
  public void testWriteKeys() {
     System.out.println("Testing common.KeyChain.writeKeys()");
     //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
     PrintStream originalStream = System.out;
     PrintStream dummyStream    = new PrintStream(new OutputStream(){
         public void write(int b) {
         }
     });
     System.setOut(dummyStream);
     KeyChain.writeKeys("./keys/generated/server_pub.pem", "./keys/generated/server_priv.pem");
     System.setOut(originalStream);
     File pubKey = new File("./keys/generated/server_pub.pem");
     File privKey = new File("./keys/generated/server_priv.pem");
     assertTrue(pubKey.exists());
     assertTrue(privKey.exists());
  }

}
