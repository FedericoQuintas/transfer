package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.actions.TransferMoney;
import account.domain.entities.TransactionEvent;
import account.persistence.InMemoryAccountRepository;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransferMoneyTest {

    private AccountRepository accountRepository = new InMemoryAccountRepository();
    private AccountId fromAccountId = new AccountId(1L);
    private AccountId toAccountId = new AccountId(2L);
    private Amount amount = new Amount(BigDecimal.valueOf(1L));

    @Test
    public void transferenceCreatesOneDebitAndOneCreditTransactions(){

        new TransferMoney(accountRepository).transfer(fromAccountId, toAccountId, amount);

        TransactionEvent debitEvent = accountRepository.findTransactionsById(fromAccountId).get(0);
        TransactionEvent creditEvent = accountRepository.findTransactionsById(toAccountId).get(0);

        assertEquals(amount, debitEvent.getAmount());
        assertEquals(fromAccountId, debitEvent.getAccountId());
        assertEquals(amount, creditEvent.getAmount());
        assertEquals(toAccountId, creditEvent.getAccountId());

    }

    @Test
    public void afterTransferenceOnlyOneEventIsAddedToEachAccount(){

        new TransferMoney(accountRepository).transfer(fromAccountId, toAccountId, amount);

        List<TransactionEvent> senderAccountEvents = accountRepository.findTransactionsById(fromAccountId);
        List<TransactionEvent> receiverAccountEvents = accountRepository.findTransactionsById(toAccountId);

        assertEquals(1, senderAccountEvents.size());
        assertEquals(1, receiverAccountEvents.size());

    }

}
