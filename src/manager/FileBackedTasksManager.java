package manager;

import Builders.TaskBuilder;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            insideFile.append("id,type,name,status,description,epic\n");
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

            while (reader.ready()) {
                String taskLine = reader.readLine();
                Task t = TaskBuilder.fromString(taskLine);
                fileBackedTasksManager.fillMapsByTask(t);
            }

            fileBackedTasksManager.fillHistoryList(file);

        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден!!!");
        }
        return fileBackedTasksManager;
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

    void fillHistoryList(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String last = null, line;
            while (null != (line = br.readLine())) {
                last = line;
            }
            for (int i : TaskBuilder.historyFromString(last)) {
               this.historyManager.add(this.getTask(i));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения истории из файла");
        }

    }

// проверка работоспособности
    public static void main(String[] args) {
        File file = Paths.get("save.csv").toFile();
        TaskManager manager = FileBackedTasksManager.loadFromFile(file);

        System.out.println("\nПечать истории просмотра: ");


        manager.getTask(1);
        for (Task task : manager.getHistory()) {

            System.out.println(task.getName());
        }


    }

}


