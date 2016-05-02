package client;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {
	
	public static void main(String [] args){
		
		Security.addProvider(new BouncyCastleProvider());
		
		ClientModal modal = new ClientModal();
		ClientView  view  = new ClientView();
		ClientController controller = new ClientController(modal, view);
		controller.control();
		
	}
	


}
