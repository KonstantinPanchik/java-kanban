package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, SubTask> subTasks;
    HashMap<Integer, Epic> epics;
    private static int createdId;

    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    private static int createId() {
        return ++createdId;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if (task.getClass() != Task.class) {
            return;
        }
        if (tasks.containsValue(task)) {
            System.out.println("You can't add " + task.getName() + " Task. The Task is added already");
            return;
        }
        task.setId(createId());
        tasks.put(task.getId(), task);
        System.out.println("Task " + task.getName() + " has been added");
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epic.getClass() != Epic.class) {
            return;
        }
        if (tasks.containsValue(epic)) {
            System.out.println("You can't add " + epic.getName() + " Epic. The Epic is added already");
            return;
        }
        epic.setId(createId());
        epics.put(epic.getId(), epic);
        System.out.println("Epic " + epic.getName() + " has been added");
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        if (subTask.getClass() != SubTask.class) {
            return;
        }
        if (tasks.containsValue(subTask)) {
            System.out.println("You can't add " + subTask.getName() + " SubTask. The SubTask is added already");
            return;
        }
        Epic epic;
        if (epics.containsKey(subTask.getEpicId())) {
            epic = epics.get(subTask.getEpicId());
            epic.addSubTaskInEpic(subTask);
        } else {
            System.out.println("There is not Epic with id " + subTask.getEpicId());
            System.out.println("SubTask " + subTask.getName() + " has not been added");
            return;
        }
        subTask.setId(createId());
        subTasks.put(subTask.getId(), subTask);
        System.out.println("SubTask " + subTask.getName() + " has been added");

    }

    @Override
    public ArrayList<Task> getAllUsualTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = new ArrayList<>(getAllUsualTasks());
        list.addAll(getAllEpicTasks());
        list.addAll(getAllSubTasks());
        return list;
    }

    @Override
    public Task getTask(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            return;
        }
        if (epics.containsKey(id)) {  // при удаление эпика все его субТаски также удаляются
            Epic epic = epics.get(id);
            for (SubTask subTask : epic.getSubTasksInEpic()) {
                subTasks.remove(subTask.getId());
                epic.removeSubTask(subTask);
            }
            epics.remove(id);
            return;
        }
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(subTask); //новый метод
            subTasks.remove(id);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        return epic.getSubTasksInEpic();
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        if (task.getClass() != Task.class) {
            System.out.println("You must use Task as parameter");
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epic.getClass() != Epic.class) {
            System.out.println("You must use Epic as parameter");
            return;
        }
        epic.updateStatus();
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        if (subTask.getClass() != SubTask.class) {
            System.out.println("You must use SubTask as parameter");
            return;
        }
        epics.get(subTask.getEpicId()).updateStatus();
        subTasks.put(subTask.getId(), subTask);
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}

