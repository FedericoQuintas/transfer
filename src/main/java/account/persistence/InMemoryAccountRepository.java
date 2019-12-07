package account.persistence;

import account.domain.VOs.TransactionEvents;
import account.domain.VOs.AccountId;
import account.domain.AccountRepository;
import account.domain.entities.Account;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<AccountId, Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new HashMap<>();
    }

    @Override
    public void add(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    @Override
    public CompletableFuture<Account> findAccountById(AccountId accountId) {
        return CompletableFuture.completedFuture(accounts.get(accountId));
    }
}
