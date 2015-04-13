import java.util.Date;

public interface InterfaceMessage {

	/**
	 * Get the body of the message
	 * @return the body as a string
	 */
	public String getBody();

	/**
	 * Get the message date.
	 * @return the date
	 */
	public Date getDate();

	/**
	 * Get the senders address.
	 * @return the senders address
	 */
	public String getFrom();

	/**
	 * Get the ID assigned by the connector for this message
	 * @return the ID
	 */
	public int getId();

	/**
	 * Get the recipient
	 * @return the recipient
	 */
	public String getRecipient();

	/**
	 * Get the subject
	 * @return the subject
	 */
	public String getSubject();

	/**
	 * Check if this message has been read.
	 * @return true if it has been read, false if not
	 */
	public boolean isRead();
	
	/**
	 * Mark this message as read or unread
	 * @param read true if it has been read, false if not
	 */
	public void markRead(boolean read);
	
	/**
	 * Set the body of this message.
	 * @param body
	 */
	public void setBody(String body);
	
	/**
	 * Set the date of this message.
	 * @param date
	 */
	public void setDate(Date date);
	
	/**
	 * Set the senders address
	 * @param from
	 */
	public void setFrom(String from);
	
	/**
	 * Set the ID of this message (Note, this is assigned by the connector!)
	 * @param providedId
	 */
	public void setId(int providedId);
	
	/**
	 * Set the recipient
	 * @param recipient
	 */
	public void setRecipient(String recipient);
	
	/**
	 * Set the subject.
	 * @param subject
	 */
	public void setSubject(String subject);
	


}
