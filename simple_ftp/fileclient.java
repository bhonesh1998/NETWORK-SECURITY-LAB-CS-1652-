import java.net.*;
import java.util.*;
import java.io.*;
public class fileclient
{
    public  static  void main(String a[]) throws Exception
    {
        byte[] b=new byte[200];
        Socket s=new Socket("127.0.0.1",4000);
        InputStream is=s.getInputStream();
        is.read(b,0,b.length);
        FileOutputStream fr=new FileOutputStream("E:\\wowowoow.txt");
        fr.write(b,0,b.length);
    }
}

