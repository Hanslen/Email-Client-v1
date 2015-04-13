import java.util.Collection;

public interface InterfaceFolder {

	/**
	 * Add a new message
	 * @param message to add to the folder
	 */
	public void addMessage(InterfaceMessage message);

	/**
	 * Get a message by ID
	 * @param messageId
	 * @return the message
	 */
	public InterfaceMessage getMessage(int messageId);

	/**
	 * Get the collection of messages this folder holds.
	 * @return the messages.
	 */
	public Collection<InterfaceMessage> getMessages();

	/**
	 * Check if the folder is empty
	 * @return true if the folder contains no messages, else false.
	 */
	public boolean isEmpty();

	/**
	 * Sort this folder by date
	 * @param ascending
	 */
	public void sortByDate(boolean ascending);
	
	/**
	 * Sort this folder by subject.
	 * @param ascending
	 */
	public void sortBySubject(boolean ascending);

	/**
	 * Delete a message with the provided ID
	 * @param messageId
	 * @return true if successful, false if the message does not exist.
	 */
	public boolean delete(int messageId);
}