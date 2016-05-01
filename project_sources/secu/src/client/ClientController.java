package client;

import server.FtpServer;

public class ClientController {
	ClientModal modal ;
	ClientView view;
	public ClientController(ClientModal modal, ClientView view) {
		this.modal = modal;
		this.view = view;
	}
	
	
	public void control(){
		view.getconnectButton().addActionListener(e ->{
			FtpClient ftpClient =  FtpClient.getInstance (view.getIpTextField().getText(),
					Integer.parseInt(view.getPortTextField().getText()));
			try {
				ftpClient.run();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			view.getconnectButton().setEnabled(false);
			view.getconnectButton().setText("Connected !");
		});
	}

}
