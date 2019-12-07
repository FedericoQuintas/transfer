package account.domain.actions;

import account.domain.*;
import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.VOs.TransactionType;
import account.domain.entities.TransactionEvent;

public class TransferMoney {

    private AccountRepository accountRepository;

    public TransferMoney(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(AccountId fromAccountId, AccountId toAccountId, Amount amount) {
        accountRepository.add(new TransactionEvent(TransactionType.DEBIT, amount, fromAccountId));
        accountRepository.add(new TransactionEvent(TransactionType.CREDIT, amount, toAccountId));

    }
}
