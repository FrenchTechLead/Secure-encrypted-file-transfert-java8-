package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import java.io.FilePermission;

import org.apache.commons.lang3.ArrayUtils;

import encryption.Security;
import encryption.Utils;



public class FtpServer implements Runnable {
	private DataInputStream serverInputStream;
	private DataOutputStream serverOutputStream;
	private ObjectInputStream ois;
	private ObjectOutputStream oos ;
	private ServerSocket serverSocket;
	static FtpServer server ;
	int port ;
	
	// on fait du serveur un SingleTon
	private FtpServer( int port){
		this.port = port;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static FtpServer getInstance(int port){
		if(server == null)server = new FtpServer(port);
		else{
			JOptionPane.showMessageDialog(null, "Server Already up !", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return server;		
	}
	
	
	@Override
    public void run () {  
    	System.out.println("En attente de connexion ...");
    	while(true){         
            try {
				accept();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
    }
	
	
    public void accept() throws Exception {
    	Socket soc = null;

    	try {
			soc = serverSocket.accept();
			System.out.println("Nouveau client: " + soc);
	        serverInputStream = new DataInputStream(soc.getInputStream());
	        serverOutputStream = new DataOutputStream(soc.getOutputStream());
	        oos = new ObjectOutputStream(serverOutputStream);
	        ois = new ObjectInputStream (serverInputStream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Server Can't Accept Client !", "Error", JOptionPane.ERROR_MESSAGE);
		}  
    	
    	KeyPair serverKeyPair = Security.generateRSAKeyPair();
		PrivateKey serverPrivateKey = serverKeyPair.getPrivate();
		PublicKey  serverPublicKey = serverKeyPair.getPublic();
		X509Certificate cert =   Security.createCertificate("FTP Secure Server", serverKeyPair);
		
		Signature signature = Signature.getInstance("SHA256withRSA");
    	SignedObject signedCert = new SignedObject(cert, serverPrivateKey, signature);
		
    	oos.writeObject(signedCert);
    	oos.writeObject(serverPublicKey);
    	
    	SignedObject signedClientCertificate = (SignedObject) ois.readObject();
		PublicKey  clientPublicKey = (PublicKey) ois.readObject();
		X509Certificate clientCertificate = (X509Certificate) signedClientCertificate.getObject();
		
		if (!signedClientCertificate.verify(clientPublicKey, signature)){
			Thread.currentThread().sleep((long) 5000);
			Thread.currentThread().destroy();
		}
		
		// Verification du certificat du serveur aupres de la CA
		try {
			clientCertificate.verify(clientPublicKey);
		} catch (InvalidKeyException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace()[0], e.getMessage(), JOptionPane.ERROR_MESSAGE);
			Thread.currentThread().sleep((long) 5000);
			Thread.currentThread().destroy();
		} catch (CertificateException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace()[0], e.getMessage(), JOptionPane.ERROR_MESSAGE);
			Thread.currentThread().sleep((long) 5000);
			Thread.currentThread().destroy();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace()[0], e.getMessage(), JOptionPane.ERROR_MESSAGE);
			Thread.currentThread().sleep((long) 5000);
			Thread.currentThread().destroy();
		} catch (NoSuchProviderException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace()[0], e.getMessage(), JOptionPane.ERROR_MESSAGE);
			Thread.currentThread().sleep((long) 5000);
			Thread.currentThread().destroy();
		} catch (SignatureException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace()[0], e.getMessage(), JOptionPane.ERROR_MESSAGE);
			Thread.currentThread().sleep((long) 15000);
			Thread.currentThread().destroy();
		}
		
		JOptionPane.showMessageDialog(null, "Client's certificate has been reveived Correctly : !\n"
				+ clientCertificate.getIssuerDN()+"\n"
				+ clientCertificate.getSigAlgName()+"\n", "Success", JOptionPane.INFORMATION_MESSAGE);
		
		
		File workingDirectory = new File(System.getProperty("user.dir"));
		String userFolder = workingDirectory.getAbsolutePath().concat("/filesInServer/"+clientCertificate.getIssuerDN());
		new File(userFolder).mkdir();
		
		KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
	    SecretKey sessionKey = keygenerator.generateKey();
	    
		byte[] sessionKeyEncrypted = Security.getDESKeyEncryptedByRSA(clientPublicKey, sessionKey);
		
    	SignedObject signedSessionKeyEncrypted = new SignedObject(sessionKeyEncrypted, serverPrivateKey, signature);
		
		// Envoi de la cle de session
		oos.writeObject(signedSessionKeyEncrypted);
		
		
		// Test
	/*	ArrayList<String> array = new ArrayList<>();
		array.add("Akram");
		byte [] serialise = Security.DesEncrypt(sessionKey, Utils.serialize(array) ) ;
		serverOutputStream.writeInt(serialise.length);
		serverOutputStream.write(serialise);*/
		
		while(true){
			int available = serverInputStream.readInt();
			byte[] textCrypted = new byte [available];
			serverInputStream.readFully(textCrypted, 0, available);
			byte[] decripted = Security.DesDecrypt(sessionKey, textCrypted);
	    	HashMap<String, Byte[]> request = (HashMap<String, Byte[]>) Utils.deserialize(decripted);
	    	Set <String> key = request.keySet();
	    	Iterator<String> keyIterator = key.iterator();
	    	String requestTitle = (String) keyIterator.next();
	    	
	    	if(requestTitle.contains("send")){
	    		String fileName = requestTitle.substring(5);
	    		byte[] receivedFile = ArrayUtils.toPrimitive(request.get(requestTitle));
	    		Utils.writeFile(clientCertificate.getIssuerDN()+"/"+fileName, receivedFile);
	    		JOptionPane.showMessageDialog(null, "The File"+fileName+" Was received successfuly", "Server Success", JOptionPane.INFORMATION_MESSAGE);
	    	
	    	}
	    	
	    	
	    	// To get specefic file in server
	    	else if(requestTitle.contains("get") ){
	    		String fileName = requestTitle.substring(4);
	    			
				   File selectedFile = new File(fileName);
				   Path path = Paths.get(selectedFile.getAbsolutePath());
				   byte[] bytes = null;
				   try {
					bytes = Files.readAllBytes(path);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				   HashMap<String, Byte[]> toSend = new HashMap<String, Byte[]>();
				   toSend.clear();
				   toSend.put("send "+fileName, ArrayUtils.toObject(bytes));
				   byte[] toSendBytes = null;
				try {
					toSendBytes = Utils.serialize(toSend);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   byte[] toSendBytesCrypted = Security.DesEncrypt(sessionKey, toSendBytes);
				   try {
					   serverOutputStream.writeInt(toSendBytesCrypted.length);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   
				   
				   
				   try {
					   serverOutputStream.write(toSendBytesCrypted);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		
	    	}
	    	
	    	
	    	// To get Files List in server
	    	else if(requestTitle.contains("GETALL")){
	    		ArrayList<String> files = new ArrayList<String>();
	    		Files.walk(Paths.get(userFolder)).forEach(filePath -> {
	    		    if (Files.isRegularFile(filePath)) {
	    		    	files.add(filePath.toString());
	    		    }
	    		});
	    		byte [] filesList = Utils.serialize(files);
	    		byte [] filesListEncrypted = Security.DesEncrypt(sessionKey, filesList);
	    		serverOutputStream.writeInt(filesListEncrypted.length);
	    		serverOutputStream.write(filesListEncrypted);
	    	}
		}
    }
	
	

    
	
	
	
	
}
