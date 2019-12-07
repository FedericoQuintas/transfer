package account.domain.VOs;

import java.math.BigDecimal;

public class TransactionEvent {

    private final TransactionType type;
    private final Amount amount;

    public TransactionEvent(TransactionType type, Amount amount) {
        this.type = type;
        this.amount = amount;
    }

    public Amount getAmount() {
        return amount;
    }

    public boolean isCredit() {
        return this.type.equals(TransactionType.CREDIT);
    }

    public BigDecimal calculateAmountForBalanceCalculation() {
        if (isCredit()) {
            return getAmount().asBigDecimal();
        }
        return getAmount().asBigDecimal().negate();
    }

    public boolean isDebit() {
        return this.type.equals(TransactionType.DEBIT);
    }
}
