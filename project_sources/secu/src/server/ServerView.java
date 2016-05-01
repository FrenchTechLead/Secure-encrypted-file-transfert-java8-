package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ServerView   {

	private JFrame frame ;
    private JButton btnStartserver;
    private JTextField portTextField;
    private JLabel portLabel ;
	public ServerView() {
		
        frame = new JFrame("Serveur de Transfere de fichiers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        frame.setSize(600,400);        
        frame.getContentPane().setLayout(null);
        

        
    

       
        portLabel = new JLabel("Port");
        portLabel.setBounds(450, 48, 70, 36);
        frame.getContentPane().add(portLabel);
        portTextField = new JTextField();
        portTextField.setBounds(450, 96, 50, 36);
        portTextField.setText("9494");
        frame.getContentPane().add(portTextField);
        
        
        btnStartserver = new JButton("StartServer");        
        btnStartserver.setBounds(134, 185, 316, 36);
        frame.getContentPane().add(btnStartserver); 
        
        frame.setVisible(true);
	}

	
	
	
	//****************Getters et Setters*************************
	
	public JButton getBtnStartserver() {
		return btnStartserver;
	}
	public JTextField getPortTextField() {
		return portTextField;
	}

	
	
	
	
	
	
}
