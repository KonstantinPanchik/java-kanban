package manager;

import tasks.TaskBuilder;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;
    String fileName;

    public FileBackedTasksManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
        this.fileName = file.getName();
    }

    public FileBackedTasksManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
        this.fileName = file.getName();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;

    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    void save() {
        try {
            Files.createFile(file.toPath());
            System.out.println("Новый файл " + file + " создан");
        } catch (IOException e) {

        }
        try (Writer writer = new FileWriter(fileName)) {
            StringBuilder insideFile = new StringBuilder();
            insideFile.append("id,type,name,status,description,epic,startDate,duration\n");
            for (Integer integer : tasks.keySet()) {
                String line = TaskBuilder.toString(tasks.get(integer));
                insideFile.append(line + "\n");
            }
            for (Integer integer : epics.keySet()) {
                String line = TaskBuilder.toString(epics.get(integer));
                insideFile.append(line + "\n");
            }
            for (Integer integer : subTasks.keySet()) {
                String line = TaskBuilder.toString(subTasks.get(integer));
                insideFile.append(line + "\n");
            }
            insideFile.append("\n");
            insideFile.append(TaskBuilder.historyToString(historyManager));

            writer.write(insideFile.toString());

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи данных в файл");
        }

    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String taskLine = null;
            while (reader.ready()) {
                taskLine = reader.readLine();
                Task t = TaskBuilder.fromString(taskLine);
                fileBackedTasksManager.fillMapsByTask(t);
            }

            fileBackedTasksManager.fillHistoryList(taskLine);

            fileBackedTasksManager.setCreatedID();
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден!!!");
        }


        return fileBackedTasksManager;
    }

    void setCreatedID() {
        List<Integer> findMax = new ArrayList<Integer>(tasks.keySet());
        findMax.addAll(new ArrayList<>(epics.keySet()));
        findMax.addAll(new ArrayList<>(subTasks.keySet()));
        for (Integer id : findMax) {
            if (id > createdId) {
                createdId = id;
            }
        }
    }

    void fillMapsByTask(Task t) {
        if (t == null) {
            return;
        }
        switch (t.getType()) {

            case TASK: {
                super.addTask(t);
                break;
            }
            case EPIC: {
                super.addEpic((Epic) t);
                break;
            }
            case SUBTASK: {
                super.addSubTask((SubTask) t);
            }
            default: {
            }
        }

    }

    void fillHistoryList(String last) {
        for (int i : TaskBuilder.historyFromString(last)) {
            this.historyManager.add(this.getTask(i));
        }
    }




    // проверка работоспособности
    public static void main(String[] args) {
        File file = Paths.get("save.csv").toFile();
        TaskManager manager = FileBackedTasksManager.loadFromFile(file);

//        9,SUBTASK,Узи,NEW,Пройти узи брюшной полости,2
//        SubTask ultraSaund = new SubTask("Узи", "Пройти узи брюшной полости", 2);
//        manager.addSubTask(ultraSaund);
//        System.out.println("\nПечать истории просмотра: ");

        List<Epic> list = manager.getAllEpicTasks();
        System.out.println(list.get(0).getSubTasksInEpic());
        manager.getTask(1);


        for (Task task : manager.getHistory()) {

            System.out.println(task.getName());
        }


    }

}


