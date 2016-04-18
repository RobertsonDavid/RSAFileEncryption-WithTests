/*Created By: David M Robertson
 * 
 * Input for a keygen is needed initially; use a small integer ex.719
 * must be a reasonably small integer or the performance will decrease exponentially
 * The larger the integer provided, the larger the keys generated
 * 
 * after the keys have been generated, two inputs will be needed
 *    [-decrypt | -encrypt] (file.txt)
 * 
 * Use a text file to either encrypt or decrypt
*/
import java.math.BigInteger;
import java.util.Random;
import java.io.*;

public class RSA
{
  public static BigInteger d;
  public static BigInteger s;
  public static BigInteger t;
  public static void main (String args[])throws IOException
  {
    if(args.length==1)
    {
      KeyGen(Integer.parseInt(args[0]));
      return;
    }
    
    else if(args.length==2)
    {
      if(args[0].equals("-encrypt"))
      {
        Encode(args[1]);
        return;
      }
      
      if(args[0].equals("-decrypt"))
      {
        Decode(args[1]);
        return;
      }
    }
    
    else
    {
      System.out.println("invalid arguments");
      System.exit(1);
    }  
  }
  
  public RSA(int input)throws IOException
  {
    KeyGen(input);
    return;
  }

  public RSA(String input, String option)throws IOException
  {
    if(option.equals("-encrypt"))
      {
        Encode(input);
        return;
      }
      
      if(option.equals("-decrypt"))
      {
        Decode(input);
        return;
      }
  }
  
  public static void Decode(String fileName) throws IOException
  {
    File inFile = new File("private.txt");
    In input1= new In(inFile);
    
    
    BigInteger n= new BigInteger(input1.readString());
    BigInteger d= new BigInteger(input1.readString());
    
    In input= new In(fileName);
    
    String fileArr[]=fileName.split(".enc");
    int flen= fileArr.length;
    int it=0;
    fileName=fileArr[0];
    String newFileName=fileName+".cop";
    PrintWriter writer = new PrintWriter(newFileName, "UTF-8");
    
    while(input.hasNextLine())
    {
      String ln=input.readLine();
      String ints[]=ln.split(" ");
      
      int i=0;
     
      while(i<ints.length)
      {
        char dm;
        if(ints[i].equals("")==false)
        {
          
          BigInteger tchar= new BigInteger(ints[i]);
          char c =(char) tchar.modPow(d,n).intValue();
          
          writer.print(c );
        }
        i++;
        
      }
      
      writer.print("\n");
    }
    
    writer.close();
    System.out.println("Message Decrypted.");
  }
  
  public static void Encode(String fileName) throws IOException
  {
    File inFile = new File("public.txt");
    In input1= new In(inFile);
    
    BigInteger n= new BigInteger(input1.readString());
    BigInteger e= new BigInteger(input1.readString());
    
    In input= new In(fileName);
    
    String newFileName=fileName+".enc";
    PrintWriter writer = new PrintWriter(newFileName, "UTF-8");
    
    while(input.hasNextLine())
    {
      String ln=input.readLine();
      int i=ln.length();
      int t=0;
      while(t<i)
      {
        String change=""+(int)ln.charAt(t);
        BigInteger tchar= new BigInteger(change);
        BigInteger c = tchar.modPow(e,n);
        
        writer.print(c + " ");
        
        t++;
      }
      writer.print("\n");
    }
    
    writer.close();
    System.out.println("Message Encrypted.");
  }
  
  
  public static void KeyGen(int primeBits) throws IOException
  {
    Random rnd = new Random();
    
    BigInteger p= new BigInteger(primeBits,128,rnd);
    BigInteger q;
    do q = new BigInteger(primeBits,128,rnd);
    while(p.compareTo(q) == 0);
    
    BigInteger n = p.multiply(q);
    BigInteger pMinus1 = p.subtract(BigInteger.valueOf(1));
    BigInteger qMinus1 = q.subtract(BigInteger.valueOf(1));
    BigInteger m = pMinus1.multiply(qMinus1);
    
    BigInteger e = BigInteger.valueOf(3);
    while(e.gcd(m).compareTo(BigInteger.valueOf(1)) > 0)
      e = e.add(BigInteger.valueOf(2));
    
    XGCD(e,m);
    
    System.out.println("n= " + n+ "\n\nphi= " +m + "\n\ne = " + e + "\n\nd = "+s);
    
    PrintWriter pubwriter = new PrintWriter("public.txt", "UTF-8");
    pubwriter.println(n);
    pubwriter.println(e);
    pubwriter.close();
    
    PrintWriter writer = new PrintWriter("private.txt", "UTF-8");
    writer.println(n);
    writer.println(s);
    writer.close();
    
  }

  public static void XGCD(BigInteger a, BigInteger b)
  {
    BigInteger u, v, u0, v0, u1, v1, u2, v2, q, r;
    long aneg = 0, bneg = 0;
    
    
    
    if (a.compareTo(BigInteger.ZERO) == -1) 
    {
      a = a.negate();
      aneg = 1;
    }
    
    if (b.compareTo(BigInteger.ZERO)==-1) 
    {
      b = b.negate();
      bneg = 1;
    }
    
    if (a.compareTo(BigInteger.ZERO)==-1 || b.compareTo(BigInteger.ZERO)==-1)
    {
      System.out.println("Error: XGCD: integer overflow");
      System.exit(1);
      
    }
    
    u1=BigInteger.ONE; v1=BigInteger.ZERO;
    u2=BigInteger.ZERO; v2=BigInteger.ONE;
    u = a; v = b;
    
    while (v.compareTo( BigInteger.ZERO)!=0) 
    {
      q = u.divide( v);
      r = u.mod( v);
      u = v;
      v = r;
      u0 = u2;
      v0 = v2;
      u2 =  u1.subtract( (q.multiply(u2)));
      v2 = v1.subtract( q.multiply(v2));
      u1 = u0;
      v1 = v0;
      
    }
    
    if (aneg==1)
      u1 = u1.negate();
    
    if (bneg==1)
      v1 = v1.negate();
    
    
    d = u;
    s = u1;
    t = v1;

    d=a.multiply(s);
    BigInteger test=b.multiply(t);
    d=d.add(test);

    
    if(s.compareTo(BigInteger.ZERO)==-1)
      s=s.add(b);
  }

}