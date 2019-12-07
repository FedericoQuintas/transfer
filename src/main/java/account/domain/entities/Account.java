package account.domain.entities;

import account.domain.NotEnoughCreditForOperationInvariant;
import account.domain.VOs.*;
import account.domain.factories.TransactionEventFactory;

public class Account {

    private final AccountId accountId;
    private final TransactionEvents transactionEvents;

    public Account(AccountId accountId) {
        this.accountId = accountId;
        this.transactionEvents = TransactionEvents.createEmpty();
    }

    public void addDebitEvent(Amount amount) {
        if (!transactionEvents.hasEnoughRunningBalanceFor(amount)) {
            throw new NotEnoughCreditForOperationInvariant();
        }
        this.transactionEvents.add(createTransactionEvent(amount, TransactionType.DEBIT));
    }

    private TransactionEvent createTransactionEvent(Amount amount, TransactionType type) {
        return TransactionEventFactory.create(amount, type);
    }

    public TransactionEvents getTransactionEvents() {
        return this.transactionEvents;
    }

    public Balance calculateRunningBalance() {
        return this.transactionEvents.calculateRunningBalance();
    }

    public void addCreditEvent(Amount amount) {
        this.transactionEvents.add(createTransactionEvent(amount, TransactionType.CREDIT));
    }

    public AccountId getAccountId() {
        return this.accountId;
    }
}
