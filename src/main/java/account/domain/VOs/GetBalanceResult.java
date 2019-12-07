package account.domain.VOs;

public class GetBalanceResult {

    private Balance balance;

    private GetBalanceResult(Balance balance) {
        this.balance = balance;
    }

    public static GetBalanceResult create(Balance runningBalance) {
        return new GetBalanceResult(runningBalance);
    }

    public Balance getCurrentBalance() {
        return this.balance;
    }

}
