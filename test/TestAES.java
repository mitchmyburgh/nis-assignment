package test;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import java.io.PrintStream;
import java.io.OutputStream;

import common.AES;

public class TestAES {

   @Test
   public void testEncrypt() {
      System.out.println("Testing common.AES.encrypt()");
      //test produces correct encrypted string
      String key = "Bar12345Bar12345";
      String initVector = "RandomInitVector";
      String expectedOutput = "9MU7vSBqfzPnj7iWvvfsEw==";
      //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
      PrintStream originalStream = System.out;
      PrintStream dummyStream    = new PrintStream(new OutputStream(){
          public void write(int b) {
          }
      });
      System.setOut(dummyStream);
      String output = AES.encrypt(key, initVector, "Hello World");
      System.setOut(originalStream);
      assertEquals(output, expectedOutput);
   }

   @Test
   public void testDecrypt() {
      System.out.println("Testing common.AES.decrypt()");
      //test produces correct encrypted string
      String key = "Bar12345Bar12345";
      String initVector = "RandomInitVector";
      String expectedOutput = "Hello World";
      //suppress output
      PrintStream originalStream = System.out;
      PrintStream dummyStream    = new PrintStream(new OutputStream(){
          public void write(int b) {
          }
      });
      System.setOut(dummyStream);
      String output = AES.decrypt(key, initVector, "9MU7vSBqfzPnj7iWvvfsEw==");
      System.setOut(originalStream);
      assertEquals(output, expectedOutput);
   }

}
