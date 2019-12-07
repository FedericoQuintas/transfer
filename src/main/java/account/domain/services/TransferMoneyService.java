package account.domain.services;

import account.domain.*;
import account.domain.VOs.*;

import java.util.concurrent.CompletableFuture;

public class TransferMoneyService {

    private AccountRepository accountRepository;

    public TransferMoneyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<TransferMoneyResult> transfer(AccountId fromAccountId, AccountId toAccountId, Amount amount) {

        return accountRepository.findAccountById(fromAccountId).thenApply(account -> {
            try {
                account.addDebitEvent(amount);
            } catch (NotEnoughCreditForOperationInvariant ex){
                return errorResponse();
            }
            accountRepository.findAccountById(toAccountId)
                    .thenAccept(receiverAccount -> receiverAccount.addCreditEvent(amount));
            return successfulResponse();
        });
    }

    private static TransferMoneyResult successfulResponse() {
        return new TransferMoneyResult(TransferMoneyResultType.SUCCESS);
    }

    private static TransferMoneyResult errorResponse() {
        return new TransferMoneyResult(TransferMoneyResultType.ERROR);
    }

}
