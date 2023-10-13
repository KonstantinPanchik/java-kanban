package server.Handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;

public class EpicHandler extends HandlerMethods implements HttpHandler {
    TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        super();

        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /epic от сервера");
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        String response = "Такого эндпоинта не существует";
        int responseCode = 404;
        switch (Endpoints.valueOf(method)) {

            case GET:
                if (query == null) {
                    responseCode = 200;
                    response = gson.toJson(taskManager.getAllEpicTasks());
                    break;
                }
                if (query.startsWith("id=")) {
                    responseCode = 200;
                    response = gson.toJson(taskManager.getTask(getIdFromQuery(query)));
                }
                break;
            case POST:
                Epic epic = getEpicFromJson(exchange);
                if (taskManager.addEpic(epic)) {
                    responseCode = 200;
                    response = "Задача " + epic.getName() + "успешно добавлена с id: " + epic.getId();
                    System.out.println(epic.getDuration());
                } else {
                    responseCode = 404;
                    response = "Произошла ошибка задача не добавлена";
                }
                break;

            default: {

            }
        }
        writeStringResponse(exchange, response, responseCode);

    }

    private Epic getEpicFromJson(HttpExchange exchange) throws IOException {
        Epic task = null;
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes());
        try {
            task = gson.fromJson(body, Epic.class);
        } catch (JsonSyntaxException e) {

        }
        return task;
    }

}
