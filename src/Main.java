import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        //Создание задачи
        Task openDepositInBank = new Task("Открыть депозит в банке", "Сходить в банк и открыть счёт");
        manager.addTask(openDepositInBank);
        //Создание эпиков
        Epic hospitalCheckIn = new Epic("Обследование в поликлиннике", "Пройти все необходимые процедуры");
        //добавление эпика
        manager.addEpic(hospitalCheckIn);
        //Создание подзадач
        SubTask bloodAnalysis = new SubTask("Анализ крови"
                , "Прийти в клинику и сдать кровь"
                , hospitalCheckIn.getId());
        //добавление подзадач
        manager.addSubTask(bloodAnalysis);

        SubTask ultrasound = new SubTask("УЗИ", "Пройти узи брюшной полости", hospitalCheckIn.getId());
        manager.addSubTask(ultrasound);

        Epic planVacation = new Epic("Разпланировать отпуск", "Проверить все дела и запланировть отпуск");
        manager.addEpic(planVacation);

        SubTask askAboutFreeDates = new SubTask("Спросить даты"
                , "Узнать у HR возможные даты для отпуска"
                , planVacation.getId());
        manager.addSubTask(askAboutFreeDates);


//        Печать всех списков определенных задач
        System.out.println(manager.getAllUsualTasks());
        System.out.println();
        System.out.println(manager.getAllEpicTasks());
        System.out.println();
        System.out.println(manager.getAllSubTasks());
        System.out.println("_____________________________________");
        //Изменение статуса в подзадачах
        ultrasound.setStatus(Status.DONE);
        bloodAnalysis.setStatus(Status.DONE);
        manager.updateEpic(hospitalCheckIn);
        System.out.println(hospitalCheckIn);
        System.out.println("_____________________________________");
        //Удаление задачи из эпика
        manager.removeTaskById(ultrasound.getId());
        System.out.println(hospitalCheckIn);
        System.out.println("_____________________________________");
        //Удаление эпика
        manager.removeTaskById(planVacation.getId());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("_____________________________________");
        System.out.println(manager.getAllTasks());


        System.out.println("@@@@@@@@@@@");
        manager.getTask(openDepositInBank.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(hospitalCheckIn.getId());


        System.out.println("ddddd");
        for (Task task : historyManager.getHistory()) {

            System.out.println(task.getName());
        }



    }

}
