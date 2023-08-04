package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    ArrayList<Task> getAllUsualTasks();

    ArrayList<Epic> getAllEpicTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Task> getAllTasks();

    Task getTask(int id);


    void removeTaskById(int id);

    void removeAllTasks();
    ArrayList<SubTask> getSubTasksOfEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    public HistoryManager getHistoryManager();
}

