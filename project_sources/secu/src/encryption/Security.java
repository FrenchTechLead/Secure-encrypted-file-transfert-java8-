package encryption;

import java.math.BigInteger;
import java.security.*;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.x509.X509V1CertificateGenerator;




public class Security {

	
	
	public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		 
			 KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			 generator.initialize(1024);		 
			 KeyPair keyPair = generator.generateKeyPair();	 
			 return keyPair;	 
	}
	
	
	
	
	
	
	
	
	public static X509Certificate createCertificate(String owner, KeyPair keyPair) throws Exception{
		Date startDate = new Date();              // time from which certificate is valid
		Date expiryDate = DateUtils.addMonths(startDate, 1); // time after which certificate is not valid
		BigInteger serialNumber = Utils.randomBigInteger(BigInteger.valueOf(2121212121));
							
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		X500Principal              dnName = new X500Principal("CN="+owner);
		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(dnName);
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(dnName);                       // note: same as issuer
		certGen.setPublicKey(keyPair.getPublic());
		certGen.setSignatureAlgorithm("MD5withRSA");
		X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");

		return cert ;
	}
	
	
	
	
	
	public static byte[] getDESKeyEncryptedByRSA(PublicKey  rsaKey ,SecretKey aesKey ) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA", "BC");
		cipher.init(Cipher.WRAP_MODE, rsaKey);
		byte[] wrapped = cipher.wrap(aesKey);
		return wrapped;
	}
	
	
	
	public static SecretKey getDesKeyDecrypted(byte[] aesCrypted , PrivateKey privateRsaKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA", "BC");
		cipher.init(Cipher.UNWRAP_MODE, privateRsaKey);
		SecretKey sessionKey = (SecretKey) cipher.unwrap(aesCrypted, "DES",
		    Cipher.SECRET_KEY);
		return sessionKey;
	}

	
	
	public static byte[] DesEncrypt(SecretKey secretKey, byte[] dataToEncrypt ){
		Cipher desCipher = null;
		try {
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] textEncrypted = null;
		try {
			textEncrypted = desCipher.doFinal(dataToEncrypt);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textEncrypted;
	}
	
	
	public static byte[] DesDecrypt(SecretKey secretKey, byte[] dataToDecrypt){
		Cipher desCipher = null;
		try {
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			desCipher.init(Cipher.DECRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] textDecrypted= null;
		try {
			textDecrypted = desCipher.doFinal(dataToDecrypt);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textDecrypted;
	}
	
}