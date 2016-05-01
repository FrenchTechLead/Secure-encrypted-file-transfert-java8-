package client.user_interface;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UserView {
	private JFrame frame ;
    private JButton sendFileButton;
    private JButton ReceiveFileButton;

    
    public UserView(){
    	frame = new JFrame("Client de Transfere de fichiers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);              
        frame.setSize(600,400);        
        frame.getContentPane().setLayout(null);
        
        
        sendFileButton = new JButton("Send File");        
        sendFileButton.setBounds(134, 185, 316, 36);
        frame.getContentPane().add(sendFileButton);
        
        ReceiveFileButton = new JButton("Receive File");        
        ReceiveFileButton.setBounds(134, 80, 316, 36);
        frame.getContentPane().add(ReceiveFileButton); 
        
        frame.setVisible(true);
    }


	public JButton getSendFileButton() {
		return sendFileButton;
	}


	public JButton getReceiveFileButton() {
		return ReceiveFileButton;
	}
    
    
    
	
}
