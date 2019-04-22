import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Server2 {
	private static Cipher eC;
	private static Cipher dC;

	public static void main(String[] args) {
		try{
			KeyPair kP = buildKeyPair();
			PublicKey pK = kP.getPublic();
			PrivateKey pvK = kP.getPrivate();

			eC = Cipher.getInstance("RSA");
			eC.init(Cipher.ENCRYPT_MODE, pK);

			dC = Cipher.getInstance("RSA");
			dC.init(Cipher.DECRYPT_MODE, pvK);

			encrypt(new FileInputStream("/home/l2code/Documents/check.txt"), new FileOutputStream("/home/l2code/Akash/FkYrSf/ss.txt"));

			decrypt(new FileInputStream("/home/l2code/Akash/FkYrSf/ss.txt"), new FileOutputStream("/home/l2code/Akash/FkYrSf/ss1.txt"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void encrypt(InputStream is, OutputStream os) throws IOException {
		os = new CipherOutputStream(os, eC);
		writeData(is, os);
	}

	private static void decrypt(InputStream is, OutputStream os) throws IOException {
		is = new CipherInputStream(is, dC);
		writeData(is, os);
	}

	private static void writeData(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		int num = 0;

		while((num = is.read(buf)) >= 0) {
			os.write(buf, 0, num);
		}
		os.close();
		is.close();
	}

	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
		final int keySize = 2048;
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.genKeyPair();
	}
}
