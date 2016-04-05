package test;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import java.io.PrintStream;
import java.io.OutputStream;

import common.Hash;

public class TestHash {

   @Test
   public void testHash() {
      System.out.println("Testing common.Hash.hash()");
      String expectedOutput = "b10a8db164e0754105b7a99be72e3fe5";
      //suppress output via http://stackoverflow.com/questions/8363493/hiding-system-out-print-calls-of-a-class
      PrintStream originalStream = System.out;
      PrintStream dummyStream    = new PrintStream(new OutputStream(){
          public void write(int b) {
          }
      });
      System.setOut(dummyStream);
      String output = Hash.hash("Hello World");
      System.setOut(originalStream);
      assertEquals(output, expectedOutput);
   }
}
