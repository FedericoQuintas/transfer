package account.domain.VOs;

import java.util.Objects;

public class AccountId {
    private long value;

    public AccountId(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return value == accountId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
