import static org.junit.Assert.*;

import org.junit.Test;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class test {

	@Test
	public void test() {
		InterfaceConnector connector = StandardConnector.getInstance();
		// You may use the "wiki connector" for testing or messing with if you wish.
		// Uncomment the line below, and comment out the previous connector.
	    //InterfaceConnector connector = WikiConnector.getInstance();
		InterfaceClientModel model = new ClientModel(connector);
		View textBasedView = new View(model);

		
		assertTrue(model.changeActiveFolder("inbox"));
		assertTrue(model.changeActiveFolder("sent"));
		assertFalse(model.changeActiveFolder("Inbo"));
		
		assertTrue(model.createFolder("favourite"));
		assertFalse(model.createFolder("inbox"));
		assertFalse(model.createFolder("sent"));
		
		assertFalse(model.renameFolder("inbox", "newinbox"));
		assertFalse(model.renameFolder("newfolder", "favourite"));
	}

}
