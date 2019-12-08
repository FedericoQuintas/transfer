package account.domain;

import account.domain.VOs.*;
import account.domain.services.TransferMoneyService;
import account.domain.entities.Account;
import account.domain.factories.AccountFactory;
import account.persistence.InMemoryAccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class TransferMoneyServiceTest {

    private AccountRepository accountRepository;
    private AccountId fromAccountId = new AccountId("1");
    private AccountId toAccountId = new AccountId("2");
    private Amount amount = new Amount(BigDecimal.valueOf(1L));
    private TransferMoneyService transferMoneyService;
    private Account senderAccount;
    private Account receiverAccount;

    @Before
    public void before(){
        accountRepository = new InMemoryAccountRepository();
        senderAccount = AccountFactory.create(fromAccountId);
        receiverAccount = AccountFactory.create(toAccountId);
        accountRepository.add(senderAccount);
        accountRepository.add(receiverAccount);
        transferMoneyService = new TransferMoneyService(accountRepository);
    }

    @Test
    public void transferenceCreatesOneDebitAndOneCreditTransactions() throws ExecutionException, InterruptedException {

        senderAccount.addCreditEvent(amount);

        TransferMoneyResponse result = transferMoneyService.transfer(fromAccountId, toAccountId, amount).get();

        TransactionEvent debitEvent = accountRepository.findAccountById(fromAccountId).get().getTransactionEvents().asList().get(1);
        TransactionEvent creditEvent = accountRepository.findAccountById(toAccountId).get().getTransactionEvents().asList().get(0);

        assertEquals(amount, debitEvent.getAmount());
        assertTrue(debitEvent.isDebit());
        assertFalse(debitEvent.isCredit());

        assertEquals(amount, creditEvent.getAmount());
        assertTrue(creditEvent.isCredit());
        assertFalse(creditEvent.isDebit());
        assertTrue(result.isSuccessful());

    }

    @Test
    public void afterTransferenceOnlyOneEventIsAddedToEachAccount() throws ExecutionException, InterruptedException {

        senderAccount.addCreditEvent(amount);

        transferMoneyService.transfer(fromAccountId, toAccountId, amount);

        List<TransactionEvent> senderAccountEvents = accountRepository.findAccountById(fromAccountId).get().getTransactionEvents().asList();
        List<TransactionEvent> receiverAccountEvents = accountRepository.findAccountById(toAccountId).get().getTransactionEvents().asList();

        assertEquals(2, senderAccountEvents.size());
        assertEquals(1, receiverAccountEvents.size());

    }

    @Test
    public void cannotTransferMoreThanRunningBalance() throws ExecutionException, InterruptedException {
        TransferMoneyResponse result = transferMoneyService.transfer(fromAccountId, toAccountId, amount).get();
        assertFalse(result.isSuccessful());
    }

    @Test
    public void eventsAreNotAddedWhenTransfersMoreThanRunningBalance() throws ExecutionException, InterruptedException {

        transferMoneyService.transfer(fromAccountId, toAccountId, amount);

        List<TransactionEvent> senderAccountEvents = accountRepository.findAccountById(fromAccountId).get().getTransactionEvents().asList();
        List<TransactionEvent> receiverAccountEvents = accountRepository.findAccountById(toAccountId).get().getTransactionEvents().asList();

        assertEquals(0, senderAccountEvents.size());
        assertEquals(0, receiverAccountEvents.size());
    }

    @Test
    public void onlyCreditEventsCountForBalance() throws ExecutionException, InterruptedException {

        senderAccount.addCreditEvent(amount);
        senderAccount.addDebitEvent(amount);

        TransferMoneyResponse transferMoneyResult = transferMoneyService.transfer(fromAccountId, toAccountId, amount).get();

        assertFalse(transferMoneyResult.isSuccessful());
    }

    @Test
    public void returnsErrorIfSenderAccountDoesNotExist() throws ExecutionException, InterruptedException {

        accountRepository = new InMemoryAccountRepository();
        receiverAccount = AccountFactory.create(toAccountId);
        accountRepository.add(receiverAccount);
        transferMoneyService = new TransferMoneyService(accountRepository);

        TransferMoneyResponse transferMoneyResult = transferMoneyService.transfer(fromAccountId, toAccountId, amount).get();

        assertFalse(transferMoneyResult.isSuccessful());
    }

    @Test
    public void returnsErrorIfReceiverAccountDoesNotExist() throws ExecutionException, InterruptedException {

        accountRepository = new InMemoryAccountRepository();
        senderAccount = AccountFactory.create(fromAccountId);
        senderAccount.addCreditEvent(amount);
        accountRepository.add(senderAccount);

        transferMoneyService = new TransferMoneyService(accountRepository);

        TransferMoneyResponse transferMoneyResult = transferMoneyService.transfer(fromAccountId, toAccountId, amount).get();

        assertFalse(transferMoneyResult.isSuccessful());
    }


}
