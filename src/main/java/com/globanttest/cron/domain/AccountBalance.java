package com.globanttest.cron.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accountBalances")
public class AccountBalance {

	@Id
	private String id;
	
	private String amount;
	private Long accountId;
	private String accountType;

	public AccountBalance(String amount, Long accountId,
			String accountType) {
		this.accountId = accountId;
		this.amount = amount;
		this.accountType = accountType;
	}

	public String getAmount() {
		return amount;
	}

	public Long getTransactionId() {
		return accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
}
