import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {

    TaskManager taskManager;


    @Test
    public void shouldAddTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllUsualTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotAddTask() {
        Task task = null;
        taskManager.addTask(task);
        assertThrows(NullPointerException.class, () -> task.getId());
        assertEquals(0, taskManager.getAllUsualTasks().size(), "Список задач не пуст!");
    }

    @Test
    public void shouldAddEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        final Task savedTask = taskManager.getTask(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpicTasks();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotAddEpic() {
        Task task = null;
        taskManager.addTask(task);
        assertThrows(NullPointerException.class, () -> task.getId());
        assertEquals(0, taskManager.getAllEpicTasks().size(), "Список задач не пуст!");
    }

    @Test
    public void shouldAddSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Subtask addNewSubTask", "Subtask addNewSubTask", epic.getId());
        taskManager.addSubTask(subTask);

        final int subTaskId = subTask.getId();
        final SubTask savedTask = (SubTask) taskManager.getTask(subTaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(savedTask, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotAddSubTask() {
        Task task = null;
        taskManager.addTask(task);
        assertThrows(NullPointerException.class, () -> task.getId());
        assertEquals(0, taskManager.getAllEpicTasks().size(), "Список задач не пуст!");
    }

    @Test
    public void shouldNotAddSubTaskWithoutEpic() {
        SubTask subTask = new SubTask("Subtask addNewSubTask", "Subtask addNewSubTask", 777);
        taskManager.addSubTask(subTask);
        assertTrue(taskManager.getAllSubTasks().isEmpty(),"SubTask был добавлен");
    }
@Test
    public void shouldReturnNullIfthereIsNotId(){
        int fakeId=189233755;
        Task task= taskManager.getTask(fakeId);
        assertNull(task,"Задача не null");
    }

    @Test
    public void shouldDeleteTaskById(){
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Subtask addNewSubTask", "Subtask addNewSubTask", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(3,taskManager.getAllTasks().size());
        taskManager.removeTaskById(subTask.getId());
        assertEquals(2,taskManager.getAllTasks().size());
    }
    @Test
    public void shouldDeleteAllTasks(){
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Subtask addNewSubTask", "Subtask addNewSubTask", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(3,taskManager.getAllTasks().size());
        taskManager.removeAllTasks();
        assertEquals(0,taskManager.getAllTasks().size());
    }

    //Тесты на добавление задачи при пересечении

    @Test
    public void shouldAddTaskIfTimeIsDifferent(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,14,0,00)
                ,240);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfNewTaskInsideEx(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,12,10,00)
                ,30);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfNewTaskStartInsideEx(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,12,10,00)
                ,120);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfNewTaskEndInsideEx(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,11,10,00)
                ,180);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfExTaskInsideNew(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,11,00,00)
                ,180);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfNewTaskEndEqualsExStart(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,11,10,00)
                ,50);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2,taskManager.getPrioritizedTasks().size());

    }
    @Test
    public void shouldNotAddTaskIfNewTaskStartEqualsExEnd(){

        Task task1=new Task("Task1","TaskDescription"
                , LocalDateTime.of(2020,01,01,12,00,00)
                ,60);
        Task task2=new Task("Task2","TaskDescription"
                ,LocalDateTime.of(2020,01,01,13,00,00)
                ,50);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2,taskManager.getPrioritizedTasks().size());

    }


}


