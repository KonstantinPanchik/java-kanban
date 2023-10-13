package manager;

import Client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import java.util.Collection;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    KVServer kvServer;
    KVTaskClient kvTaskClient;
    Gson gson;



     private void fillTasksMap(List<Task> list) {
         for (Task task : list) {
             addTask(task);
         }
    }

    private void fillEpicsMap(List<Epic> list) {
        for (Epic epic : list) {
            addEpic(epic);
        }
    }

    private void fillSubtaskMap(List<SubTask> list) {
        for (SubTask subTask : list) {
            addSubTask(subTask);
        }
    }

    public HttpTaskManager(String pathToKVServer) throws IOException {
        super(Paths.get("src/ProgrammsFiles/"+pathToKVServer.substring("http://localhost:".length(), 21) + ".csv")
                .toFile());

        kvTaskClient = new KVTaskClient(pathToKVServer);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Epic.class,new EpicDeserializer())
                .create();

        String arrayTasksJson = kvTaskClient.load("TASKS");
        String arrayEpicsJson = kvTaskClient.load("EPICS");
        String arraySubtaskJson = kvTaskClient.load("SUBTASKS");
        String historyString = kvTaskClient.load("HISTORY");

        try {
            TypeToken<Collection<Task>> typeToken =new TypeToken<Collection<Task>>(){};
            List<Task> listTasks = gson.fromJson(arrayTasksJson,typeToken.getType());
            fillTasksMap(listTasks);

            TypeToken<Collection<Epic>> typeTokenEpic =new TypeToken<Collection<Epic>>(){};
            List<Epic> listEpics = gson.fromJson(arrayEpicsJson,typeTokenEpic.getType());
            fillEpicsMap(listEpics);

            TypeToken<Collection<SubTask>> typeTokenSubtask =new TypeToken<Collection<SubTask>>(){};
            List<SubTask> listSubtask = gson.fromJson(arrayEpicsJson,typeTokenSubtask.getType());
            fillSubtaskMap(listSubtask);

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
