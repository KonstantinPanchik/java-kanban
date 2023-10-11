package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.InputStream;
import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    TaskManager taskManager;

    HttpServer server;

    Gson gson;

    public HttpTaskServer() throws IOException {
        gson= new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Epic.class,new EpicDeserializer())
                .create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/",new HandlerForReturningPrioritizedTasks());
        server.createContext("/tasks/task/", new TasksHandler());
        server.createContext("/tasks/epic/", new EpicHandler());
        server.createContext("/tasks/subtask/", new SubtaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
        taskManager = Managers.getDefaultHttpTaskManager();

    }
    public HttpTaskServer(TaskManager taskManager)throws IOException{
        this();
        this.taskManager=taskManager;
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().server.start();
    }

    class HandlerForReturningPrioritizedTasks implements HttpHandler {


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /task/(getPriritet)");
            String method=exchange.getRequestMethod();
            String response="Такого эндпоинта не существует";
            int code=404;
            if (method.equals("GET")){
                response=gson.toJson(taskManager.getPrioritizedTasks());
                code=200;
            }
            writeStringResponse(exchange,response,code);
        }
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /history от сервера");
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                String response = gson.toJson(taskManager.getHistory());
                writeStringResponse(exchange, response, 200);
            } else {
                writeStringResponse(exchange, "Эндпойнт не существует", 404);
            }

        }
    }

    class TasksHandler implements HttpHandler {
        String response = "Обработка данного метода не настроена";
        int responseCode = 404;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks от сервера");
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getRawQuery();
            switch (method) {

                case "GET":
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
                case "POST":
                    Task task = taskFromJson(exchange);
                    if (taskManager.addTask(task)) {
                        responseCode = 200;
                        response = "Задача " + task.getName() + "успешно добавлена с id: " + task.getId();
                    } else {
                        responseCode = 404;
                        response = "Произошла ошибка задача не добавлена";
                    }
                    break;
                case "DELETE":
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

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /epic от сервера");
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String response = "Такого эндпоинта не существует";
            int responseCode = 404;
            switch (method) {

                case "GET":
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
                case "POST":
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

    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /subtask от сервера");
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String response = "Такого эндпоинта не существует";
            int responseCode = 404;
            String array[] = exchange.getRequestURI().getPath().split("/");
            switch (method) {

                case "GET":
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

                case "POST":
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


    private void writeStringResponse(HttpExchange exchange,
                                     String responseString,
                                     int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private int getIdFromQuery(String query) {
        String substringId = query.substring(3);
        try {
            int id = Integer.parseInt(substringId);
            return id;
        } catch (NumberFormatException e) {
            //задачи с id 0 быть не может
            return 0;
        }
    }

    public void startServer(){
        System.out.println("Запускаем TaskSERVER сервер на порту"+PORT);
        server.start();
    }

    public void stopServer(){
        System.out.println("Останавливаем TaskSERVER сервер на порту"+PORT);
        server.stop(0);}

}