package account.domain.VOs;

import java.util.Objects;

public class AccountId {
    private String value;

    public AccountId(String value) {
        this.value = value;
    }

    public static AccountId valueOf(String accountId) {
        return new AccountId(accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return value.equals(accountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
