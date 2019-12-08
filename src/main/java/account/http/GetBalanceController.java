package account.http;

import account.domain.VOs.AccountId;
import account.domain.services.GetAccountBalanceService;
import io.vertx.core.http.HttpServerResponse;


import java.util.concurrent.CompletableFuture;

public class GetBalanceController {
    private GetAccountBalanceService getAccountBalanceService;

    public GetBalanceController(GetAccountBalanceService getAccountBalanceService) {
        this.getAccountBalanceService = getAccountBalanceService;
    }

    public CompletableFuture<Void> getBalance(String accountId, HttpServerResponse response) {
        return getAccountBalanceService.get(AccountId.valueOf(accountId))
                .thenAccept(getBalanceResponse -> {
                    if(getBalanceResponse.isSuccessful()) {
                        response.setStatusCode(201);
                        response.setStatusMessage("tito");
                    } else {
                        response.setStatusMessage("Rolo");
                        response.setStatusCode(400);
                    }
                    response.end();
                });
    }
}
