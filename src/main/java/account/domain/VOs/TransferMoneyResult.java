package account.domain.VOs;

public class TransferMoneyResult {

    private TransferMoneyResultType type;

    public TransferMoneyResult(TransferMoneyResultType type) {
        this.type = type;
    }

    public boolean isSuccessful() {
        return type.equals(TransferMoneyResultType.SUCCESS);
    }
}
