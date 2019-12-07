package account.domain;

import account.domain.VOs.AccountId;
import account.domain.entities.TransactionEvent;

import java.util.List;

public interface AccountRepository {
    List<TransactionEvent> findTransactionsById(AccountId accountId);

    void add(TransactionEvent transactionEvent);
}
