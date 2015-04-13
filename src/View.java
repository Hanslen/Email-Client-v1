
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import emailConnector.Connector;

public class View {

	private InterfaceClientModel model;
	private static final String PROMPT = "?> ";
	private Scanner input;
	private int correctcommand = 0;

	public View(InterfaceClientModel model) {
		this.model = model;
		input = new Scanner(System.in);
	}

	public void start() {
		//InterfaceCommand command;
		String userCommand = "";
		System.out.println("===== AE1ISO Mail Client ====");
		
		// your code here?
		userCommand = getUserInput();
		// and here?
		while(!userCommand.equals("quit")){
			switch(userCommand){
			case "listfolders": listfolders();correctcommand = 1;break;
			case "receive":receive();correctcommand = 1;break;
			case "list":list();correctcommand = 1;break;
			case "compose":createMessage();correctcommand = 1; break;
			}
			String[] scommand = userCommand.split(" ");
			if(scommand[0].equals("view")){
				if(scommand.length > 1){
					show(Integer.parseInt(scommand[1]));
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("sort")){
				if(scommand.length > 1){
					if(scommand[1].equals("-d")){
						sortDate();
					}
					else{
						sortSubject();
					}
					System.out.println("Success: Messages sorted.");
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("mkf")){
				if(scommand.length > 1){
					String name = scommand[1];
					makefolder(name);
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("move")){
				if(scommand.length > 1){
					int messageId = Integer.parseInt(scommand[1]);
					String foldername = scommand[2];
					move(messageId,foldername);
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("cf")){
				if(scommand.length > 1){
					changefolder(scommand[1]);
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("delete")){
				if(scommand.length > 1){
					delete(Integer.parseInt(scommand[1]));
					correctcommand = 1;
				}
			}
			if(correctcommand == 0){
				System.out.println("Error: Not a valid command.");
			}
			userCommand = getUserInput();
			correctcommand = 0;
		}
		System.out.println("Quitting...");
	}

	private String getUserInput() {
		System.out.print(PROMPT);
		return input.nextLine();
	}
	
	private void listfolders(){
		System.out.println("===Folders===");
		Iterator<String> itr = model.getFolderNames().iterator();
		while(itr.hasNext()){
			String element = itr.next();
			System.out.println(element);
		}
	}
	
	private void receive(){
		if(model.checkForNewMessages()){
			System.out.println("Successfully updated");
		}
		else{
			System.out.println("Failed!");
		}
	}
	
	private void list(){
		System.out.println("===Messages===");
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		Iterator<InterfaceMessage> itr = templefolder.getMessages().iterator();
		while(itr.hasNext()){
			InterfaceMessage gmessage = itr.next();
			String isRead;
			SimpleDateFormat sim=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dates = sim.format(gmessage.getDate());
			if(gmessage.isRead()){
				isRead = "R";
			}
			else{
				isRead = "U";
			}
			System.out.println(gmessage.getId()+": "+ isRead+": "+ dates + ":  "+gmessage.getSubject());
		}
	}
	private void show(int messageId){
		if(isvalidId(messageId)){
			InterfaceMessage templemessage = new Message();
			templemessage = model.getFolder(model.getActiveFolderName()).getMessage(messageId);
			System.out.println("To:  "+ templemessage.getRecipient());
			System.out.println("From :  " + templemessage.getFrom());
			SimpleDateFormat sim=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dates = sim.format(templemessage.getDate());
			System.out.println("Date: "+dates);
			System.out.println("Subject:  "+ templemessage.getSubject());
			System.out.println("\n"+templemessage.getBody());
			model.mark(messageId, true);
		}
		else{
			System.out.println("Error: Message does not exist");
		}
	}
	private void sortDate(){
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		templefolder.sortByDate(false);
	}
	private void sortSubject(){
		InterfaceFolder templefolder = new Folder();
		templefolder = model.getFolder(model.getActiveFolderName());
		templefolder.sortBySubject(true);
	}
	private void makefolder(String name){
		if(model.createFolder(name)){
			System.out.println("Success: Created folder TestFolder");
		}
		else{
			System.out.println("Error: Folder TestFolder exists already.");
		}
	}
	private void move(int messageId, String destination){
		if(model.move(messageId, destination)){
			System.out.println("Success: Moved "+messageId+" to "+destination);
		}
	}
	private void changefolder(String folderName){
		if(model.changeActiveFolder(folderName)){
			System.out.println("Success: Changed folder to "+folderName);
		}
		else{
			System.out.println("Error: Invalid arguments.");
		}
	}
	private void createMessage(){
		System.out.print("To: ");
		String to = input.nextLine();
		System.out.print("From: ");
		String from = input.nextLine();
		System.out.print("Subject: ");
		String subject = input.nextLine();
		System.out.print("Body: ");
		String body = input.nextLine();
		InterfaceMessage newmess = new Message();
		newmess.setRecipient(to);
		newmess.setFrom(from);
		newmess.setSubject(subject);
		newmess.setBody(body);
		Date day = new Date();
		day.getTime();
		SimpleDateFormat sim=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String dates = sim.format(day);
		Date d = null;
		try{
			d=sim.parse(dates);
		}catch(ParseException e){
		}
		newmess.setDate(d);
		int id = totalMessagenum();
		newmess.setId(id);
		newmess.markRead(false);
		if(model.sendMessage(newmess)){
			System.out.println("Success: sent");
		}
		else{
			System.out.println("Error: failed, could not sent");
		}
	}
	private int totalMessagenum(){
		int total = 0;
		Iterator<String> itr = model.getFolderNames().iterator();
		for(int i = 0; i < model.getFolderNames().size(); i++){
			total += model.getFolder(itr.next()).getMessages().size();
		}
		return total;
	}
	private void delete(int messageId){
		if(model.delete(messageId)){
			System.out.println("Successfully deleted "+messageId);
		}
		else{
			System.out.println("Error: failed, could not delete");
		}
	}
	private boolean isvalidId(int messageId){
		for(int i = 0; i < model.getFolder(model.getActiveFolderName()).getMessages().size();i++){
			if(model.getFolder(model.getActiveFolderName()).getMessage(i).getId() == messageId){
				return true;
			}
		}
		return false;
	}
}
