import java.util.Date;


public class Message implements InterfaceMessage {
	private String body;
	private String from;
	private Date date;
	private String recipient;
	private String subject;
	private boolean isread;
	private int providedId;
	
	public Message() {
		// do not edit the arugments passed into this constructor.
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return this.body;
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return this.date;
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return this.from;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return providedId;
	}

	@Override
	public String getRecipient() {
		// TODO Auto-generated method stub
		return this.recipient;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return this.subject;
	}

	@Override
	public boolean isRead() {
		// TODO Auto-generated method stub
		return isread;
	}

	@Override
	public void markRead(boolean read) {
		// TODO Auto-generated method stub
		this.isread = read;
	}

	@Override
	public void setBody(String body) {
		// TODO Auto-generated method stub
		this.body = body;
	}

	@Override
	public void setDate(Date date) {
		// TODO Auto-generated method stub
		this.date = date;
	}

	@Override
	public void setFrom(String from) {
		// TODO Auto-generated method stub
		this.from = from;
	}

	@Override
	public void setId(int providedId) {
		// TODO Auto-generated method stub
		this.providedId = providedId;
	}

	@Override
	public void setRecipient(String recipient) {
		// TODO Auto-generated method stub
		this.recipient = recipient;
	}

	@Override
	public void setSubject(String subject) {
		// TODO Auto-generated method stub
		this.subject = subject;
	}

	
}
