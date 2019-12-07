package account.domain.VOs;

public class GetBalanceResult {

    private Balance balance;
    private GetBalanceResultType type;

    private GetBalanceResult(Balance balance, GetBalanceResultType type) {
        this.balance = balance;
        this.type = type;
    }

    private GetBalanceResult(GetBalanceResultType type) {
        this.type = type;
    }

    public static GetBalanceResult createSuccessful(Balance runningBalance) {
        return new GetBalanceResult(runningBalance, GetBalanceResultType.SUCCESS);
    }

    public static GetBalanceResult createError() {
        return new GetBalanceResult(GetBalanceResultType.ERROR);
    }

    public Balance getCurrentBalance() {
        return this.balance;
    }

    public boolean isSuccessful() {
        return this.type.equals(GetBalanceResultType.SUCCESS);
    }
}
