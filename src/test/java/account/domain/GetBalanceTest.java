package account.domain;

import account.domain.VOs.*;
import account.domain.actions.GetAccountBalance;
import account.domain.factories.TransactionEventFactory;
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
    private GetAccountBalance getAccountBalance;

    @Before
    public void before(){
        accountRepository = new InMemoryAccountRepository();
        getAccountBalance = new GetAccountBalance(accountRepository);
    }

    @Test
    public void calculatesBalanceWithOnlyOneCreditEvent() throws ExecutionException, InterruptedException {

        addEventToRepository(accountId, TransactionType.CREDIT, amount);

        GetBalanceResult result = getAccountBalance.get(accountId).get();

        assertEquals(BigDecimal.ONE, result.getCurrentBalance().asBigDecimal());
    }

    @Test
    public void calculatesBalanceWithOnlyOneCreditAndOneDebitEvents() throws ExecutionException, InterruptedException {

        addEventToRepository(accountId, TransactionType.CREDIT, amount);
        addEventToRepository(accountId, TransactionType.DEBIT, amount);

        GetBalanceResult result = getAccountBalance.get(accountId).get();

        assertEquals(BigDecimal.ZERO, result.getCurrentBalance().asBigDecimal());
    }

    @Test
    public void returnsZeroIfAccountDoesNotExist() throws ExecutionException, InterruptedException {

        GetBalanceResult result = getAccountBalance.get(accountId).get();

        assertEquals(BigDecimal.ZERO, result.getCurrentBalance().asBigDecimal());

    }

    private void addEventToRepository(AccountId fromAccountId, TransactionType type, Amount amount) {
        accountRepository.add(TransactionEventFactory.create(fromAccountId, amount, type));
    }

}
