package com.globanttest.cron.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.globanttest.cron.domain.AccountBalance;
import com.globanttest.cron.events.AccountEvent;
import com.globanttest.cron.events.AccountEventType;

@Service
@EnableScheduling
//Adapter converts external request and events into commands
public class AccountServiceImpl implements AccountQueryService {
	
	@Autowired
	private MongoOperations mongoOperation;
	
	@Scheduled(cron="*/25 * * * * *")
	public void saveAccountsFromEvents(){
		mongoOperation.insert(mappingAccountBalance(),AccountBalance.class);
	}
	
	private List<AccountBalance> mappingAccountBalance(){
		List<AccountEvent> allEvents =  getEventsFromQueue();
		List<AccountBalance> accounts = new ArrayList<>();
		
		for (AccountEvent accountEvent : allEvents) {
			AccountBalance account = new AccountBalance(accountEvent.getAmount().toString(),accountEvent.getAccountId(),accountEvent.getAccountType().toString());
			accounts.add(account);
			account = null;
		}
		
		return accounts;
	}

	private List<AccountEvent> getEventsFromQueue(){
		AccountEvent ac = new AccountEvent(new BigDecimal("12"), 1L, AccountEventType.OPEN);
		AccountEvent ac2 = new AccountEvent(new BigDecimal("11"), 1L, AccountEventType.CREDIT);
		AccountEvent ac3 = new AccountEvent(new BigDecimal("1"), 2L, AccountEventType.OPEN);
		
		
		List<AccountEvent> accounts = new ArrayList<>();
		accounts.add(ac);
		accounts.add(ac2);
		accounts.add(ac3);
		
		return accounts;
		
	}
	
	
}
