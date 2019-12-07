package account.domain;

import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.VOs.TransactionType;
import account.domain.VOs.TransferMoneyResult;
import account.domain.actions.TransferMoney;
import account.domain.entities.TransactionEvent;
import account.domain.factories.TransactionEventFactory;
import account.persistence.InMemoryAccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransferMoneyTest {

    private AccountRepository accountRepository;
    private AccountId fromAccountId = new AccountId(1L);
    private AccountId toAccountId = new AccountId(2L);
    private Amount amount = new Amount(BigDecimal.valueOf(1L));
    private TransferMoney transferMoney;

    @Before
    public void before(){
        accountRepository = new InMemoryAccountRepository();
        transferMoney = new TransferMoney(accountRepository);
    }

    @Test
    public void transferenceCreatesOneDebitAndOneCreditTransactions() throws ExecutionException, InterruptedException {

        accountRepository.add(TransactionEventFactory.create(fromAccountId, amount, TransactionType.CREDIT));

        TransferMoneyResult result = transferMoney.transfer(fromAccountId, toAccountId, amount).get();

        TransactionEvent debitEvent = accountRepository.findTransactionsById(fromAccountId).get().asList().get(0);
        TransactionEvent creditEvent = accountRepository.findTransactionsById(toAccountId).get().asList().get(0);

        assertEquals(amount, debitEvent.getAmount());
        assertEquals(fromAccountId, debitEvent.getAccountId());
        assertEquals(amount, creditEvent.getAmount());
        assertEquals(toAccountId, creditEvent.getAccountId());
        assertTrue(result.isSuccessful());

    }

    @Test
    public void afterTransferenceOnlyOneEventIsAddedToEachAccount() throws ExecutionException, InterruptedException {

        accountRepository.add(TransactionEventFactory.create(fromAccountId, amount, TransactionType.CREDIT));

        transferMoney.transfer(fromAccountId, toAccountId, amount);

        List<TransactionEvent> senderAccountEvents = accountRepository.findTransactionsById(fromAccountId).get().asList();
        List<TransactionEvent> receiverAccountEvents = accountRepository.findTransactionsById(toAccountId).get().asList();

        assertEquals(2, senderAccountEvents.size());
        assertEquals(1, receiverAccountEvents.size());

    }

    @Test
    public void cannotTransferMoreThanRunningBalance() throws ExecutionException, InterruptedException {
        TransferMoneyResult result = transferMoney.transfer(fromAccountId, toAccountId, amount).get();
        assertFalse(result.isSuccessful());
    }

    @Test
    public void eventsAreNotAddedWhenTransfersMoreThanRunningBalance() throws ExecutionException, InterruptedException {

        transferMoney.transfer(fromAccountId, toAccountId, amount);

        List<TransactionEvent> senderAccountEvents = accountRepository.findTransactionsById(fromAccountId).get().asList();
        List<TransactionEvent> receiverAccountEvents = accountRepository.findTransactionsById(toAccountId).get().asList();
        assertEquals(0, senderAccountEvents.size());
        assertEquals(0, receiverAccountEvents.size());
    }

    @Test
    public void onlyCreditEventsCountForBalance() throws ExecutionException, InterruptedException {
        accountRepository.add(TransactionEventFactory.create(fromAccountId, amount, TransactionType.DEBIT));

        TransferMoneyResult transferMoneyResult = transferMoney.transfer(fromAccountId, toAccountId, amount).get();

        assertFalse(transferMoneyResult.isSuccessful());
    }


}
