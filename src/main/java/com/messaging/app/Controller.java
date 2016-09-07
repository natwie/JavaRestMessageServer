package com.messaging.app;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entities.Message;

@RestController
public class Controller {

	DatabaseCommunicator dC;

	public void setDatabaseCommunicator(DatabaseCommunicator value) {
		this.dC = value;
	}

	/**
	 * @param message
	 * @param from
	 * @param to
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/message/create", method=RequestMethod.POST)
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
			dC.saveMessage(newMessage);
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

	@RequestMapping(value="/messages", method=RequestMethod.PUT)
	public List<Message> getAllMessages() {
		return dC.fetchAllMessages();      
	}

	@RequestMapping(value="/messages/unread", method=RequestMethod.PUT)
	public List<Message> getUnreadMessags() {
		return dC.fetchUnread();      

	}

	@RequestMapping(value="/messages/range/{start}/{end}/", method=RequestMethod.PUT)
	public List<Message> getAllMessagesInRange(@PathVariable int start, @PathVariable int end) {
		return dC.fetchMessagesInRange(start, end);      
	}

	@RequestMapping(value="/messages/delete", method=RequestMethod.DELETE)
	public String deleteAllMessages() {
		return dC.deleteAllMessages();      
	}

	@RequestMapping(value="/messages/delete/{id}/", method=RequestMethod.DELETE)
	public void deleteMessage(@PathVariable int id, HttpServletResponse response) {
		int result = dC.deleteMessage(id);
		if(result == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@RequestMapping(value="/messags/from/{from}/", method=RequestMethod.PUT)
	public List<Message> getAllMessagesFrom(@PathVariable String from) {
		return dC.fetchMessagesBySender(from);     
	}





}