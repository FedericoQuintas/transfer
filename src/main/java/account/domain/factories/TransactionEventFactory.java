package account.domain.factories;

import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.VOs.TransactionType;
import account.domain.entities.TransactionEvent;

public class TransactionEventFactory {
    public static TransactionEvent create(AccountId accountId, Amount amount, TransactionType type) {
        return new TransactionEvent(type, amount, accountId);
    }
}
