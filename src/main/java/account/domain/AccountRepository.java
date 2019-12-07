package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.TransactionEvents;
import account.domain.entities.TransactionEvent;

public interface AccountRepository {

    TransactionEvents findTransactionsById(AccountId accountId);

    void add(TransactionEvent transactionEvent);
}
