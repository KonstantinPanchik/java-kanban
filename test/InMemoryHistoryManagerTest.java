import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    Task task1;
    Task task2;
    Task task3;


    @BeforeEach
    public void setHistoryManager() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "Test addNewTask", Status.NEW, "Test addNewTask description");
        task2 = new Task(2, "Test addNewTask2", Status.NEW, "Test addNewTask description2");
        task3 = new Task(3, "Test addNewTask3", Status.NEW, "Test addNewTask description3");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
    }

    @Test
    public void shouldAddTask() {
        List<Task> history = historyManager.getHistory();
        Task addedTask = history.get(0);
        assertEquals(3, history.size(), " Задача не добавлена");
        assertEquals(task1, addedTask, "Задачи не совпадают");
    }

    @Test
    public void shouldDeletefirtAndAddlastIfSame() {

        List<Task> history = historyManager.getHistory();
        Task addedTask1 = history.get(0);
        Task addedTask2 = history.get(1);
        Task addedTask3 = history.get(2);

        assertEquals(3, history.size(), " Задачи не добавились");
        assertEquals(task1, addedTask1, "Задачи не совпадают");
        assertEquals(task2, addedTask2, "Задачи не совпадают");
        assertEquals(task3, addedTask3, "Задачи не совпадают");

        historyManager.add(task1);

        List<Task> expected = List.of(task2, task3, task1);
        history = historyManager.getHistory();

        assertEquals(expected, history);
    }

    @Test
    public void shouldDeleteAllTasks() {

        List<Task> history = historyManager.getHistory();
        List<Task> expected = List.of(task1, task2, task3);
        assertEquals(history, expected);

        historyManager.removeAllTasks();

        history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    public void shouldDeleteFirt() {
        historyManager.remove(1);
        assertEquals(List.of(task2,task3),historyManager.getHistory());

    }

    @Test
    public void shouldDeleteMidle() {
        historyManager.remove(2);
        assertEquals(List.of(task1,task3),historyManager.getHistory());
    }

    @Test
    public void shouldDeleteLast() {
        historyManager.remove(3);
        assertEquals(List.of(task1,task2),historyManager.getHistory());
    }
}
