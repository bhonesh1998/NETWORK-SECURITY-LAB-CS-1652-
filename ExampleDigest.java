import java.io.*;
import  java.security.*;
import de.flexiprovider.common.util.ByteUtils;
import de.flexiprovider.core.FlexiCoreProvider;

public class ExampleDigest {

    public static void main(String a[]) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new FlexiCoreProvider());
        File file = new File("C:\\Users\\DELL\\Desktop\\hi.txt");
        byte[] buffer = new byte[(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buffer);
        fis.close();

        MessageDigest md=MessageDigest.getInstance("MD5","FlexiCore");
        md.update(buffer);
        byte[] digest=md.digest();
        System.out.println("hash is"+ ByteUtils.toHexString(digest));


    }

}
