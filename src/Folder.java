import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Folder implements InterfaceFolder {

	List<InterfaceMessage> messages = new ArrayList<InterfaceMessage>();
	public Folder(){
		// do not edit the arguments passed in to this constructor.
	}

	@Override
	public void addMessage(InterfaceMessage message) {
		// TODO Auto-generated method stub
		InterfaceMessage temple = new Message();
		temple.setBody(message.getBody());
		temple.setDate(message.getDate());
		temple.setFrom(message.getFrom());
		temple.setId(message.getId());
		temple.setRecipient(message.getRecipient());
		temple.setSubject(message.getSubject());
		messages.add(temple);
	}

	@Override
	public InterfaceMessage getMessage(int messageId) {
		// TODO Auto-generated method stub
		int index = 0;
		for(int i = 0; i < messages.size();i++){
			if(messages.get(i).getId() == messageId){
				index = i;
			}
		}
		return messages.get(index);
	}

	@Override
	public Collection<InterfaceMessage> getMessages() {
		// TODO Auto-generated method stub
		return messages;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (messages.size()==0);
	}

	@Override
	public void sortByDate(boolean ascending) {
		// TODO Auto-generated method stub
		if(!ascending){
		Collections.sort((List<InterfaceMessage>) messages, new Comparator<Object>()
		{
			public int compare(Object o1, Object o2){
				return ((InterfaceMessage) o2).getDate().compareTo(((InterfaceMessage) o1).getDate());
			}
		});	
		}
		else{
			Collections.sort((List<InterfaceMessage>) messages, new Comparator<Object>()
			{
				public int compare(Object o1, Object o2){
					return ((InterfaceMessage) o1).getDate().compareTo(((InterfaceMessage) o2).getDate());
				}
			});
		}
	}

	@Override
	public void sortBySubject(boolean ascending) {
		// TODO Auto-generated method stub
		if(ascending){
			Collections.sort((List<InterfaceMessage>) messages, new Comparator<Object>()
			{
				public int compare(Object o1, Object o2){
					return ((InterfaceMessage) o1).getSubject().compareTo(((InterfaceMessage) o2).getSubject());
				}
			});	
		}
		else{
			Collections.sort((List<InterfaceMessage>) messages, new Comparator<Object>()
			{
				public int compare(Object o1, Object o2){
					return ((InterfaceMessage) o1).getSubject().compareTo(((InterfaceMessage) o2).getSubject());
				}
			});
		}
	}

	@Override
	public boolean delete(int messageId) {
		// TODO Auto-generated method stub
		int index = 0;
		for(int i = 0; i < messages.size();i++){
			if(messages.get(i).getId() == messageId){
				index = i;
			}
		}
		return messages.remove(getMessage(index));
		
	}

}
