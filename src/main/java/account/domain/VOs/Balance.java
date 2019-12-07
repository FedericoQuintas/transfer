package account.domain.VOs;

import java.math.BigDecimal;

public class Balance {
    private final BigDecimal value;

    public Balance(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal asBigDecimal() {
        return value;
    }
}
