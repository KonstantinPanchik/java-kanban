package server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;


import java.io.IOException;

public  class HandlerForReturningPrioritizedTasks extends HandlerMethods implements HttpHandler {

    TaskManager taskManager;


    public HandlerForReturningPrioritizedTasks( TaskManager taskManager){
        super();
        this.taskManager=taskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /task/(getPriritet)");
        String method=exchange.getRequestMethod();
        String response="Такого эндпоинта не существует";
        int code=404;
        if (Endpoints.GET.equals(Endpoints.valueOf(method))){
            response=gson.toJson(taskManager.getPrioritizedTasks());
            code=200;
        }
        writeStringResponse(exchange,response,code);
    }
}