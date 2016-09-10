package com.messaging.app;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entities.Message;

/**
 * @author natwie
 *
 */
@RestController
public class Controller {

	DatabaseCommunicator databaseCommunicator;

	public void setDatabaseCommunicator(DatabaseCommunicator value) {
		this.databaseCommunicator = value;
	}
	
	/**
	 * Creates a new message with basic validation
	 * 	 
	 * @param message
	 * @param from
	 * @param to
	 * @param response
	 * @return Message if successful, BAD_REQUST response if failed
	 */
	@RequestMapping(value="/api/v1.0/message/create/", method=RequestMethod.POST)
	public Message createMessage(@RequestParam(value="message") String message,
			@RequestParam(value="from") String from,
			@RequestParam(value="to") String to,
			HttpServletResponse response) {
		
		if(!validateMessageCreate(message, from, to)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		else {
			Message newMessage = new Message(message, from, to);
			databaseCommunicator.saveMessage(newMessage);
			response.setStatus(HttpServletResponse.SC_CREATED);
			return newMessage;   
			
		}		   
	}
	
	private boolean validateMessageCreate(String message, String from, String to) {
		if(message == null || message.isEmpty() || message.length() > 140) {
			return false;
			
		}
		
		if(to == null || to.isEmpty() || to.length() > 40) {
			return false;
		}
		
		if(from == null || from.isEmpty() || from.length() > 40) {
			return false;
		}
		
		return true;
	}

	/**
	 * Returns all messages and sets them as read
	 * 	  
	 * @return List<Message>
	 */
	@RequestMapping(value="/api/v1.0/messages/", method=RequestMethod.PUT)
	public List<Message> getAllMessages() {
		return databaseCommunicator.fetchAllMessages();      
	}

	/**
	 * Returns all unread messages and sets them as read
	 * 	  
	 * @return List<Message>
	 */
	@RequestMapping(value="/api/v1.0/messages/unread/", method=RequestMethod.PUT)
	public List<Message> getUnreadMessags() {
		return databaseCommunicator.fetchUnread();      

	}
	
	/**
	 * Returns all messages in range, according to time order
	 * 	
	 * @param start
	 * @param end
	 *   
	 * @return List<Message>
	 */
	@RequestMapping(value="/api/v1.0/messages/range/{start}/{end}/", method=RequestMethod.PUT)
	public List<Message> getAllMessagesInRange(@PathVariable int start, @PathVariable int end) {
		return databaseCommunicator.fetchMessagesInRange(start, end);      
	}

	/**
	 * Deletes all messages
	 *   
	 * @return void
	 */
	@RequestMapping(value="/api/v1.0/messages/delete/", method=RequestMethod.DELETE)
	public void deleteAllMessages() {
		databaseCommunicator.deleteAllMessages();      
	}
	
	/**
	 * Deletes a specific message
	 * 
	 * @param id
	 *   
	 * @return void
	 */
	@RequestMapping(value="/api/v1.0/messages/delete/{id}/", method=RequestMethod.DELETE)
	public void deleteMessage(@PathVariable int id, HttpServletResponse response) {
		int result = databaseCommunicator.deleteMessage(id);
		if(result == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	/**
	 * Deletes a specific range of messages
	 * 
	 * @param id
	 *   
	 * @return void
	 */
	@RequestMapping(value="/api/v1.0/messages/delete/inrange/{start}/{end}/", method=RequestMethod.DELETE)
	public void deleteMessagesInRange(@PathVariable int start, @PathVariable int end, HttpServletResponse response) {
		int result = databaseCommunicator.deleteMessagesInRange(start, end);
		if(result == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * List all messages from a specific sender
	 * 
	 * @param from
	 *   
	 * @return List<Message>
	 */
	@RequestMapping(value="/api/v1.0/messages/from/{from}/", method=RequestMethod.PUT)
	public List<Message> getAllMessagesFrom(@PathVariable String from) {
		return databaseCommunicator.fetchMessagesBySender(from);     
	}





}