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

public class FileBackedTasksManagerTest extends TaskManagerTest {
    File file;

    @BeforeEach
    public void setTaskManager() {
        file = Paths.get("test/TestFiles/saveTestDefault.csv").toFile();
        taskManager = new FileBackedTasksManager(file, new InMemoryHistoryManager());
    }

    @Test
    public void shouldSaveInTotheFile() {
        file = Paths.get("test/TestFiles/saveTestSavingIntoFile.csv").toFile();
        taskManager = new FileBackedTasksManager(file, new InMemoryHistoryManager());

        Task openDepositInBank = new Task("Открыть депозит в банке", "Сходить в банк и открыть счёт"
                , LocalDateTime.now()
                ,30);
        taskManager.addTask(openDepositInBank);


        //Создание эпиков
        Epic hospitalCheckIn = new Epic("Обследование в поликлиннике", "Пройти все необходимые процедуры");
        //добавление эпика
        taskManager.addEpic(hospitalCheckIn);
        //Создание подзадач
        SubTask bloodAnalysis = new SubTask("Анализ крови"
                , "Прийти в клинику и сдать кровь"
                , hospitalCheckIn.getId());
        //добавление подзадач
        taskManager.addSubTask(bloodAnalysis);

        SubTask ultrasound = new SubTask("УЗИ", "Пройти узи брюшной полости", hospitalCheckIn.getId());
        taskManager.addSubTask(ultrasound);

        SubTask removingBirthmark = new SubTask("Удаление родинки", "Удалить родинку у онколога"
                , hospitalCheckIn.getId(),LocalDateTime.now().plus(3, ChronoUnit.DAYS),90);
        taskManager.addSubTask(removingBirthmark);

        Epic planVacation = new Epic("Разпланировать отпуск", "Проверить все дела и запланировть отпуск");
        taskManager.addEpic(planVacation);

        SubTask askAboutFreeDates = new SubTask("Спросить даты"
                , "Узнать у HR возможные даты для отпуска"
                , planVacation.getId());
        taskManager.addSubTask(askAboutFreeDates);

        SubTask searchHotel = new SubTask("Найти отель", "Выбрать хороший отель на Яндекс.Путешествиях",
                planVacation.getId());
        taskManager.addSubTask(searchHotel);

        taskManager.getTask(bloodAnalysis.getId());
        taskManager.getTask(ultrasound.getId());
        taskManager.getTask(planVacation.getId());
        taskManager.getTask(removingBirthmark.getId());
        taskManager.getTask(searchHotel.getId());

        //Создание нового TaskManager из файла и проверка данных в них.
        TaskManager managerFromfile = FileBackedTasksManager.loadFromFile(file);


        assertEquals(managerFromfile.getAllTasks(), taskManager.getAllTasks(), "Задачи не совпадают");
        assertEquals(managerFromfile.getHistory(), taskManager.getHistory(), "История не совпадает");

        Epic epic1 = hospitalCheckIn;

        Epic epic2 = (Epic) managerFromfile.getTask(hospitalCheckIn.getId());

        assertEquals(epic1.getStartTime(),epic2.getStartTime());
        assertEquals(epic1.getEndTime(),epic2.getEndTime());

        assertEquals(3, epic2.getSubTasksInEpic().size());
        assertEquals(epic1, epic2, "Эпики не одинаковы");
        assertEquals(epic1.getSubTasksInEpic(), epic2.getSubTasksInEpic(), "Списки задач в эпике не одинаковы");
    }

    @Test
    public void shouldLoadFileWithEptyHistory() {
        file = Paths.get("test/TestFiles/saveTestWithEmptyHistory.csv").toFile();
        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void shouldLoadFileWithEptyFile() {
        file = Paths.get("test/TestFiles/saveTestWithEmptyFile.csv").toFile();
        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWithoutFile() {
        file = Paths.get("a file.csv").toFile();
        assertThrows(ManagerSaveException.class,()->taskManager = FileBackedTasksManager.loadFromFile(file));
    }

}


