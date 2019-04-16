import java.io.*;
import java.security.*;
import javax.crypto.*;

//import de.flexiprovider.*;
import de.flexiprovider.core.FlexiCoreProvider;

public class ExampleCrypt {

public static void main(String a[]) throws Exception
{
    Security.addProvider(new FlexiCoreProvider());
    Cipher cipher = Cipher.getInstance("AES128_CBC", "FlexiCore");
    KeyGenerator keyGenerator=KeyGenerator.getInstance("AES","FlexiCore");
    SecretKey seckey=keyGenerator.generateKey();

    cipher.init(Cipher.ENCRYPT_MODE,seckey);
    String CTF="C:\\Users\\DELL\\Desktop\\pt.txt";
    String ETF="C:\\Users\\DELL\\Desktop\\ct.txt";

    //String ETF="C:\\Users\\DELL\\IdeaProjects\\wns_md5\\src\\ETF.txt";

    FileInputStream fis=new FileInputStream(CTF);
    FileOutputStream fos=new FileOutputStream(ETF);

    CipherOutputStream cos=new CipherOutputStream(fos,cipher);

    byte[] block=new byte[8];
    int i;
    while((i=fis.read(block))!=-1)
    {
        cos.write(block,0,i);
    }

    cos.close();

    // encrytion part done
    String PTFA="C:\\Users\\DELL\\Desktop\\pta.txt";

    cipher.init(Cipher.DECRYPT_MODE,seckey);

    fis=new FileInputStream(CTF);
    CipherInputStream cis=new CipherInputStream(fis,cipher);
    fos=new FileOutputStream(PTFA);
    while((i=fis.read(block))!=-1)
    {
        fos.write(block,0,i);

    }

    fos.close();




}

}
