import java.util.Collection;

/**
 * A model object is responsible for storing the programs state.
 * In this case, it should have some folders and should have
 * methods for interacting with them.
 * @author ISO
 *
 */
public interface InterfaceClientModel {


	/**
	 * 
	 * @param folderName the name of the folder to switch to.
	 * @return true if successful, false if not successful e.g if the folder does not exist.
	 */
	boolean changeActiveFolder(String folderName);

	/**
	 * Check for new messages.
	 * @return true if successful, false if could not connect. Note, true even if no new messages received.
	 */
	boolean checkForNewMessages();

	/**
	 * Create a new, empty folder.
	 * @param folderName, the name of the folder to create.
	 * @return true is successful, false if failed, e.g the folder name already exists.
	 */
	boolean createFolder(String folderName);

	/**
	 * Used to delete a message. Remember to call markForDeletion on the connector
	 * otherwise it will be received again next time calling sendrec!
	 * (The server needs to know this message is now deleted).
	 * Note - can only delete from current folder.
	 * @param messageId the message to delete.
	 * @return true if successful, false if failed (eg the message doesn't exist).
	 */
	boolean delete(int messageId);
	
	/**
	 * Gets the name of the current folder
	 * @return the name of the current folder.
	 */
	String getActiveFolderName();

	/**
	 * Gets the folder
	 * @param The name of the folder to get
	 * @return The folder
	 */
	InterfaceFolder getFolder(String folderName);
	
	/**
	 * Get a collection of the folder names available
	 * @return a collection holding the folder names.
	 */
	Collection<String> getFolderNames();

	/**
	 * Get a message by ID
	 * @param messageId the message ID
	 * @return the message.
	 */
	InterfaceMessage getMessage(int messageId);

	/**
	 * Retrieves a collection of messages in the current folder.
	 * @return a collection of messages in the current folder.
	 */
	Collection<InterfaceMessage> getMessages();

	/**
	 * Mark a message as read or unread.
	 * @param messageId the messaage ID to mark
	 * @param read (true if read, false if unread)
	 * @return true if successful (false if not, e.g message does not exist).
	 */
	boolean mark(int messageId, boolean read);

	/**
	 * Move a message from the current folder to another.
	 * @param messageId the message to move
	 * @param destination the name of the folder to move to.
	 * @return true if successful, false if not (e.g message or folder do not exist).
	 */
	boolean move(int messageId, String destination);

	/**
	 * Rename a folder, keeping it's current state and contents.
	 * @param oldName The old name of the folder
	 * @param newName The new name of the folder
	 * @return True if successful, false if failed (e.g folder doesn't exist, or new name already existed).
	 */
	boolean renameFolder(String oldName, String newName);

	/**
	 * Send a message.
	 * Hint: The response from the server contains the assigned ID!
	 * @param The message to send. 
	 * @return True if successful.
	 */
	boolean sendMessage(InterfaceMessage msg);

	/**
	 * Sort by date
	 * @param ascending
	 */
	void sortByDate(boolean ascending);

	/**
	 * Sort by subject.
	 * @param ascending
	 */
	void sortBySubject(boolean ascending);
}