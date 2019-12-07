package account.domain.services;

import account.domain.AccountRepository;
import account.domain.VOs.AccountId;
import account.domain.VOs.GetBalanceResult;
import account.domain.entities.Account;

import java.util.concurrent.CompletableFuture;

public class GetAccountBalanceService {

    private final AccountRepository accountRepository;

    public GetAccountBalanceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<GetBalanceResult> get(AccountId accountId) {
        return accountRepository.findAccountById(accountId)
                .thenApply(account ->{
                        if(account == null) return errorResponse();
                        return successfulResponse(account);
                });
    }

    private GetBalanceResult successfulResponse(Account account) {
        return GetBalanceResult.createSuccessful(account.calculateRunningBalance());
    }

    private GetBalanceResult errorResponse() {
        return GetBalanceResult.createError();
    }
}
