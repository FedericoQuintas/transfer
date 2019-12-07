package account.domain.actions;

import account.domain.*;
import account.domain.VOs.*;
import account.domain.entities.TransactionEvent;
import account.domain.factories.TransactionEventFactory;

import java.util.concurrent.CompletableFuture;

public class TransferMoney {

    private AccountRepository accountRepository;

    public TransferMoney(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<TransferMoneyResult> transfer(AccountId fromAccountId, AccountId toAccountId, Amount amount) {

            return accountRepository.findTransactionsById(fromAccountId).thenApply(transactionsById -> {
                if (!transactionsById.hasEnoughRunningBalanceFor(amount)) {
                    return errorResponse();
                }

                accountRepository.add(createTransactionEvent(fromAccountId, amount, TransactionType.DEBIT));
                accountRepository.add(createTransactionEvent(toAccountId, amount, TransactionType.CREDIT));
                return successfulResponse();
            });
    }

    private static TransferMoneyResult successfulResponse() {
        return new TransferMoneyResult(TransferMoneyResultType.SUCCESS);
    }

    private static TransferMoneyResult errorResponse() {
        return new TransferMoneyResult(TransferMoneyResultType.ERROR);
    }

    private TransactionEvent createTransactionEvent(AccountId accountId, Amount amount, TransactionType type) {
        return TransactionEventFactory.create(accountId, amount, type);
    }
}
