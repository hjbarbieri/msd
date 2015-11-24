package com.globanttest.cron.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.globanttest.cron.domain.AccountBalance;
import com.globanttest.domain.events.AccountEvent;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
@Service
@EnableScheduling
//Adapter converts external request and events into commands
public class AccountQueryImpl implements AccountQueryService {
	
	private final static String QUEUE_NAME = "account-transaction";
	private static List<AccountEvent> accountEvents = new ArrayList<>();
	@Autowired
	private MongoOperations mongoOperation;
	
	@Scheduled(cron="*/25 * * * * *")
	public void saveAccountsFromEvents(){
		try {
			mongoOperation.insert(mappingAccountBalance(),AccountBalance.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			accountEvents = null;
			accountEvents = new ArrayList<>();
		}
	}
	
	private AccountEvent ByteToevents(byte[] bytesEvent) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytesEvent);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  AccountEvent o = (AccountEvent) in.readObject(); 
		 return o;
		} finally {
		  try {
		    bis.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    if (in != null) {
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
	}
	
	private List<AccountBalance> mappingAccountBalance() throws IOException, TimeoutException{
		List<AccountEvent> allEvents =  getEventsFromQueue();
		List<AccountBalance> accounts = new ArrayList<>();
		
		for (AccountEvent accountEvent : allEvents) {
			AccountBalance account = new AccountBalance(accountEvent.getAmount().toString(),accountEvent.getAccountId(),accountEvent.getAccountType().toString());
			accounts.add(account);
			account = null;
		}
		
		return accounts;
	}

	private List<AccountEvent> getEventsFromQueue() throws IOException, TimeoutException{
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	    	  AccountEvent accountEvent = null;
			try {
				accountEvent = ByteToevents(body);
				accountEvents.add(accountEvent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println(" [x] Received '" + accountEvent.toString() + "'");
	      }
	    };
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	    return accountEvents;
	  }
	
		
	
	
}
