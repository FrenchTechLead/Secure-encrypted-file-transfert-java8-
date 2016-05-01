package encryption;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Utils {

	public static BigInteger randomBigInteger(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while (result.compareTo(n) >= 0) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		os.close();
		return out.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		in.close();
		ObjectInputStream is = new ObjectInputStream(in);
		is.close();
		return is.readObject();

	}
	
	public static void writeFile(String fileName, byte[] fileBytes) throws IOException{
		File workingDirectory = new File(System.getProperty("user.dir"));
		String file = workingDirectory.getAbsolutePath().concat("/filesInServer/"+fileName);	
		FileOutputStream output = new FileOutputStream(new File(file));
		IOUtils.write(fileBytes, output);
		output.close();
	}
	
	public static void writeFile2(String fileName, byte[] fileBytes) throws IOException{
		File workingDirectory = new File(System.getProperty("user.dir"));
		String file = workingDirectory.getAbsolutePath().concat(fileName);	
		FileOutputStream output = new FileOutputStream(new File(file));
		IOUtils.write(fileBytes, output);
		output.close();
	}
	
	
}