package account.http;


import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.services.TransferMoneyService;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class TransferMoneyController {

    private TransferMoneyService transferMoneyService;

    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }


    public CompletableFuture<Void> transfer(JsonObject bodyAsJson, HttpServerResponse response) throws ParseException {

        AccountId fromAccountId = convertToAccountId(bodyAsJson, "from_account_id");
        AccountId toAccountId = convertToAccountId(bodyAsJson, "to_account_id");
        BigDecimal amount = BigDecimal.valueOf(bodyAsJson.getFloat("amount"));

        return transferMoneyService.transfer(fromAccountId, toAccountId, Amount.valueOf(amount))
                .thenAccept(getBalanceResponse -> {
                    if(getBalanceResponse.isSuccessful()) {
                        response.setStatusCode(201);
                        response.setStatusMessage("Jorge");
                    }
                    response.setStatusCode(400);
                    response.setStatusMessage("Pepe");
                });
    }

    private AccountId convertToAccountId(JsonObject jsonObject, String key) {
        String serializedAccountId = jsonObject.getString(key);
        return AccountId.valueOf(serializedAccountId);
    }

}
