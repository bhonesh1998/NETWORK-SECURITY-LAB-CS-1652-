import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import de.flexiprovider.core.FlexiCoreProvider;

public class ExampleRSA {

    public static void main(String[] args) throws Exception {

	Security.addProvider(new FlexiCoreProvider());

	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "FlexiCore");
	Cipher cipher = Cipher.getInstance("RSA", "FlexiCore");

	kpg.initialize(1024);
	KeyPair keyPair = kpg.generateKeyPair();
	PrivateKey privKey = keyPair.getPrivate();
	PublicKey pubKey = keyPair.getPublic();

	// Encrypt

	cipher.init(Cipher.ENCRYPT_MODE, pubKey);

	String cleartextFile = "cleartext.txt";
	String ciphertextFile = "ciphertextRSA.txt";

	FileInputStream fis = new FileInputStream(cleartextFile);
	FileOutputStream fos = new FileOutputStream(ciphertextFile);
	CipherOutputStream cos = new CipherOutputStream(fos, cipher);

	byte[] block = new byte[32];
	int i;
	while ((i = fis.read(block)) != -1) {
	    cos.write(block, 0, i);
	}
	cos.close();

	// Decrypt

	String cleartextAgainFile = "cleartextAgainRSA.txt";

	cipher.init(Cipher.DECRYPT_MODE, privKey);

	fis = new FileInputStream(ciphertextFile);
	CipherInputStream cis = new CipherInputStream(fis, cipher);
	fos = new FileOutputStream(cleartextAgainFile);

	while ((i = cis.read(block)) != -1) {
	    fos.write(block, 0, i);
	}
	fos.close();
    }

}
