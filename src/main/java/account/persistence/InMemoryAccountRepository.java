package account.persistence;

import account.domain.VOs.AccountId;
import account.domain.AccountRepository;
import account.domain.entities.Account;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<AccountId, Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    @Override
    public CompletableFuture<Account> findAccountById(AccountId accountId) {
        return CompletableFuture.completedFuture(accounts.get(accountId));
    }

    @Override
    public CompletableFuture<Map<AccountId, Account>> findAccountsById(List<AccountId> accountIds) {
        Map<AccountId, Account> requiredAccounts = accountIds.parallelStream()
                .filter(accounts::containsKey)
                .collect(Collectors.toMap(Function.identity(), accounts::get));
        return CompletableFuture.completedFuture(requiredAccounts);
    }
}
