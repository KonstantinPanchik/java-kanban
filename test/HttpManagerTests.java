import Client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.FileBackedTasksManager;
import manager.HttpTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.EpicDeserializer;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.TaskBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import manager.ManagerSaveException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HttpManagerTests extends InMemoryTaskManagerTest {

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    KVTaskClient kvTaskClient;

    Gson gson;

    @BeforeEach
    public void setDataToServer() throws IOException {
        System.out.println("Начинаем загрузку данных на KVServer");
        kvServer = new KVServer();
        kvServer.start();


        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Epic.class, new EpicDeserializer()).create();

        taskManager = new HttpTaskManager("http://localhost:8077/");
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.startServer();
        System.out.println("Данные загружены,сервера работают новый HttpTaskManadger создан");

    }

    @Test
    public void shoudReturnTaskWithId() throws IOException{
        File file = Paths.get("defaultDataForHttpTest.csv").toFile();
        TaskManager managerForFillingDateFromFile = FileBackedTasksManager.loadFromFile(file);
        kvTaskClient = new KVTaskClient("http://localhost:8077/");
        kvTaskClient.put("TASKS", gson.toJson(managerForFillingDateFromFile.getAllUsualTasks()));
        kvTaskClient.put("EPICS", gson.toJson(managerForFillingDateFromFile.getAllEpicTasks()));
        kvTaskClient.put("SUBTASKS", gson.toJson(managerForFillingDateFromFile.getAllSubTasks()));

        kvTaskClient.put("HISTORY", TaskBuilder.historyToString(managerForFillingDateFromFile.getHistoryManager()));


        taskManager=new HttpTaskManager("http://localhost:8077/");
        Task taskLoadedfromKVServer=taskManager.getTask(898);
        assertEquals(taskLoadedfromKVServer.getName(),"Купить роутер на яндекс маркете");
        Task taskLoadedFromFile=managerForFillingDateFromFile.getTask(898);
        assertEquals(taskLoadedFromFile,taskLoadedFromFile);

    }
    @Test
    public void shoudEpicWithRightAmountOfSubtask() throws IOException{
        File file = Paths.get("defaultDataForHttpTest.csv").toFile();
        TaskManager managerForFillingDateFromFile = FileBackedTasksManager.loadFromFile(file);
        kvTaskClient = new KVTaskClient("http://localhost:8077/");
        kvTaskClient.put("TASKS", gson.toJson(managerForFillingDateFromFile.getAllUsualTasks()));
        kvTaskClient.put("EPICS", gson.toJson(managerForFillingDateFromFile.getAllEpicTasks()));
        kvTaskClient.put("SUBTASKS", gson.toJson(managerForFillingDateFromFile.getAllSubTasks()));

        kvTaskClient.put("HISTORY", TaskBuilder.historyToString(managerForFillingDateFromFile.getHistoryManager()));


        taskManager=new HttpTaskManager("http://localhost:8077/");
        Epic taskLoadedfromKVServer=(Epic) taskManager.getTask(2);
        Epic taskLoadedFromFile=(Epic)managerForFillingDateFromFile.getTask(2);
        assertEquals(managerForFillingDateFromFile.getSubTasksOfEpic(taskLoadedFromFile)
                ,taskManager.getSubTasksOfEpic(taskLoadedfromKVServer));
        assertEquals(taskLoadedFromFile,taskLoadedFromFile);


    }

    @AfterEach
    public void stopEveryThing() {
        kvServer.stop();
        httpTaskServer.stopServer();
    }
}
