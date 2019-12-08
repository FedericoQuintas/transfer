package account.domain.VOs;

public class TransferMoneyResponse {

    private TransferMoneyResponseType type;

    private TransferMoneyResponse(TransferMoneyResponseType type) {
        this.type = type;
    }

    public static TransferMoneyResponse createSuccessful() {
        return new TransferMoneyResponse(TransferMoneyResponseType.SUCCESS);
    }

    public static TransferMoneyResponse createError() {
        return new TransferMoneyResponse(TransferMoneyResponseType.ERROR);
    }


    public boolean isSuccessful() {
        return type.equals(TransferMoneyResponseType.SUCCESS);
    }
}
