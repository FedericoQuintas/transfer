package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.TransactionEvents;
import account.domain.entities.Account;
import account.domain.VOs.TransactionEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface AccountRepository {

    void add(Account account);

    CompletableFuture<Account> findAccountById(AccountId fromAccountId);

    CompletableFuture<Map<AccountId, Account>> findAccountsById(List<AccountId> accountIds);
}
