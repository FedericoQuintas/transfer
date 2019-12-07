package account.domain.VOs;

public class TransferMoneyResult {

    private TransferMoneyResultType type;

    private TransferMoneyResult(TransferMoneyResultType type) {
        this.type = type;
    }

    public static TransferMoneyResult createSuccessful() {
        return new TransferMoneyResult(TransferMoneyResultType.SUCCESS);
    }

    public static TransferMoneyResult createError() {
        return new TransferMoneyResult(TransferMoneyResultType.ERROR);
    }


    public boolean isSuccessful() {
        return type.equals(TransferMoneyResultType.SUCCESS);
    }
}
