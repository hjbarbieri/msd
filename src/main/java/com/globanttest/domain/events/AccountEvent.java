package com.globanttest.domain.events;


import java.io.Serializable;
import java.math.BigDecimal;

public class AccountEvent implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal amount;
	private Long accountId;
	private AccountEventType accountType;

	public AccountEvent(BigDecimal amount, Long accountId,
			AccountEventType accountType) {
		this.accountId = accountId;
		this.amount = amount;
		this.accountType = accountType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public AccountEventType getAccountType() {
		return accountType;
	}

	@Override
	public String toString() {
		return "AccountEvent [amount=" + amount + ", accountId=" + accountId
				+ ", accountType=" + accountType + "]";
	}
	
	

}
