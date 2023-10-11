package manager;

import Client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import server.EpicDeserializer;
import server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    KVServer kvServer;
    KVTaskClient kvTaskClient;
    Gson gson;


    static void fillTasksMap(Task[] arrayTasks, TaskManager manager) {
        for (int i = 0; i < arrayTasks.length; i++) {
            Task usualTask = arrayTasks[i];
            manager.addTask(usualTask);
        }
    }

    static void fillEpicsMap(Epic[] arrayEpic, TaskManager manager) {
        for (int i = 0; i < arrayEpic.length; i++) {
            Epic epic = arrayEpic[i];
            manager.addEpic(epic);
        }
    }

    static void fillSubtaskMap(SubTask[] arraySubtask, TaskManager manager) {
        for (int i = 0; i < arraySubtask.length; i++) {
            SubTask subTask = arraySubtask[i];
            manager.addSubTask(subTask);
        }
    }

    public HttpTaskManager(String uri) throws IOException {
        super(Paths.get(uri.substring("http://localhost:".length(), 21) + ".csv").toFile());
        kvTaskClient = new KVTaskClient(uri);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Epic.class,new EpicDeserializer())
                .create();

        String arrayTasksJson = kvTaskClient.load("TASKS");
        String arrayEpicsJson = kvTaskClient.load("EPICS");
        String arraySubtaskJson = kvTaskClient.load("SUBTASKS");
        String historyString = kvTaskClient.load("HISTORY");

        try {

            Task[] arrayTasks = gson.fromJson(arrayTasksJson, Task[].class);

            fillTasksMap(arrayTasks, this);


            Epic[] arrayEpic = gson.fromJson(arrayEpicsJson, Epic[].class);
            fillEpicsMap(arrayEpic, this);

            SubTask[] arraySubTask = gson.fromJson(arraySubtaskJson, SubTask[].class);
            fillSubtaskMap(arraySubTask, this);

            this.fillHistoryList(historyString);

            this.setCreatedID();
        } catch (JsonSyntaxException exception) {
            System.out.println("ошибка синтаксиса джсон");
        }

    }



    @Override
    void save() {
        super.save();
        System.out.println("_______________");
        System.out.println("Обновляем данные на сервере");
        kvTaskClient.put("TASKS", gson.toJson(getAllUsualTasks()));
        kvTaskClient.put("EPICS", gson.toJson(getAllEpicTasks()));
        kvTaskClient.put("SUBTASKS", gson.toJson(getAllSubTasks()));
        kvTaskClient.put("HISTORY", TaskBuilder.historyToString(getHistoryManager()));
        System.out.println("______________");

    }


    //ниже не нужное


    public static void main(String[] args) throws IOException {
        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078/");

        SubTask subTask = new SubTask("сабтаска эпика 897", "897 jgbcfybt", 897);
        manager2.addSubTask(subTask);
    }


}
