package account.http;


import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.VOs.TransferMoneyResponse;
import account.domain.services.TransferMoneyService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class TransferMoneyController {
    private static final String CONTENT_TYPE = "content-type";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";
    private static final String FROM_ACCOUNT_ID = "from_account_id";
    private static final String TO_ACCOUNT_ID = "to_account_id";
    private static final String AMOUNT = "amount";
    private TransferMoneyService transferMoneyService;

    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    public CompletableFuture<Void> transfer(JsonObject bodyAsJson, HttpServerResponse response) {

        AccountId fromAccountId = convertToAccountId(bodyAsJson, FROM_ACCOUNT_ID);
        AccountId toAccountId = convertToAccountId(bodyAsJson, TO_ACCOUNT_ID);
        BigDecimal amount = BigDecimal.valueOf(bodyAsJson.getFloat(AMOUNT));

        return transferMoneyService.transfer(fromAccountId, toAccountId, Amount.valueOf(amount))
                .thenAccept(transferMoneyResponse -> setStatusCode(response, transferMoneyResponse));
    }

    private void setStatusCode(HttpServerResponse response, TransferMoneyResponse transferMoneyResponse) {
        response.putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);

        if (transferMoneyResponse.isSuccessful()) {
            response.setStatusCode(HttpResponseStatus.CREATED.code());
            response.end();
        } else {
            response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
            response.end();
        }
    }

    private AccountId convertToAccountId(JsonObject jsonObject, String key) {
        String serializedAccountId = jsonObject.getString(key);
        return AccountId.valueOf(serializedAccountId);
    }

}
