import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class q1_cln 
{
    public static void main(String[] args) throws Exception 
    {

	long start = System.currentTimeMillis();

	// localhost for testing
	Socket sock = new Socket("127.0.0.1", 13267);
	System.out.println("Connecting...");
        
        // file to be encrypted
	FileInputStream inFile = new FileInputStream("/home/user/20164101.sql");

	// encrypted file
	FileOutputStream outFile = new FileOutputStream("encryptedfile.des");

	// password to encrypt the file
	String password = "javapapers";

	// password, iv and salt should be transferred to the other end
	// in a secure manner

	// salt is used for encoding
	// writing it to a file
	// salt should be transferred to the recipient securely
	// for decryption
	byte[] salt = new byte[8];
	SecureRandom secureRandom = new SecureRandom();
	secureRandom.nextBytes(salt);
	FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
	saltOutFile.write(salt);
	saltOutFile.close();

	SecretKeyFactory factory = SecretKeyFactory
			.getInstance("PBKDF2WithHmacSHA1");
	KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
			256);
	SecretKey secretKey = factory.generateSecret(keySpec);
	SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

	//
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, secret);
	AlgorithmParameters params = cipher.getParameters();

	// iv adds randomness to the text and just makes the mechanism more
	// secure
	// used while initializing the cipher
	// file to store the iv
	FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
	byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
	ivOutFile.write(iv);
	ivOutFile.close();

	//file encryption
	byte[] input = new byte[64];
	int bytesRead;

	while ((bytesRead = inFile.read(input)) != -1) {
		byte[] output = cipher.update(input, 0, bytesRead);
		if (output != null)
			outFile.write(output);
	}

	byte[] output = cipher.doFinal();
	if (output != null)
		outFile.write(output);

	inFile.close();
	outFile.flush();
	outFile.close();

	System.out.println("File Encrypted.");
        
        InputStream is = sock.getInputStream();
        // receive file
        //new q1_cln().receiveFile(is);
        OutputStream os = sock.getOutputStream();
        new q1_cln().send(os);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        sock.close();
    }


    public void send(OutputStream os) throws Exception 
    {
        // sendfile
        File myFile = new File("encryptedfile.des");
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

        do 
        {
            bytesRead = is.read(mybytearray, current,
                    (mybytearray.length - current));
            if (bytesRead >= 0)
                current += bytesRead;
        } while (bytesRead > -1);

        bos.write(mybytearray, 0, current);
        bos.flush();
        bos.close();
    }
} 
