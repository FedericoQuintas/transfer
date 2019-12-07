package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.TransactionEvents;
import account.domain.entities.TransactionEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface AccountRepository {

    CompletableFuture<TransactionEvents> findTransactionsById(AccountId accountId);

    void add(TransactionEvent transactionEvent);
}
