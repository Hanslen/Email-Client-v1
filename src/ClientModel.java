
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import emailConnector.InterfaceConnector;

public class ClientModel implements InterfaceClientModel {

	InterfaceConnector connector;
	ArrayList<String> foldersname = new ArrayList<String>();
	ArrayList<InterfaceFolder> folders = new ArrayList<InterfaceFolder>();
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
		folders.add(folderinbox);
		folders.add(foldersent);
	}

	@Override
	public boolean changeActiveFolder(String folderName) {
		// TODO Auto-generated method stub
		for(int i = 0; i < foldersname.size(); i++){
			if(foldersname.get(i).equals(folderName)){
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
					SimpleDateFormat sim=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
					folders.get(0).addMessage(temple);
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
		for(int i = 0; i < foldersname.size(); i ++){
			if(foldersname.get(i).equals(folderName)){
				return false;
			}
		}
		InterfaceFolder newfolder = new Folder();
		foldersname.add(folderName);
		folders.add(newfolder);
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
		int index = 0;
		for(int i = 0; i < foldersname.size(); i++){
			if(foldersname.get(i).equals(folderName)){
				index = i;
				break;
			}
		}
		return folders.get(index);
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
		int index = 0;
		Iterator<InterfaceMessage> itr = getFolder(getActiveFolderName()).getMessages().iterator();
		for(int i = 0; i < getFolder(getActiveFolderName()).getMessages().size();i++){
			if(itr.next().getId() == messageId){
				index = i;
			}
		}
		getFolder(getActiveFolderName()).getMessage(index).markRead(read);
		return false;
	}

	@Override
	public boolean move(int messageId, String destination) {
		// TODO Auto-generated method stub
		int index = -1;
		int current = 0;
		for(int i = 0; i < foldersname.size(); i++){
			if(foldersname.get(i).equals(destination)){
				index = i;
				break;
			}
		}
		for(int i = 0; i < foldersname.size(); i++){
			if(foldersname.get(i).equals(getActiveFolderName())){
				current = i;
				break;
			}
		}
		if(index == -1){
			return false;
		}
		folders.get(index).addMessage(folders.get(current).getMessage(messageId));
		folders.get(current).delete(messageId);		
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
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean sendMessage(InterfaceMessage msg) {
		String message;
		SimpleDateFormat sim=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dates = sim.format(msg.getDate());
		message = "To: "+msg.getRecipient()+"\r\n"+"From: "+msg.getFrom()+"\r\n"+"Date: "+dates+"\r\n"+"Subject: "+msg.getSubject()+"\r\n\r\n"+msg.getBody();
		String[] id = connector.sendMessage(message).split(" ");
		msg.setId(Integer.parseInt(id[1]));
		folders.get(1).addMessage(msg);
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
}
