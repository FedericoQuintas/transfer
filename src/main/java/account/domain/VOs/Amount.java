package account.domain.VOs;

import java.math.BigDecimal;
import java.util.Objects;

public class Amount {

    private final BigDecimal value;

    public Amount(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public BigDecimal asBigDecimal() {
        return value;
    }
}
