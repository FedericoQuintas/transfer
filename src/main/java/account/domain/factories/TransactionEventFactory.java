package account.domain.factories;

import account.domain.VOs.Amount;
import account.domain.VOs.TransactionType;
import account.domain.VOs.TransactionEvent;

public class TransactionEventFactory {

    public static TransactionEvent create(Amount amount, TransactionType type) {
        return new TransactionEvent(type, amount);
    }
}
