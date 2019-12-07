package account.domain;

import account.domain.entities.TransactionEvent;

import java.util.ArrayList;
import java.util.List;

public class TransactionEvents {

    private List<TransactionEvent> events;

    public TransactionEvents() {
        this.events = new ArrayList<>();
    }

    public void add(TransactionEvent transactionEvent) {
        this.events.add(transactionEvent);
    }

    public List<TransactionEvent> asList() {
        return this.events;
    }
}
