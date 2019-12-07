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
       return 0 >= amount.asBigDecimal().compareTo(calculateRunningBalance(events)) ;
    }

    private static BigDecimal calculateRunningBalance(List<TransactionEvent> events) {
        return events.stream().map(TransactionEvent::calculateAmountForBalanceCalculation).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Balance calculateRunningBalance() {
        return new Balance(calculateRunningBalance(this.events));
    }

}
