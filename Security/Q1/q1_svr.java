import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class q1_svr 
{
    public static void main(String[] args) throws Exception 
    {
        // create socket
        ServerSocket servsock = new ServerSocket(13267);
        while (true) {
            System.out.println("Waiting...");

            Socket sock = servsock.accept();
            System.out.println("Accepted connection : " + sock);
		        
            OutputStream os = sock.getOutputStream();
            //new q1_svr().send(os);
            InputStream is = sock.getInputStream();
            new q1_svr().receiveFile(is);
            sock.close();
        }
    }

    public void send(OutputStream os) throws Exception 
    {
        // sendfile
        File myFile = new File("/home/user/SAVE.fig");
        byte[] mybytearray = new byte[(int) myFile.length() + 1];
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray, 0, mybytearray.length);
        System.out.println("Sending...");
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
    }

    public void receiveFile(InputStream is) throws Exception 
    {
        int filesize = 6022386;
        int bytesRead;
        int current = 0;
        byte[] mybytearray = new byte[filesize];

        FileOutputStream fos = new FileOutputStream("def");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(mybytearray, 0, mybytearray.length);
        current = bytesRead;

        do {
            bytesRead = is.read(mybytearray, current,
                    (mybytearray.length - current));
            if (bytesRead >= 0)
                current += bytesRead;
        } while (bytesRead > -1);

        bos.write(mybytearray, 0, current);
        bos.flush();
        bos.close();
        
        String password = "javapapers";

	// reading the salt
	// user should have secure mechanism to transfer the
	// salt, iv and password to the recipient
	FileInputStream saltFis = new FileInputStream("salt.enc");
	byte[] salt = new byte[8];
	saltFis.read(salt);
	saltFis.close();

	// reading the iv
	FileInputStream ivFis = new FileInputStream("iv.enc");
	byte[] iv = new byte[16];
	ivFis.read(iv);
	ivFis.close();

	SecretKeyFactory factory = SecretKeyFactory
			.getInstance("PBKDF2WithHmacSHA1");
	KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
			256);
	SecretKey tmp = factory.generateSecret(keySpec);
	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

	// file decryption
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
	FileInputStream fis = new FileInputStream("encryptedfile.des");
	FileOutputStream fos1 = new FileOutputStream("plainfile_decrypted.txt");
	byte[] in = new byte[64];
	int read;
	while ((read = fis.read(in)) != -1) {
		byte[] output = cipher.update(in, 0, read);
		if (output != null)
			fos1.write(output);
	}

	byte[] output = cipher.doFinal();
	if (output != null)
		fos1.write(output);
	fis.close();
	fos1.flush();
	fos1.close();
	System.out.println("File Decrypted.");
    }
} 
