package account.http;

import account.domain.VOs.AccountId;
import account.domain.VOs.GetBalanceResponse;
import account.domain.services.GetAccountBalanceService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CompletableFuture;

public class GetBalanceController {
    public static final String BALANCE = "balance";
    public static final String CONTENT_TYPE = "content-type";
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";
    private GetAccountBalanceService getAccountBalanceService;

    public GetBalanceController(GetAccountBalanceService getAccountBalanceService) {
        this.getAccountBalanceService = getAccountBalanceService;
    }

    public CompletableFuture<Void> getBalance(String accountId, HttpServerResponse response) {
        return getAccountBalanceService.get(AccountId.valueOf(accountId))
                .thenAccept(getBalanceResponse -> {
                    if (getBalanceResponse.isSuccessful()) {
                        successfulResponse(response, getBalanceResponse);
                    } else {
                        errorResponse(response);
                    }
                });

    }

    private static void errorResponse(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
        response.end();
    }

    private static void successfulResponse(HttpServerResponse response, GetBalanceResponse getBalanceResponse) {
        response.putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatusCode(HttpResponseStatus.CREATED.code());
        JsonObject object = new JsonObject();
        object.put(BALANCE, runningBalance(getBalanceResponse));
        response.end(Json.encodePrettily(object));
    }

    private static String runningBalance(GetBalanceResponse getBalanceResponse) {
        return getBalanceResponse.getCurrentBalance().asBigDecimal().toString();
    }
}
