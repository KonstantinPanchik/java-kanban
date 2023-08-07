package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    List<Task> getAllUsualTasks();

    List<Epic> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    List<Task> getAllTasks();

    Task getTask(int id);


    void removeTaskById(int id);

    void removeAllTasks();
    List<SubTask> getSubTasksOfEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    List<Task> getHistory();
}

