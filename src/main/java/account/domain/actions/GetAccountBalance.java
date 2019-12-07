package account.domain.actions;

import account.domain.AccountRepository;
import account.domain.VOs.AccountId;
import account.domain.VOs.GetBalanceResult;

import java.util.concurrent.CompletableFuture;

public class GetAccountBalance{

    private final AccountRepository accountRepository;

    public GetAccountBalance(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<GetBalanceResult> get(AccountId accountId) {
        return accountRepository.findTransactionsById(accountId)
                .thenApply(transactionEvents ->
                        GetBalanceResult.create(transactionEvents.calculateRunningBalance()));
    }
}
