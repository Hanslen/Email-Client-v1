
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
		InterfaceCommand command = new InterfaceCommand(model);
		String userCommand = "";
		System.out.println("===== AE1ISO Mail Client ====");
		
		// your code here?
		userCommand = getUserInput();
		// and here?
		while(!userCommand.equals("quit")){
			String[] scommand = userCommand.split(" ");
			switch(userCommand){
				case "listfolders": System.out.print(command.listfolders());correctcommand = 1;break;
				case "receive":System.out.println(command.receive());correctcommand = 1;break;
				case "list":System.out.print(command.list());correctcommand = 1;break;
				case "compose":createMessage();correctcommand = 1; break;
				case "sort -d":command.sortDate();System.out.println("Success: Messages sorted.");correctcommand = 1;break;
				case "sort -s":command.sortSubject();System.out.println("Success: Messages sorted.");correctcommand = 1;break;
			}
			if(scommand.length == 2 && checkIsInteger(scommand[1])){
				switch(scommand[0]){
					case "view" :System.out.println(command.show(Integer.parseInt(scommand[1])));correctcommand = 1;break;
					case "reply":reply(Integer.parseInt(scommand[1]));correctcommand = 1;break;
					case "delete":System.out.println(command.delete(Integer.parseInt(scommand[1])));correctcommand = 1;break;
				}
			}
			if(scommand[0].equals("mkf")&&scommand.length == 2){
				String name = scommand[1];
				System.out.println(command.makefolder(name));
				correctcommand = 1;
			}
			else if(scommand[0].equals("move")&&scommand.length == 3){
				if(checkIsInteger(scommand[1])){
					int messageId = Integer.parseInt(scommand[1]);
					String foldername = scommand[2];
					System.out.println(command.move(messageId,foldername));
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("cf")&&scommand.length == 2){
				System.out.println(command.changefolder(scommand[1]));
				correctcommand = 1;
			}
			else if(scommand[0].equals("delete")){
				if(scommand[1].equals("-r")&&scommand.length == 3){
					if(scommand.length == 3){
						System.out.println(command.deleteFolder(scommand[2]));
						correctcommand = 1;
					}
				}
			}
			else if(scommand[0].equals("mark")&&scommand.length == 3){					
				if(scommand[1].equals("-r")){
					System.out.println(command.markMessage(Integer.parseInt(scommand[2]), true));
					correctcommand = 1;
				}
				else if(scommand[1].equals("-u")){
					System.out.println(command.markMessage(Integer.parseInt(scommand[2]),false));
					correctcommand = 1;
				}
			}
			else if(scommand[0].equals("rename")&&scommand.length == 3){
				System.out.println(command.rename(scommand[1],scommand[2]));
				correctcommand = 1;
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
	
	
	
	private void createMessage(){
		System.out.print("To: ");
		String to = input.nextLine();
		while(to.equals("")){
			System.out.println("Error: please type who you want to sent!");
			System.out.print("To: ");
			to = input.nextLine();
		}
		System.out.print("From: ");
		String from = input.nextLine();
		while(from.equals("")){
			System.out.println("Error: please type you email address!");
			System.out.print("From: ");
			from = input.nextLine();
		}
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
		SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
		String dates = sim.format(day);
		Date d = null;
		try{
			d=sim.parse(dates);
		}catch(ParseException e){
		}
		newmess.setDate(d);
		newmess.markRead(true);
		if(model.sendMessage(newmess)){
			System.out.println("Success: sent");
		}
		else{
			System.out.println("Error: failed, could not sent");
		}
	}
	public void reply(int messageId){
		int has = 0;
		Iterator<InterfaceMessage> itr = model.getallmessage().iterator();
		while(itr.hasNext()){
			if(itr.next().getId() == messageId){
				InterfaceFolder temple = new Folder();
				temple = model.getFolder(model.getActiveFolderName());
				System.out.println("To: "+temple.getMessage(messageId).getFrom());
				System.out.print("From: ");
				String from = input.nextLine();
				System.out.println("Subject: RE: "+temple.getMessage(messageId).getSubject());
				System.out.print("Body: ");
				String body = input.nextLine();
				InterfaceMessage newmess = new Message();
				newmess.setRecipient(temple.getMessage(messageId).getFrom());
				newmess.setFrom(from);
				newmess.setSubject("RE: "+temple.getMessage(messageId).getSubject());
				newmess.setBody(body);
				Date day = new Date();
				day.getTime();
				SimpleDateFormat sim=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
				String dates = sim.format(day);
				Date d = null;
				try{
					d=sim.parse(dates);
				}catch(ParseException e){
				}
				newmess.setDate(d);
				newmess.markRead(true);
				if(model.sendMessage(newmess)){
					System.out.println("Success: sent");
				}
				else{
					System.out.println("Error: failed, could not sent");
				}
				has = 1;
				break;
			}
		}
		if(has == 0){
			System.out.println("Error: Message does not exist");
		}
	}
	private boolean checkIsInteger(String messageId){
		char integerArray[];
		integerArray = messageId.toCharArray();
		for(int counter = 0; counter < integerArray.length;counter++){
			if(!Character.isDigit(integerArray[counter])){
				return false;
			}
		}
		return true;
	}
	
}
