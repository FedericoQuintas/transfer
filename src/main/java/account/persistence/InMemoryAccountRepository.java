package account.persistence;

import account.domain.VOs.TransactionEvents;
import account.domain.VOs.AccountId;
import account.domain.AccountRepository;
import account.domain.entities.TransactionEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<AccountId, TransactionEvents> eventsByAccount;

    public InMemoryAccountRepository() {
        this.eventsByAccount = new HashMap<>();
    }


    public CompletableFuture<TransactionEvents> findTransactionsById(AccountId accountId) {
        return CompletableFuture.completedFuture(eventsByAccount.getOrDefault(accountId, TransactionEvents.createEmpty()));
    }

    @Override
    public void add(TransactionEvent transactionEvent) {
        eventsByAccount.computeIfAbsent(transactionEvent.getAccountId(),
                age-> new TransactionEvents()).add(transactionEvent);

    }
}
