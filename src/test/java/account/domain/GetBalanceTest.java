package account.domain;

import account.domain.VOs.*;
import account.domain.services.GetAccountBalanceService;
import account.domain.entities.Account;
import account.domain.factories.AccountFactory;
import account.persistence.InMemoryAccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class GetBalanceTest {

    private AccountRepository accountRepository;
    private AccountId accountId = new AccountId(1L);
    private Amount amount = new Amount(BigDecimal.valueOf(1L));
    private GetAccountBalanceService getAccountBalanceService;

    @Before
    public void before(){
        accountRepository = new InMemoryAccountRepository();
        getAccountBalanceService = new GetAccountBalanceService(accountRepository);
    }

    @Test
    public void calculatesBalanceWithOnlyOneCreditEvent() throws ExecutionException, InterruptedException {

        addCreditEventToAccount(AccountFactory.create(accountId), amount);

        GetBalanceResult result = getAccountBalanceService.get(accountId).get();

        assertEquals(BigDecimal.ONE, result.getCurrentBalance().asBigDecimal());
        assertTrue(result.isSuccessful());
    }

    @Test
    public void calculatesBalanceWithOnlyOneCreditAndOneDebitEvents() throws ExecutionException, InterruptedException {

        Account account = AccountFactory.create(accountId);
        addCreditEventToAccount(account, amount);
        addDebitEventToAccount(account, amount);

        GetBalanceResult result = getAccountBalanceService.get(accountId).get();

        assertEquals(BigDecimal.ZERO, result.getCurrentBalance().asBigDecimal());
    }

    @Test
    public void returnsErrorIfAccountDoesNotExist() throws ExecutionException, InterruptedException {

        GetBalanceResult result = getAccountBalanceService.get(accountId).get();

        assertFalse(result.isSuccessful());

    }

    private void addCreditEventToAccount(Account account, Amount amount) {
        account.addCreditEvent(amount);
        accountRepository.add(account);
    }

    private void addDebitEventToAccount(Account account, Amount amount) {
        account.addDebitEvent(amount);
        accountRepository.add(account);
    }

}
