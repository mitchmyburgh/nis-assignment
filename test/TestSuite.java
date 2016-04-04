package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestAES.class,
   TestHash.class,
   TestKeyChain.class,
   TestZipfile.class
})
public class TestSuite {
}
