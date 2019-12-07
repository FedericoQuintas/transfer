package account.domain.factories;

import account.domain.VOs.AccountId;
import account.domain.entities.Account;

public class AccountFactory {
    public static Account create(AccountId accountId) {
        return new Account(accountId);
    }
}
