package client.receiveFile_interface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ReceiveFileView {
	private JFrame frame ;
    private JLabel filesLabel;
    private JButton getFileButton;  
    private JComboBox<String> comboBox;
	
	public ReceiveFileView( ArrayList<String> files){
		frame = new JFrame("Recevoir Un Fichier");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);          
        frame.setSize(800,600);        
        frame.getContentPane().setLayout(null);
        
        filesLabel = new JLabel("Please select a file :");
        filesLabel.setBounds(12, 48, 400, 36);
        frame.getContentPane().add(filesLabel);
                    
        
        getFileButton = new JButton("Get File !");        
        getFileButton.setBounds(134, 500, 316, 36);
        frame.getContentPane().add(getFileButton); 
        
        comboBox = new JComboBox<String>();
        comboBox.setBounds(12, 96, 650, 36);
        for(String item : files)comboBox.addItem(item);
        frame.getContentPane().add(comboBox);
        
        frame.setVisible(true);
	}

	public JButton getFileButton() {
		return getFileButton;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}
	
	
}
