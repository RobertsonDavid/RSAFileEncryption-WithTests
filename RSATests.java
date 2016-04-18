import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import org.junit.Before;

public class RSATests
{
  public static void main(String args[])
  {
    org.junit.runner.JUnitCore.main("RSATests");
  }
  RSA rsaTest;
  int KEY_GEN_INPUT=715;
  
  @Before
  public void initialize()throws IOException
  {
    rsaTest= new RSA(KEY_GEN_INPUT);
    PrintWriter pubwriter = new PrintWriter("test.txt", "UTF-8");
    pubwriter.println("hello world and all who inhabit it!");
    pubwriter.println("just Going to test a bit he385902820fjx;'/.,[]\\=-!@#$%^&*()1234567890 =\\][|}{:?></., zxcvbnm lkjh gfdsa qwe r t yuiop");
    pubwriter.close();
  }
  
  //tests to see if keys are generated
  @Test
  public void keyGenTest()throws IOException
  {
    Boolean privKeyGen= false;
    Boolean pubKeyGen=false;

    File priv= new File("private.txt");
    File pub = new File("public.txt");
    In inputPriv= new In(priv);
    In inputPub=new In(pub);
    
    BigInteger npub= new BigInteger(inputPub.readString());
    BigInteger e= new BigInteger(inputPub.readString());
    BigInteger npriv= new BigInteger(inputPriv.readString());
    BigInteger d= new BigInteger(inputPriv.readString());
    
    if(npub!=null && e!=null)
      pubKeyGen=true;
    
    if(npriv!=null && d!=null)
      privKeyGen=true;
    
    assertTrue(privKeyGen && pubKeyGen);
    
  }
  
  //make sure that the n values are created as equal values on both public and private files
  @Test
  public void equalNTest()throws IOException
  {
    
    File priv= new File("private.txt");
    File pub = new File("public.txt");
    In inputPriv= new In(priv);
    In inputPub=new In(pub);
    
    BigInteger npub= new BigInteger(inputPub.readString());
    BigInteger npriv= new BigInteger(inputPriv.readString());
    
    assertEquals(npub, npriv);
  }
  
  //test to see that the encode file is created
  @Test
  public void encodeTest()throws IOException
  { 
    rsaTest.Encode("test.txt");
    File test= new File("test.txt.enc");
    assertTrue(test.isFile());
  }
  
  //test to see that the decode file is created
  //note that the encode test must be successful for this test to be accurate
  @Test
  public void decodeTest()throws IOException
  {
    rsaTest.Encode("test.txt");
    rsaTest.Decode("test.txt.enc");
    File test= new File("test.txt.cop");
    assertTrue(test.isFile());
  }
  
  //test to ensure that the encoded file differs from the original
  //requires that encoded test is successful for this test to be accurate
  @Test
  public void isEncodedTest()throws IOException
  {
    
    rsaTest.Encode("test.txt");
    
    File encFile= new File("test.txt.enc");
    File orgFile = new File("test.txt");
    In input1= new In(encFile);
    In input2=new In(orgFile);
    
    String org= input2.readAll();
    String enc=input1.readAll();
    
    assertFalse(org.equals(enc));
  }
  
  //test that the decoded file is the same as the original file before encoding
  //needs the encoding tests to have been successful to throw usefull result
  //needs the decodeTest to be successful
  @Test
  public void isDecodedTest()throws IOException
  {
    rsaTest.Encode("test.txt");
    rsaTest.Decode("test.txt.enc");
    
    File decFile= new File("test.txt.cop");
    File orgFile = new File("test.txt");
    In input1= new In(decFile);
    In input2=new In(orgFile);
    
    String org= input2.readAll();
    String dec=input1.readAll();
    assertEquals(org,dec);
  }
  
  //make sure there are public and Private key files created
  @Test
  public void keyFileTest()throws IOException
  {
    
    boolean publc= false;
    boolean privat=false;
    File priv=new File("private.txt");
    File pub= new File("public.txt");
    
    if(priv.isFile())
      privat=true;
    
    if(pub.isFile())
      publc=true;
    
    assertTrue(privat && publc);
  }
  
  //test that the public and private keys are different
  @Test
  public void keyDifferenceTest()
  {
    File priv= new File("private.txt");
    File pub= new File("public.txt");
    In privat=new In(priv);
    In publ= new In(pub);
    
    String privateNum= privat.readString();
    privateNum=privat.readString();
    
    String publicNum= publ.readString();
    publicNum=publ.readString();
    
    assertFalse(publicNum.equals(privateNum));
  }
  
  //tests that the keys from any two key gens are not the same
  @ Test
  public void keyRandomizeTest()throws IOException
  {
    In pub=new In(new File("public.txt"));
    In priv= new In(new File("private.txt"));
    
    String pubOrig= pub.readString();
    String privOrigFirstVal=priv.readString();
    String privOrigSecondVal=priv.readString();
    
    RSA rsa2= new RSA(KEY_GEN_INPUT);
    In pub2=new In(new File("public.txt"));
    In priv2= new In(new File("private.txt"));
 
    assertFalse(pubOrig.equals(pub2.readString()));
    assertFalse(privOrigFirstVal.equals(priv2.readString()));
    assertFalse(privOrigSecondVal.equals(priv2.readString()));
    
  }
}