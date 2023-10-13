package server.Handlers;


import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;

public class TasksHandler extends HandlerMethods implements HttpHandler {


    TaskManager taskManager;


    public TasksHandler( TaskManager taskManager){
        this.taskManager=taskManager;
    }

    String response = "Обработка данного метода не настроена";
    int responseCode = 404;



    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /tasks от сервера");
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getRawQuery();
        switch (Endpoints.valueOf(method)) {

            case GET:
                if (query == null) {
                    response = gson.toJson(taskManager.getAllUsualTasks());
                    responseCode = 200;
                    break;
                }
                if (query.startsWith("id=")) {
                    response = responseTaskById(query);
                    break;
                }
                break;
            case POST:
                Task task = taskFromJson(exchange);
                if (taskManager.addTask(task)) {
                    responseCode = 200;
                    response = "Задача " + task.getName() + "успешно добавлена с id: " + task.getId();
                } else {
                    responseCode = 404;
                    response = "Произошла ошибка задача не добавлена";
                }
                break;
            case DELETE:
                if (query == null) {
                    response = "Все задачи удалены";
                    responseCode = 200;
                    break;
                }
                if (query.startsWith("id=")) {
                    taskManager.removeTaskById(getIdFromQuery(query));
                    response = "Задача с " + query + "удалена";
                    break;
                }
            default:

        }
        writeStringResponse(exchange, response, responseCode);
    }


    private Task taskFromJson(HttpExchange exchange) throws IOException {
        Task task = null;
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes());
        try {
            task = gson.fromJson(body, Task.class);
        } catch (JsonSyntaxException e) {

        }
        return task;

    }

    private String responseTaskById(String query) {

        int id = getIdFromQuery(query);

        Task task = taskManager.getTask(id);
        if (task != null) {
            responseCode = 200;
            response = gson.toJson(task);
        } else {
            responseCode = 404;
            if (id != 0) {
                response = "Задача c id:" + id + " отсутствует";
            }
        }
        return response;
    }

}