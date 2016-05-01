package client.receiveFile_interface;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

import encryption.Security;
import encryption.Utils;

public class ReceiveFileController {
	private ReceiveFileModal modal;
	private ReceiveFileView view ;
	
	
	public ReceiveFileController(ReceiveFileModal modal, ReceiveFileView view) {
		this.modal = modal;
		this.view = view;
	}
	
	public void control(){
		
		view.getFileButton().addActionListener(e->{
			
			//*********************************Trying To  get The File ****************************
			String selectedFile = (String) view.getComboBox().getSelectedItem();
			System.out.println("Client Want : "+selectedFile);
			HashMap<String, Byte[]> toSend = new HashMap<String, Byte[]>();
			   toSend.clear();
			   toSend.put("get "+selectedFile, null);
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
				   
				   
				   //**************************Getting The File *************************************************
				   
				   
					int available = 0;
					try {
						available = modal.getInputStream().readInt();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					byte[] textCrypted = new byte [available];
					try {
						modal.getInputStream().readFully(textCrypted, 0, available);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					byte[] decripted = Security.DesDecrypt(modal.getSessionKey(), textCrypted);
			    	HashMap<String, Byte[]> request = null;
					try {
						request = (HashMap<String, Byte[]>) Utils.deserialize(decripted);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	Set <String> key = request.keySet();
			    	Iterator<String> keyIterator = key.iterator();
			    	String requestTitle = (String) keyIterator.next();
			    	
			    	if(requestTitle.contains("send")){
			    		String fileName = requestTitle.substring(5);
			    		Path p = Paths.get(fileName);
			    		String file = p.getFileName().toString();
			    		byte[] receivedFile = ArrayUtils.toPrimitive(request.get(requestTitle));
			    		try {
							Utils.writeFile2("/filesInClient/"+file, receivedFile);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    		JOptionPane.showMessageDialog(null, "The File"+fileName+" Was received successfuly", "Server Success", JOptionPane.INFORMATION_MESSAGE);
			    	
			    	}
				   
				   
				   
				   
				   
				   
				   
		});
		
	}

}
