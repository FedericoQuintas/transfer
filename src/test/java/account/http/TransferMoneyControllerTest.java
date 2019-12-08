package account.http;

import account.domain.VOs.AccountId;
import account.domain.VOs.Balance;
import account.domain.VOs.GetBalanceResponse;
import account.domain.services.GetAccountBalanceService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GetBalanceControllerTest {


    public static final String BALANCE = "balance";
    private static GetAccountBalanceService getBalanceMock;
    private GetBalanceController getBalanceController;

    @Before
    public void before(){
        getBalanceController = setUpController();
    }

    @Test
    public void controllerSetsRightStatusCodeWhenBalanceRetrievesSuccess() {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(getBalanceMock.get(any())).thenReturn(createSuccessfulResponse());
        HttpServerResponse responseMock = mock(HttpServerResponse.class);

        getBalanceController.getBalance("1", responseMock);

        verify(responseMock).end(captor.capture());
        JsonObject object = expectedJson();
        assertEquals(Json.encodePrettily(object), captor.getValue());
        verify(responseMock).setStatusCode(HttpResponseStatus.CREATED.code());
    }

    @Test
    public void controllerSetsRightStatusCodeWhenBalanceRetrievesError() {
        HttpServerResponse responseMock = mock(HttpServerResponse.class);
        when(getBalanceMock.get(any())).thenReturn(createErrorResponse());

        getBalanceController.getBalance("1", responseMock);

        verify(responseMock).setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
    }

    private static GetBalanceController setUpController() {
        getBalanceMock = mock(GetAccountBalanceService.class);
        GetBalanceController getBalanceController = new GetBalanceController(getBalanceMock);
        return getBalanceController;
    }

    private static JsonObject expectedJson() {
        JsonObject object = new JsonObject();
        object.put(BALANCE, "1");
        Json.encodePrettily(object);
        return object;
    }

    private static CompletableFuture<GetBalanceResponse> createSuccessfulResponse() {
        return CompletableFuture.completedFuture(GetBalanceResponse.createSuccessful(createBalance()));
    }

    private static CompletableFuture<GetBalanceResponse> createErrorResponse() {
        return CompletableFuture.completedFuture(GetBalanceResponse.createError());
    }

    private static Balance createBalance() {
        return new Balance(BigDecimal.ONE);
    }

}