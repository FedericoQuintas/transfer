import account.domain.AccountRepository;
import account.domain.VOs.AccountId;
import account.domain.VOs.Amount;
import account.domain.entities.Account;
import account.domain.services.GetAccountBalanceService;
import account.domain.services.TransferMoneyService;
import account.http.GetBalanceController;
import account.http.TransferMoneyController;
import account.persistence.InMemoryAccountRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.math.BigDecimal;

public class Main extends AbstractVerticle {

    public static final String BALANCE_URL = "/balance/:account_id";
    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCOUNT_ID = "account_id";
    public static final String SERVER_ERROR = "Server Error";
    public static final String TRANSFERENCE_URL = "/transference";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        AccountRepository inMemoryAccountRepository = setUpRepositoryWithSampleData();
        TransferMoneyService transferMoneyService = new TransferMoneyService(inMemoryAccountRepository);
        TransferMoneyController transferMoneyController = new TransferMoneyController(transferMoneyService);
        GetBalanceController getBalanceController = new GetBalanceController(new GetAccountBalanceService(inMemoryAccountRepository));

        HttpServer server = vertx.createHttpServer();

        Router router = configureRoutes(vertx, transferMoneyController, getBalanceController);

        server.requestHandler(router).listen(8080);
    }

    private static Router configureRoutes(Vertx vertx, TransferMoneyController transferMoneyController, GetBalanceController getBalanceController) {
        Router router = Router.router(vertx);
        router.get(BALANCE_URL)
                .produces(APPLICATION_JSON)
                .handler(routingContext -> {
                  HttpServerResponse response = routingContext.response();
                  String accountId = routingContext.request().getParam(ACCOUNT_ID);
                  getBalanceController.getBalance(accountId, response);
                });

        router.errorHandler(500, (ctx) -> ctx.response().setStatusCode(500).end(SERVER_ERROR));
        router.route().handler(BodyHandler.create());
        router.post(TRANSFERENCE_URL)
              .consumes(APPLICATION_JSON)
              .produces(APPLICATION_JSON)
              .handler(routingContext -> {
                JsonObject bodyAsJson = routingContext.getBodyAsJson();
                HttpServerResponse response = routingContext.response();
                transferMoneyController.transfer(bodyAsJson, response);
              });
        return router;
    }

    private static AccountRepository setUpRepositoryWithSampleData() {
        AccountRepository inMemoryAccountRepository = new InMemoryAccountRepository();
        Account account = new Account(AccountId.valueOf("1"));
        account.addCreditEvent(Amount.valueOf(BigDecimal.TEN));
        inMemoryAccountRepository.add(account);
        inMemoryAccountRepository.add(new Account(AccountId.valueOf("2")));
        return inMemoryAccountRepository;
    }

}


