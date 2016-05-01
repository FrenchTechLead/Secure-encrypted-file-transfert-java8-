package client.user_interface;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;



import org.apache.commons.lang3.ArrayUtils;



import client.receiveFile_interface.ReceiveFileController;
import client.receiveFile_interface.ReceiveFileModal;
import client.receiveFile_interface.ReceiveFileView;



import javax.sound.midi.Receiver;
import javax.swing.JFileChooser;













import encryption.Security;
import encryption.Utils;


public class UserController {
	private UserModal modal;
	private UserView view ;
	public UserController(UserModal modal, UserView view) {
		this.modal = modal;
		this.view = view;
	}
	
	public void control(){
		
		
		
		
		
		
		
		
		view.getSendFileButton().addActionListener(e ->{
			JFileChooser chooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			chooser.setCurrentDirectory(workingDirectory);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			   System.out.println("You chose to open this file: " +
			        chooser.getSelectedFile().getName());
			   File selectedFile = chooser.getSelectedFile();
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
			   toSend.put("send "+chooser.getSelectedFile().getName(), ArrayUtils.toObject(bytes));
			   byte[] toSendBytes = null;
			try {
				toSendBytes = Utils.serialize(toSend);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   byte[] toSendBytesCrypted = Security.DesEncrypt(modal.getSessionKey(), toSendBytes);
			   try {
				modal.getOutputStream().writeInt(toSendBytesCrypted.length);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   
			   
			   
			   try {
				modal.getOutputStream().write(toSendBytesCrypted);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   
			}
		});
		
		
		
		
	
		
		
		
		view.getReceiveFileButton().addActionListener(e->{
			HashMap<String, Byte[]> toSend = new HashMap<String, Byte[]>();
			   toSend.clear();
			   toSend.put("GETALL ", null);
			   byte[] toSendBytes = null;
				try {
					toSendBytes = Utils.serialize(toSend);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   byte[] toSendBytesCrypted = Security.DesEncrypt(modal.getSessionKey(), toSendBytes);
				   try {
					modal.getOutputStream().writeInt(toSendBytesCrypted.length);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   	      
				   try {
					modal.getOutputStream().write(toSendBytesCrypted);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   
				   
				   
				   
				   int available= 0;
				   try {
					available = modal.getInputStream().readInt();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   byte [] encryptedFilesBytes = new byte[available];
				   try {
					modal.getInputStream().readFully(encryptedFilesBytes);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   byte[] decryptedFilesBytes = Security.DesDecrypt(modal.getSessionKey(), encryptedFilesBytes);
				   ArrayList<String> paths =null ;
				   try {
					   paths = (ArrayList<String>) Utils.deserialize(decryptedFilesBytes);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   ReceiveFileModal rmodal = new ReceiveFileModal( modal.getInputStream(),modal.getOutputStream(), modal.getSessionKey());
				   ReceiveFileView  rview = new ReceiveFileView(paths);
				   ReceiveFileController rcontroller = new ReceiveFileController(rmodal, rview);
				   rcontroller.control();
				   
		});
		
	}
}
