package account.domain.VOs;

import account.domain.entities.TransactionEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionEvents {

    private List<TransactionEvent> events;

    public TransactionEvents() {
        this.events = new ArrayList<>();
    }

    public static TransactionEvents createEmpty() {
        return new TransactionEvents();
    }

    public void add(TransactionEvent transactionEvent) {
        this.events.add(transactionEvent);
    }

    public List<TransactionEvent> asList() {
        return this.events;
    }

    public boolean hasEnoughRunningBalanceFor(Amount amount) {
       return 0 < amount.asBigDecimal().compareTo(calculateRunningBalance(events)) ;
    }

    private static BigDecimal calculateRunningBalance(List<TransactionEvent> events) {
        return events.stream().filter(event-> event.isCredit()).map(event-> event.getAmount().asBigDecimal()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
