import manager.InMemoryTaskManager;
import manager.Managers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class InMemoryTaskManagerTest extends TaskManagerTest{


    @BeforeEach
    public  void setTaskManager(){

        taskManager= Managers.getDefault();
    }



    @Test
    public void shouldReturnTimeSortedSet(){

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
        List<Task> sorted=new ArrayList<>(taskManager.getPrioritizedTasks());
        assertEquals(taskManager.getTask(1),sorted.get(0));
        assertEquals(taskManager.getTask(removingBirthmark.getId()),sorted.get(1));

    }


}
