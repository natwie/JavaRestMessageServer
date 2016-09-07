package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
	
	private int id;
	private String message;
	private String recipient;
	private String sender;
	private long timestamp;
	private boolean read;
	
	public Message() {}
		
	public Message(String message, String sender, String recipient) {
		this.message = message;
		this.recipient = recipient;
		this.sender = sender;
		this.read = false;
		this.timestamp = System.currentTimeMillis();
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	

}

