package server;



public class ServerController  {
	ServerView view;
	ServerModal modal;
	
	public ServerController(ServerView view , ServerModal modal){
		this.modal = modal;
		this.view = view;
	}
	
	public void control(){
		
		view.getBtnStartserver().addActionListener(e ->{
			FtpServer ftpServer = FtpServer.getInstance(Integer.parseInt(view.getPortTextField().getText()));
			Thread serverThread = new Thread(ftpServer);
			serverThread.start();
			view.getBtnStartserver().setEnabled(false);
			view.getBtnStartserver().setText("Server is Up !");
		});
		
	}
	
}
