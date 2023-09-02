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

       SubTask removingBirthmark= new SubTask("Удаление родинки","Удалить родинку у онколога"
               , hospitalCheckIn.getId());
       manager.addSubTask(removingBirthmark);

       Epic planVacation = new Epic("Разпланировать отпуск", "Проверить все дела и запланировть отпуск");
        manager.addEpic(planVacation);

        SubTask askAboutFreeDates = new SubTask("Спросить даты"
                , "Узнать у HR возможные даты для отпуска"
                , planVacation.getId());
        manager.addSubTask(askAboutFreeDates);

        SubTask searchHotel = new SubTask("Найти отель","Выбрать хороший отель на Яндекс.Путешествиях",
                planVacation.getId());
        manager.addSubTask(searchHotel);


        System.out.println("\n1 Получаем задачи по ID");
        manager.getTask(bloodAnalysis.getId());
        manager.getTask(ultrasound.getId());
        manager.getTask(openDepositInBank.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(planVacation.getId());
        manager.getTask(askAboutFreeDates.getId());
        manager.getTask(searchHotel.getId());
        manager.getTask(ultrasound.getId());

        System.out.println("\nПечать истории просмотра: ");
        for (Task task : manager.getHistory()) {

            System.out.println(task.getName());
        }


        System.out.println("\n2 Получаем задачи по ID");
        manager.getTask(searchHotel.getId());
        manager.getTask(bloodAnalysis.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(ultrasound.getId());
        manager.getTask(openDepositInBank.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(hospitalCheckIn.getId());
        manager.getTask(removingBirthmark.getId());
        manager.getTask(searchHotel.getId());
        manager.getTask(ultrasound.getId());
        manager.getTask(ultrasound.getId());
        manager.getTask(searchHotel.getId());
        manager.getTask(planVacation.getId());
        manager.getTask(askAboutFreeDates.getId());

        System.out.println("\nУдаление задач:");

        manager.removeTaskById(ultrasound.getId());


        manager.removeTaskById(planVacation.getId());



        System.out.println("\nПечать истории просмотра: ");
        for (Task task : manager.getHistory()) {

            System.out.println(task.getName());
        }
    }

}
