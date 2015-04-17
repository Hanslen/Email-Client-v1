import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;


public class InterfaceCommand {
	InterfaceClientModel model;
	public InterfaceCommand(InterfaceClientModel model){
		this.model = model;
	}
	public String listfolders(){
		String output = "===Folders===\n";
		Iterator<String> itr = model.getFolderNames().iterator();
		while(itr.hasNext()){
			String element = itr.next();
			output += element + "\n";
		}
		return output;
	}
	public String receive(){
		if(model.checkForNewMessages()){
			return "Successfully updated";
		}
		return "Failed!";
	}
	
	public String list(){
		String output = "===Messages===\n";
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		Iterator<InterfaceMessage> itr = templefolder.getMessages().iterator();
		while(itr.hasNext()){
			InterfaceMessage gmessage = itr.next();
			String isRead;
			SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
			String dates = sim.format(gmessage.getDate());
			if(gmessage.isRead()){
				isRead = "R";
			}
			else{
				isRead = "U";
			}
			output += gmessage.getId()+": "+ isRead+": "+ dates + ":  "+gmessage.getSubject() + "\n";
		}
		return output;
	}
	public String show(int messageId){
		if(isvalidId(messageId)){
			InterfaceMessage templemessage = new Message();
			templemessage = model.getFolder(model.getActiveFolderName()).getMessage(messageId);
			model.mark(messageId, true);
			return toString(templemessage);
		}
			return "Error: Message does not exist";
	}
	public boolean isvalidId(int messageId){
		for(int i = 0; i < model.getFolder(model.getActiveFolderName()).getMessages().size();i++){
			if(model.getFolder(model.getActiveFolderName()).getMessage(i).getId() == messageId){
				return true;
			}
		}
		return false;
	}
	public void sortDate(){
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		templefolder.sortByDate(false);
	}
	public void sortSubject(){
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		templefolder.sortBySubject(true);
	}
	public String makefolder(String name){
		if(model.createFolder(name)){
			return "Success: Created folder "+name;
		}
			return "Error: Folder "+name+" exists already.";
	}
	public String move(int messageId, String destination){
		if(model.move(messageId, destination)){
			return "Success: Moved "+messageId+" to "+destination;
		}
		return "Move failed";
	}
	public String changefolder(String folderName){
		if(model.changeActiveFolder(folderName)){
			return "Success: Changed folder to "+folderName;
		}
		return "Error: Invalid arguments.";
	}
	public String delete(int messageId){
		if(model.delete(messageId)){
			return "Successfully deleted "+messageId;
		}
		return "Error: failed, could not delete";
	}
	public String toString(InterfaceMessage msg){
		SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
		String dates = sim.format(msg.getDate());
		return "To: "+msg.getRecipient()+"\r\n"+"From: "+msg.getFrom()+"\r\n"+"Date: "+dates+"\r\n"+"Subject: "+msg.getSubject()+"\r\n\r\n"+msg.getBody();
	}
	public String deleteFolder(String folderName){
		Iterator<String> itrn = model.getFolderNames().iterator();
		while(itrn.hasNext()){
			if(itrn.next().equals(folderName)){
				if(folderName.equals(model.getActiveFolderName())){
					model.changeActiveFolder("inbox");
				}
				model.getFolderNames().remove(folderName);
				Iterator<Map.Entry<String, InterfaceFolder>> it = model.getFolders().entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, InterfaceFolder> entry = it.next();
					if(entry.getKey().equals(folderName)){
						model.getFolders().remove(folderName);
						return "Success: delete folder "+folderName;
					}
				}
			}
		}
		return "Error: this folder do not exist";
	}
	public String markMessage(int messageId,boolean isRead){
		Iterator<InterfaceMessage> itr = model.getallmessage().iterator();
		while(itr.hasNext()){
			if(itr.next().getId() == messageId){
				InterfaceFolder temple = new Folder();
				temple = model.getFolder(model.getActiveFolderName());
				temple.getMessage(messageId).markRead(isRead);
				return "Successfully mark message";
			}
		}
		return "Error: Message does not exist";
	}
	
	public String rename(String oldname, String newname){
		if(model.renameFolder(oldname, newname)){
			return "Success: you have rename "+oldname+" to "+newname;
		}
		return "Error: This folder does not exist";
	}
}
