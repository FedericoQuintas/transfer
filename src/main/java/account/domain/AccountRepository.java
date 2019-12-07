package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.TransactionEvents;
import account.domain.entities.Account;
import account.domain.VOs.TransactionEvent;

import java.util.concurrent.CompletableFuture;

public interface AccountRepository {

    void add(Account account);

    CompletableFuture<Account> findAccountById(AccountId fromAccountId);
}
