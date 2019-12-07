package account.domain.entities;

import account.domain.VOs.AccountId;
import account.domain.VOs.TransactionType;
import account.domain.VOs.Amount;

public class TransactionEvent {

    private final TransactionType type;
    private final Amount amount;
    private AccountId accountId;

    public TransactionEvent(TransactionType type, Amount amount, AccountId accountId) {
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
    }

    public Amount getAmount() {
        return amount;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
