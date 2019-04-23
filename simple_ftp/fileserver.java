import java.net.*;
import java.util.*;
import java.io.*;

public class fileserver {

    public  static  void main(String a[]) throws Exception
    {
        ServerSocket ss=new ServerSocket(4000);
        Socket sc=ss.accept();
        FileInputStream fr=new FileInputStream("E:\\hi.txt");
        byte b[]=new byte[200];
        OutputStream os=sc.getOutputStream();
        fr.read(b,0,b.length);
        os.write(b,0,b.length);
    }

}
