package account.domain;

import account.domain.VOs.AccountId;
import account.domain.entities.TransactionEvent;

import java.util.List;

public interface AccountRepository {

    TransactionEvents findTransactionsById(AccountId accountId);

    void add(TransactionEvent transactionEvent);
}
