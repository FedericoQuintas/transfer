package account.persistence;

import account.domain.TransactionEvents;
import account.domain.VOs.AccountId;
import account.domain.AccountRepository;
import account.domain.entities.TransactionEvent;

import java.util.*;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<AccountId, TransactionEvents> eventsByAccount;

    public InMemoryAccountRepository() {
        this.eventsByAccount = new HashMap<>();
    }


    public TransactionEvents findTransactionsById(AccountId accountId) {
        return eventsByAccount.get(accountId);
    }

    @Override
    public void add(TransactionEvent transactionEvent) {
        eventsByAccount.computeIfAbsent(transactionEvent.getAccountId(),
                age-> new TransactionEvents()).add(transactionEvent);

    }
}