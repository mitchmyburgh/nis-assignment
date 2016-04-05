package test;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import java.io.PrintStream;
import java.io.OutputStream;

import common.Zipfile;

public class TestZipfile {

   @Test
   public void testCompress() {
      System.out.println("Testing common.Zipfile.compress()");
      String expectedOutput = "H4sIAAAAAAAAAPNIzcnJVwjPL8pJAQBWsRdKCwAAAA==";
      //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
      PrintStream originalStream = System.out;
      PrintStream dummyStream    = new PrintStream(new OutputStream(){
          public void write(int b) {
          }
      });
      System.setOut(dummyStream);
      String output =  new String(org.apache.commons.codec.binary.Base64.encodeBase64(Zipfile.compress("Hello World")));
      System.setOut(originalStream);
      assertEquals(output, expectedOutput);
   }

    @Test
   public void testDecompress() {
      System.out.println("Testing common.Zipfile.decompress()");
      String expectedOutput = "Hello World";
      //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
      PrintStream originalStream = System.out;
      PrintStream dummyStream    = new PrintStream(new OutputStream(){
          public void write(int b) {
          }
      });
      System.setOut(dummyStream);
      String output =  Zipfile.decompress(org.apache.commons.codec.binary.Base64.decodeBase64("H4sIAAAAAAAAAPNIzcnJVwjPL8pJAQBWsRdKCwAAAA==".getBytes()));
      System.setOut(originalStream);
      assertEquals(output, expectedOutput);
   }
}
