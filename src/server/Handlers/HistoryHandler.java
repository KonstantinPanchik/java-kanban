package server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;


public class HistoryHandler extends HandlerMethods implements HttpHandler {

    TaskManager taskManager;

   public HistoryHandler( TaskManager taskManager){
        super();
        this.taskManager=taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /history от сервера");
        String method = exchange.getRequestMethod();
        if (Endpoints.GET.equals(Endpoints.valueOf(method))) {
            String response = gson.toJson(taskManager.getHistory());
            writeStringResponse(exchange, response, 200);
        } else {
            writeStringResponse(exchange, "Эндпойнт не существует", 404);
        }

    }

}

