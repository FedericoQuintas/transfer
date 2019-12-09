package account.http;

import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.VOs.TransferMoneyResponse;
import account.domain.entities.Account;
import account.domain.services.TransferMoneyService;
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

public class TransferMoneyControllerTest {

    private static final BigDecimal ONE = BigDecimal.valueOf(1f);
    private static final String FROM_ACCOUNT_ID = "from_account_id";
    private static final String TO_ACCOUNT_ID = "to_account_id";
    private static final String AMOUNT = "amount";
    private static TransferMoneyService transferMoneyServiceMock;
    private TransferMoneyController transferMoneyController;
    private HttpServerResponse responseMock;

    @Before
    public void before() {
        setUpController();
    }

    @Test
    public void controllerSetsRightStatusCodeWhenBalanceRetrievesSuccess() {

        when(transferMoneyServiceMock.transfer(any(), any(), any())).thenReturn(createSuccessfulResponse());

        JsonObject jsonObject = createRequestBodyJson();
        transferMoneyController.transfer(jsonObject, responseMock);

        verify(responseMock).setStatusCode(HttpResponseStatus.CREATED.code());
    }

    @Test
    public void controllerSetsRightStatusCodeWhenBalanceRetrievesError() {

        when(transferMoneyServiceMock.transfer(any(), any(), any())).thenReturn(createErrorResponse());

        JsonObject jsonObject = createRequestBodyJson();
        transferMoneyController.transfer(jsonObject, responseMock);

        verify(responseMock).setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
    }

    @Test
    public void parsesJsonCorrectly() {
        when(transferMoneyServiceMock.transfer(any(), any(), any())).thenReturn(createSuccessfulResponse());

        ArgumentCaptor<AccountId> accountsCaptor = ArgumentCaptor.forClass(AccountId.class);
        ArgumentCaptor<Amount> amountCapture = ArgumentCaptor.forClass(Amount.class);

        JsonObject jsonObject = createRequestBodyJson();
        transferMoneyController.transfer(jsonObject, responseMock);

        verify(transferMoneyServiceMock).transfer(accountsCaptor.capture(), accountsCaptor.capture(), amountCapture.capture());
        assertEquals(AccountId.valueOf("1"),accountsCaptor.getAllValues().get(0));
        assertEquals(AccountId.valueOf("2"),accountsCaptor.getAllValues().get(1));
        assertEquals(Amount.valueOf(ONE), amountCapture.getValue());
    }


    private static JsonObject createRequestBodyJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put(FROM_ACCOUNT_ID, "1");
        jsonObject.put(TO_ACCOUNT_ID, "2");
        jsonObject.put(AMOUNT, 1);
        return jsonObject;
    }

    private void setUpController() {
        responseMock = mock(HttpServerResponse.class);
        transferMoneyServiceMock = mock(TransferMoneyService.class);
        transferMoneyController = new TransferMoneyController(transferMoneyServiceMock);
    }

    private static CompletableFuture<TransferMoneyResponse> createSuccessfulResponse() {
        return CompletableFuture.completedFuture(TransferMoneyResponse.createSuccessful());
    }

    private static CompletableFuture<TransferMoneyResponse> createErrorResponse() {
        return CompletableFuture.completedFuture(TransferMoneyResponse.createError());
    }

}