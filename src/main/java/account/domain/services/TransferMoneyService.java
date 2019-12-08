package account.domain.services;

import account.domain.*;
import account.domain.VOs.*;
import account.domain.entities.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TransferMoneyService {

    private AccountRepository accountRepository;

    public TransferMoneyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<TransferMoneyResponse> transfer(AccountId fromAccountId, AccountId toAccountId, Amount amount) {

        List<AccountId> accountIds = Arrays.asList(fromAccountId, toAccountId);

        return accountRepository.findAccountsById(accountIds)
                .thenApply(accountsById -> {

            if (anyOfBothAccountsDoesNotExist(fromAccountId, toAccountId, accountsById)) return errorResponse();

            try {
                accountsById.get(fromAccountId).addDebitEvent(amount);
            } catch (NotEnoughCreditForOperationInvariant ex){
                return errorResponse();
            }

            accountsById.get(toAccountId).addCreditEvent(amount);
            return successfulResponse();
        });
    }

    private static boolean anyOfBothAccountsDoesNotExist(AccountId fromAccountId, AccountId toAccountId, Map<AccountId, Account> accountIds) {
        return !accountIds.containsKey(fromAccountId) || !accountIds.containsKey(toAccountId);
    }

    private static TransferMoneyResponse successfulResponse() {
        return TransferMoneyResponse.createSuccessful();
    }

    private static TransferMoneyResponse errorResponse() {
        return TransferMoneyResponse.createError();
    }

}
