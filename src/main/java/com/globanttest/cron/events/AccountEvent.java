package com.globanttest.cron.events;

import java.math.BigDecimal;

public class AccountEvent {

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

}
