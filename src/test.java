import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class test {
	InterfaceConnector connector;
	InterfaceClientModel model;
	View textBasedView = new View(model);
	String body1;
	String subject1;
	String from1;
	String to1;
	Date day1;
	String body2;
	String subject2;
	String from2;
	String to2;
	Date day2;
	InterfaceMessage testMessage1 = new Message();
	InterfaceMessage testMessage2 = new Message();
	
	@Before
	public void setUp() {
		connector = StandardConnector.getInstance();
		model = new ClientModel(connector);
		body1 = "This is the test message body1";
		subject1 = "Test message subject1";
		from1 = "you@you.com";
		to1 = "me@me.com";
		day1 = new Date();
		day1.getTime();
		testMessage1.setId(100);
		testMessage1.setFrom(from1);
		testMessage1.setRecipient(to1);
		testMessage1.setSubject(subject1);
		testMessage1.setDate(day1);
		testMessage1.setBody(body1);
		
		body2 = "This is the test message body2";
		subject2 = "Test message subject2";
		from2 = "me@me.com";
		to2 = "you@you.com";
		
		testMessage2.setId(101);
		testMessage2.setFrom(from2);
		testMessage2.setRecipient(to2);
		testMessage2.setSubject(subject2);
		testMessage2.setDate(day2);
		testMessage2.setBody(body2);
	}

	@Test
	public void test() {
		assertTrue(model.checkForNewMessages());
	}
	
	@Test
	public void testMessage(){
		assertEquals(100, testMessage1.getId());
		assertNotEquals(101,testMessage1.getId());
		assertEquals(from1,testMessage1.getFrom());
		assertNotEquals(from1+"1",testMessage1.getFrom());
		assertEquals(to1,testMessage1.getRecipient());
		assertNotEquals(to1+"1",testMessage1.getRecipient());
		assertEquals(subject1,testMessage1.getSubject());
		assertNotEquals(subject1+"1",testMessage1.getSubject());
		assertEquals(day1,testMessage1.getDate());
		assertEquals(body1,testMessage1.getBody());
		assertNotEquals(body1+"1",testMessage1.getBody());
	}
	
	@Test
	public void testFolder1(){
		InterfaceFolder testfolder = new Folder();
		InterfaceFolder testemptyfolder = new Folder();
		
		testfolder.addMessage(testMessage1);
		testfolder.addMessage(testMessage2);
		
		List<InterfaceMessage> testmessages = new ArrayList<InterfaceMessage>();
		testmessages.add(testMessage1);
		testmessages.add(testMessage2);
		
		assertEquals(testMessage1.getId(),testfolder.getMessage(100).getId());
		assertEquals(testMessage1.getFrom(),testfolder.getMessage(100).getFrom());
		assertEquals(testMessage1.getRecipient(),testfolder.getMessage(100).getRecipient());
		assertEquals(testMessage1.getDate(),testfolder.getMessage(100).getDate());
		assertEquals(testMessage1.getSubject(),testfolder.getMessage(100).getSubject());
		assertEquals(testMessage1.getBody(),testfolder.getMessage(100).getBody());
		assertNotEquals(testMessage2.getBody(),testfolder.getMessage(100).getBody());
		
		Iterator<InterfaceMessage> itr = testfolder.getMessages().iterator();
		assertEquals(testmessages.get(0).getId(), itr.next().getId());
		assertFalse(testfolder.isEmpty());
		assertTrue(testemptyfolder.isEmpty());
		assertFalse(testfolder.delete(10));
		assertTrue(testfolder.delete(100));
		assertTrue(testfolder.delete(101));
		assertTrue(testfolder.isEmpty());
	}
	@Test
	public void testClientModel(){
		assertTrue(model.changeActiveFolder("sent"));
		assertFalse(model.changeActiveFolder("favourite"));
		assertTrue(model.changeActiveFolder("inbox"));
		
		assertTrue(model.createFolder("favourite"));
		assertFalse(model.createFolder("inbox"));
		assertFalse(model.createFolder("sent"));
		assertFalse(model.createFolder("favourite"));
		
		model.getFolder("inbox").addMessage(testMessage1);
		
		assertEquals("inbox",model.getActiveFolderName());
		assertNotEquals("sent",model.getActiveFolderName());
		model.changeActiveFolder("sent");
		assertEquals("sent",model.getActiveFolderName());
		model.changeActiveFolder("inbox");

		InterfaceFolder templesent = new Folder();
		
		assertNotEquals(templesent,model.getFolder("inbox"));	
		assertEquals(templesent.isEmpty(), model.getFolder("sent").isEmpty());
		
		ArrayList<String> foldersname = new ArrayList<String>();
		foldersname.add("inbox");
		foldersname.add("sent");
		foldersname.add("favourite");
		assertEquals(foldersname,model.getFolderNames());
		
		model.getFolder("inbox").addMessage(testMessage1);
		assertEquals(testMessage1.getBody(), model.getMessage(100).getBody());
		assertEquals(testMessage1.getSubject(), model.getMessage(100).getSubject());

		Iterator<InterfaceMessage> itr = model.getMessages().iterator();
		assertEquals(testMessage1.getBody(),itr.next().getBody());
		
		assertFalse(model.mark(102, true));
		assertTrue(model.mark(100, true));
		
		assertTrue(model.move(100, "sent"));
		model.changeActiveFolder("sent");
		//assertTrue(model.move(101, "inbox"));
		model.changeActiveFolder("inbox");
		assertFalse(model.move(100,"testfolder"));
		assertFalse(model.move(19, "sent"));
		
		assertTrue(model.renameFolder("favourite", "test"));
		assertFalse(model.renameFolder("inbox", "newinbox"));
		assertFalse(model.renameFolder("sent", "newsent"));
		
		InterfaceMessage sent = new Message();
		sent.setId(3);
		sent.setDate(day1);
		sent.setFrom("you@you.com");
		sent.setRecipient("me@me.com");
		sent.setSubject("This is a sent message");
		sent.setBody("This is the sent message's body");
		
		assertTrue(model.sendMessage(sent));
	}
	
	@Test
	public void testfolder1(){
		model = new ClientModel(connector);
		model.checkForNewMessages();
		model.sortByDate(false);
		Iterator<InterfaceMessage> itr = model.getMessages().iterator();
		assertEquals(7,itr.next().getId());
		assertEquals(5,itr.next().getId());
		assertEquals(2,itr.next().getId());
		assertEquals(1,itr.next().getId());
		assertEquals(0,itr.next().getId());
		assertEquals(6,itr.next().getId());
		assertEquals(4,itr.next().getId());
		assertEquals(3,itr.next().getId());
		
	}
	

}
