
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import emailConnector.InterfaceConnector;

public class ClientModel implements InterfaceClientModel {

	InterfaceConnector connector;
	ArrayList<String> foldersname = new ArrayList<String>();
	//ArrayList<InterfaceFolder> folders = new ArrayList<InterfaceFolder>();
	Map<String, InterfaceFolder> foldersa = new HashMap<String, InterfaceFolder>();
	ArrayList<InterfaceMessage> allmessage = new ArrayList<InterfaceMessage>();
	String currentfoldername = "inbox";
	InterfaceFolder folderinbox;
	InterfaceFolder foldersent;
	
	public ClientModel(InterfaceConnector connector) {
		// Do not add or edit the arguments for this constructor
		this.connector = connector;
		foldersname.add("inbox");
		foldersname.add("sent");
		folderinbox = new Folder();
		foldersent = new Folder();
		foldersa.put("inbox",folderinbox);
		foldersa.put("sent", foldersent);
	}

	@Override
	public boolean changeActiveFolder(String folderName) {
		// TODO Auto-generated method stub
		Iterator<Map.Entry<String, InterfaceFolder>> it = foldersa.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, InterfaceFolder> entry = it.next();
			if(entry.getKey().equals(folderName)){
				this.currentfoldername = folderName;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkForNewMessages() {
		// TODO Auto-generated method stub
		try{
			String[] messageId = connector.listMessages().split("\r\n");
			for(int i = 0; i < messageId.length; i++){
				int reduce = 0;
				int have = 0;
				Iterator<InterfaceMessage> itr = getFolder("sent").getMessages().iterator();
				for(int j = 0; j < getFolder("sent").getMessages().size(); j++){
					if(itr.next().getId() < (Integer.parseInt(messageId[i]))){
						reduce++;
					}
				}
				String[] getnewMessage = connector.retrMessage(Integer.parseInt(messageId[i])-reduce).split("\r\n");
				for(int out = 0; out < allmessage.size(); out++){
					if(allmessage.get(out).getId() == Integer.parseInt(messageId[i])){
						have = 1;
					}
				}
				if(have == 0){
					InterfaceMessage temple = new Message();
					temple.setId(Integer.parseInt(messageId[i]));
					String[] forRecipient = getnewMessage[0].split(": ");
					temple.setRecipient(forRecipient[1]);
					String[] forFrom = getnewMessage[1].split(": ");
					temple.setFrom(forFrom[1]);
					String[] fordate = getnewMessage[2].split(": ");
					SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
					String dates = fordate[1];
					Date d = null;
					try{
						d=sim.parse(dates);
					}catch(ParseException e){
					}
					temple.setDate(d);
					String[] forSubject = getnewMessage[3].split(": ");
					temple.setSubject(forSubject[1]);
					temple.setBody(getnewMessage[5]);
					foldersa.get("inbox").addMessage(temple);
					allmessage.add(temple);
				}
			}
			return true;
		}catch(IOException e){
			return false;
		}
	}

	@Override
	public boolean createFolder(String folderName) {
		// TODO Auto-generated method stub
		Iterator<Map.Entry<String, InterfaceFolder>> it = foldersa.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, InterfaceFolder> entry = it.next();
			if(entry.getKey().equals(folderName)){
				return false;
			}
		}
		InterfaceFolder newfolder = new Folder();
		foldersname.add(folderName);
		foldersa.put(folderName, newfolder);
		return true;
	}

	@Override
	public boolean delete(int messageId) {
		// TODO Auto-generated method stub
		connector.markMessageForDeleting(messageId);
		return getFolder(getActiveFolderName()).delete(messageId);
	}

	@Override
	public String getActiveFolderName() {
		// TODO Auto-generated method stub
		return currentfoldername;
	}

	@Override
	public InterfaceFolder getFolder(String folderName) {
		// TODO Auto-generated method stub
		try{
			return foldersa.get(folderName);
		}catch(Exception e){
			return foldersa.get(this.currentfoldername);
		}
	}

	@Override
	public Collection<String> getFolderNames() {
		// TODO Auto-generated method stub
		return foldersname;
	}

	@Override
	public InterfaceMessage getMessage(int messageId) {
		// TODO Auto-generated method stub
		return getFolder(getActiveFolderName()).getMessage(messageId);
	}

	@Override
	public Collection<InterfaceMessage> getMessages() {
		// TODO Auto-generated method stub
		return getFolder(getActiveFolderName()).getMessages();
	}

	@Override
	public boolean mark(int messageId, boolean read) {
		// TODO Auto-generated method stub
		Iterator<InterfaceMessage> itr = getFolder(getActiveFolderName()).getMessages().iterator();
		for(int i = 0; i < getFolder(getActiveFolderName()).getMessages().size();i++){
			if(itr.next().getId() == messageId){
				getFolder(getActiveFolderName()).getMessage(i).markRead(read);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean move(int messageId, String destination) {
		// TODO Auto-generated method stub
//		try{
//			foldersa.get(destination).addMessage(foldersa.get(getActiveFolderName()).getMessage(messageId));
//			foldersa.get(getActiveFolderName()).delete(messageId);		
//			return true;
//		}catch(Exception e){
//			return false;
//		}
		Iterator<Map.Entry<String, InterfaceFolder>> it = foldersa.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, InterfaceFolder> entry = it.next();
			if(entry.getKey().equals(destination)){
				Iterator<InterfaceMessage> itr = getFolder(getActiveFolderName()).getMessages().iterator();
				for(int i = 0; i < getFolder(getActiveFolderName()).getMessages().size();i++){
					if(itr.next().getId() == messageId){
						foldersa.get(destination).addMessage(foldersa.get(getActiveFolderName()).getMessage(messageId));
						foldersa.get(getActiveFolderName()).delete(messageId);	
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean renameFolder(String oldName, String newName) {
		// TODO Auto-generated method stub
		if(oldName.equals("inbox")||oldName.equals("sent")){
			return false;
		}
		for(int i = 0; i < foldersname.size(); i++){
			if(foldersname.get(i).equals(oldName)){
				foldersname.remove(oldName);
				foldersname.add(i, newName);
			}
		}
		Iterator<Map.Entry<String, InterfaceFolder>> it = foldersa.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, InterfaceFolder> entry = it.next();
			if(entry.getKey().equals(oldName)){
				foldersa.put(newName, foldersa.get(oldName));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean sendMessage(InterfaceMessage msg) {
		String message;
		SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
		String dates = sim.format(msg.getDate());
		message = "To: "+msg.getRecipient()+"\r\n"+"From: "+msg.getFrom()+"\r\n"+"Date: "+dates+"\r\n"+"Subject: "+msg.getSubject()+"\r\n\r\n"+msg.getBody();
		String[] id = connector.sendMessage(message).split(" ");
		msg.setId(Integer.parseInt(id[1]));
		foldersa.get("sent").addMessage(msg);
		allmessage.add(msg);
		return true;
	}
	@Override
	public void sortByDate(boolean ascending) {
		// TODO Auto-generated method stub
		getFolder(getActiveFolderName()).sortByDate(ascending);
	}

	@Override
	public void sortBySubject(boolean ascending) {
		// TODO Auto-generated method stub
		getFolder(getActiveFolderName()).sortBySubject(ascending);
	}

	@Override
	public Map<String, InterfaceFolder> getFolders() {
		// TODO Auto-generated method stub
		return foldersa;
	}

	@Override
	public ArrayList<InterfaceMessage> getallmessage() {
		// TODO Auto-generated method stub
		return allmessage;
	}
	
}
