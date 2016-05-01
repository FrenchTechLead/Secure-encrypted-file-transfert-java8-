package server;


import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

	public static void main(String[] args) {
		
		Security.addProvider(new BouncyCastleProvider());
		
		
		// TODO Auto-generated method stub
		ServerModal modal = new ServerModal();
		ServerView  view  = new ServerView();
		ServerController controller = new ServerController(view, modal);
		controller.control();

	}

}
