package account.domain.services;

import account.domain.AccountRepository;
import account.domain.VOs.AccountId;
import account.domain.VOs.GetBalanceResponse;
import account.domain.entities.Account;

import java.util.concurrent.CompletableFuture;

public class GetAccountBalanceService {

    private final AccountRepository accountRepository;

    public GetAccountBalanceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<GetBalanceResponse> get(AccountId accountId) {
        return accountRepository.findAccountById(accountId)
                .thenApply(account ->{
                        if(account == null) return errorResponse();
                        return successfulResponse(account);
                });
    }

    private GetBalanceResponse successfulResponse(Account account) {
        return GetBalanceResponse.createSuccessful(account.calculateRunningBalance());
    }

    private GetBalanceResponse errorResponse() {
        return GetBalanceResponse.createError();
    }
}
