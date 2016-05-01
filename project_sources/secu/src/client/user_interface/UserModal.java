package client.user_interface;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.crypto.SecretKey;

public class UserModal {

	private DataInputStream  inputStream ;
	private DataOutputStream OutputStream ;
	private SecretKey sessionKey ;
	public UserModal(DataInputStream inputStream,
			DataOutputStream outputStream, SecretKey sessionKey) {
		
		this.inputStream = inputStream;
		OutputStream = outputStream;
		this.sessionKey = sessionKey;
	}
	public DataInputStream getInputStream() {
		return inputStream;
	}
	public DataOutputStream getOutputStream() {
		return OutputStream;
	}
	public SecretKey getSessionKey() {
		return sessionKey;
	}
	
	
}
