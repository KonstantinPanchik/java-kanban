package server.Handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;

public  class SubtaskHandler extends HandlerMethods implements HttpHandler {

    TaskManager taskManager;


    public SubtaskHandler( TaskManager taskManager){

        this.taskManager=taskManager;
    }




    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /subtask от сервера");
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        String response = "Такого эндпоинта не существует";
        int responseCode = 404;
        String array[] = exchange.getRequestURI().getPath().split("/");
        switch (Endpoints.valueOf(method)) {

            case GET:
                if (query == null) {
                    responseCode = 200;
                    response = gson.toJson(taskManager.getAllSubTasks());
                    break;
                }
                if (query.startsWith("id=") && array.length == 3) {
                    responseCode = 200;
                    response = gson.toJson(taskManager.getTask(getIdFromQuery(query)));
                    break;
                }

                if (array.length == 4 && array[3].startsWith("epic")) {
                    if (query.startsWith("id=")) {
                        int id = getIdFromQuery(query);
                        responseCode = 200;
                        Task t = taskManager.getTask(id);
                        if (t == null) {
                            response = "Задача с таким " + query + "отсутсвует";
                            responseCode = 404;
                            break;
                        }
                        try {
                            Epic epic = (Epic) t;
                            response = gson.toJson(taskManager.getSubTasksOfEpic(epic));
                            break;
                        } catch (ClassCastException e) {
                            responseCode = 404;
                            response = "Под этим " + query + " находится не эпик";
                            break;
                        }
                    }
                    break;
                }

            case POST:
                SubTask subTask = getSubtaskFromJson(exchange);
                if (taskManager.addSubTask(subTask)) {
                    responseCode = 200;
                    response = "Задача " + subTask.getName() + "успешно добавлена с id: " + subTask.getId();
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

    private SubTask getSubtaskFromJson(HttpExchange exchange) throws IOException {
        SubTask task = null;
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes());
        try {
            task = gson.fromJson(body, SubTask.class);
        } catch (JsonSyntaxException e) {

        }
        return task;
    }
}


