package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import server.Handlers.*;
import tasks.Epic;


import java.net.InetSocketAddress;
import java.io.IOException;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HttpTaskServer {

    public static final int PORT = 8080;

    TaskManager taskManager;

    HttpServer server;



    public HttpTaskServer() throws IOException {

        taskManager = Managers.getDefaultHttpTaskManager();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/", new HandlerForReturningPrioritizedTasks( taskManager));
        server.createContext("/tasks/task/", new TasksHandler(taskManager));
        server.createContext("/tasks/epic/", new EpicHandler(taskManager));
        server.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        server.createContext("/tasks/history", new HistoryHandler(taskManager));


    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this();
        this.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().server.start();
    }

    public void startServer() {
        System.out.println("Запускаем TaskSERVER сервер на порту" + PORT);
        server.start();
    }

    public void stopServer() {
        System.out.println("Останавливаем TaskSERVER сервер на порту" + PORT);
        server.stop(0);
    }

}

