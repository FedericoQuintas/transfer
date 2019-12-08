import account.domain.AccountRepository;
import account.domain.services.GetAccountBalanceService;
import account.domain.services.TransferMoneyService;
import account.http.GetBalanceController;
import account.http.TransferMoneyController;
import account.persistence.InMemoryAccountRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.json.simple.parser.ParseException;

public class Main extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        AccountRepository inMemoryAccountRepository = new InMemoryAccountRepository();
        TransferMoneyService transferMoneyService = new TransferMoneyService(inMemoryAccountRepository);
        TransferMoneyController transferMoneyController = new TransferMoneyController(transferMoneyService);
        GetBalanceController getBalanceController = new GetBalanceController(new GetAccountBalanceService(inMemoryAccountRepository));


        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().method(HttpMethod.GET)
                .path("/balance/:account_id")
                .produces("application/json");
        router.route().handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            String accountId = routingContext.request().getParam("account_id");

            getBalanceController.getBalance(accountId, response);
            // Write to the response and end it
            //response.end("Hello World from Vert.x-Web!");
        });

        Router transferenceRouter = Router.router(vertx);
        transferenceRouter.route().method(HttpMethod.POST)
                .path("/transference")
                .handler(BodyHandler.create())
                .consumes("application/json")
                .produces("application/json");


        transferenceRouter.route().handler(routingContext -> {
            JsonObject bodyAsJson = routingContext.getBodyAsJson();

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();

            try {
                transferMoneyController.transfer(bodyAsJson, response);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });

        server.requestHandler(router).listen(8080);

    }

}
