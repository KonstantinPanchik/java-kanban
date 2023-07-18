import manager.Manager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        //Создание задачи
        Task openDepositInBank = new Task("Открыть депозит в банке", "Сходить в банк и открыть счёт");

        //Создание эпиков и его добавление в него подзадач
        Epic hospitalCheckIn = new Epic("Обследование в поликлиннике", "Пройти все необходимые процедуры");
        SubTask bloodAnalysis = new SubTask("Анализ крови", "Прийти в клинику и сдать кровь");
        SubTask ultrasound = new SubTask("УЗИ", "Пройти узи брюшной полости");
        hospitalCheckIn.addSubTask(bloodAnalysis, ultrasound);

        Epic planVacation = new Epic("Разпланировать отпуск", "Проверить все дела и запланировть отпуск");
        SubTask askAboutFreeDates = new SubTask("Спросить даты", "Узнать у HR возможные даты для отпуска");
        planVacation.addSubTask(askAboutFreeDates);
        //Добавление задач в менеджер
        manager.setTasks(openDepositInBank, hospitalCheckIn, planVacation);
        //Печать всех списков определенных задач
        System.out.println(manager.getAllUsualTasks());
        System.out.println();
        System.out.println(manager.getAllEpicTasks());
        System.out.println();
        System.out.println(manager.getAllSubTasks());
        System.out.println("_____________________________________");
        //Изменение статуса в подзадачах
        ultrasound.setStatus("DONE");
        bloodAnalysis.setStatus("NEW");
        System.out.println(hospitalCheckIn);
        System.out.println("_____________________________________");
        //Удаление задачи из эпика (после удаления статус эпика также обновится)
        manager.removeTaskById(ultrasound.getId());
        System.out.println(hospitalCheckIn);
        System.out.println("_____________________________________");
        //Удаление эпика
        manager.removeTaskById(planVacation.getId());
        System.out.println(manager.getAllEpicTasks());

    }

}
