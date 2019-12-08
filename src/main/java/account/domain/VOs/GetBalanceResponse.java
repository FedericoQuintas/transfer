package account.domain.VOs;

public class GetBalanceResponse {

    private Balance balance;
    private GetBalanceResponseType type;

    private GetBalanceResponse(Balance balance, GetBalanceResponseType type) {
        this.balance = balance;
        this.type = type;
    }

    private GetBalanceResponse(GetBalanceResponseType type) {
        this.type = type;
    }

    public static GetBalanceResponse createSuccessful(Balance runningBalance) {
        return new GetBalanceResponse(runningBalance, GetBalanceResponseType.SUCCESS);
    }

    public static GetBalanceResponse createError() {
        return new GetBalanceResponse(GetBalanceResponseType.ERROR);
    }

    public Balance getCurrentBalance() {
        return this.balance;
    }

    public boolean isSuccessful() {
        return this.type.equals(GetBalanceResponseType.SUCCESS);
    }
}
