package com.messaging.app;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Service;

import entities.Message;

@Service
public class DatabaseCommunicator 
{
	private static SessionFactory sessionFactory = null;  
	private static ServiceRegistry serviceRegistry = null;  
	private Session session = null;
	private Transaction tx=null;
	
	public DatabaseCommunicator() {}
	
	public void init() {
		// Configure the session factory
		configureSessionFactory();    	
	}

	private static SessionFactory configureSessionFactory() throws HibernateException {  
		Configuration configuration = new Configuration();  
		configuration.addAnnotatedClass(entities.Message.class);
		configuration.configure();  

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();          
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);  

		return sessionFactory;  
	}

	


	public void saveMessage(Message message) {

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			// Saving to the database
			session.save(message);

			// Committing the change in the database.
			session.flush();
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();             
			// Rolling back the changes to make the data consistent in case of any failure 
			// in between multiple database write operations.
			tx.rollback();
		} finally{
			if(session != null) {
				session.close();
			}
		}
	}

	public List<Message> fetchAllMessages() {

		session = sessionFactory.openSession();
		List<Message> messages = session.createQuery("from Message").getResultList();
		tx = session.beginTransaction();
		for(Message message : messages) {
			if(!message.isRead()) {
				Query query = session
						.createQuery(" update Message set read = " + true + " where id= "
								+ message.getId());
				query.executeUpdate();
			}
		}

		session.flush();
		tx.commit();
		session.close();

		return messages;

	}

	public List<Message> fetchMessagesInRange(int start, int end) {

		session = sessionFactory.openSession();
		List<Message> messages = session.createQuery("from Message where id >=" +start + " and id <= " + end + " order by timestamp asc").getResultList();

		tx = session.beginTransaction();
		for(Message message : messages) {
			if(!message.isRead()) {
				Query query = session
						.createQuery(" update Message set read = " + true + " where id= "
								+ message.getId());
				query.executeUpdate();
			}
		}

		session.flush();
		tx.commit();
		session.close();

		return messages;

	}

	public List<Message> fetchUnread() {

		session = sessionFactory.openSession();
		List<Message> messages = session.createQuery("from Message where read = false").getResultList();

		tx = session.beginTransaction();
		for(Message message : messages) {
				Query query = session
						.createQuery(" update Message set read = " + true + " where id= "
								+ message.getId());
				query.executeUpdate();

		}

		session.flush();
		tx.commit();
		session.close();

		return messages;

	}

	public List<Message> fetchMessagesByRecipient(String recipient) {

		session = sessionFactory.openSession();
		List<Message> messages = session.createQuery("from Message where recipient = " + recipient.trim()).getResultList();

		session.flush();
		session.close();

		return messages;

	}

	public List<Message> fetchMessagesBySender(String sender) {

		session = sessionFactory.openSession();
		List<Message> messages = session.createQuery("from Message where sender = '" + sender.trim() + "'").getResultList();

		tx = session.beginTransaction();
		for(Message message : messages) {
			if(!message.isRead()) {
				Query query = session
						.createQuery(" update Message set read = " + true + " where id= "
								+ message.getId());
				query.executeUpdate();
			}

		}

		session.flush();
		tx.commit();
		session.close();

		return messages;
	}

	public int deleteMessage(int id) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int result = session.createQuery("delete from Message where id =" + id).executeUpdate();
		session.flush();
		tx.commit();
		session.close();
		return result;


	}

	public String deleteAllMessages() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		session.createQuery("delete from Message").executeUpdate();
		session.flush();
		tx.commit();
		session.close();
		return "All messages deleted";

	}

}
