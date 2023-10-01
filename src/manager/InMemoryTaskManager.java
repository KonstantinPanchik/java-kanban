package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.Type;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> tasks;
    Map<Integer, SubTask> subTasks;
    Map<Integer, Epic> epics;
    protected int createdId;

    protected HistoryManager historyManager;

    Set<Task> timeSortedSet;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = historyManager;

        timeSortedSet = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime() == null) {
                return 1;
            }
            if (task2.getStartTime() == null) {
                return -1;
            }
            if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    private int createId() {
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
        if (!isTimeSlotAvailable(task)) {
            return;
        }
        if (task.getId() == 0) {
            task.setId(createId());
        }
        tasks.put(task.getId(), task);
        timeSortedSet.add(task);
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
        if (epics.containsValue(epic)) {
            System.out.println("You can't add " + epic.getName() + " Epic. The Epic is added already");
            return;
        }
        if (epic.getId() == 0) {
            epic.setId(createId());
        }
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
        if (subTasks.containsValue(subTask)) {
            System.out.println("You can't add " + subTask.getName() + " SubTask. The SubTask is added already");
            return;
        }
        if (!isTimeSlotAvailable(subTask)) {
            return;
        }
        Epic epic;
        if (epics.containsKey(subTask.getEpicId())) {
            epic = epics.get(subTask.getEpicId());
            //Эпик добавляет в себя Сабтаски при вызове метода
            epic.addSubTaskInEpic(subTask);
        } else {
            System.out.println("There is not Epic with id " + subTask.getEpicId());
            System.out.println("SubTask " + subTask.getName() + " has not been added");
            return;
        }
        if (subTask.getId() == 0) {
            subTask.setId(createId());
        }

        subTasks.put(subTask.getId(), subTask);
        timeSortedSet.add(subTask);
        System.out.println("SubTask " + subTask.getName() + " has been added");

    }

    @Override
    public List<Task> getAllUsualTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Task> getAllTasks() {
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
            System.out.println("Task " + getTask(id).getName() + " has been deleted.");
            Task task=tasks.get(id);
            tasks.remove(id);
            historyManager.remove(id);
            timeSortedSet.remove(task);
        }
        if (epics.containsKey(id)) {
            System.out.println("Task " + getTask(id).getName() + " has been deleted.");// при удаление эпика все его субТаски также удаляются
            Epic epic = epics.get(id);
            for (SubTask subTask : epic.getSubTasksInEpic()) {
                subTasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
                epic.removeSubTask(subTask);
            }
            epics.remove(id);
            historyManager.remove(id);

        }
        if (subTasks.containsKey(id)) {
            System.out.println("Task " + getTask(id).getName() + " has been deleted.");
            SubTask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(subTask); //новый метод
            subTasks.remove(id);
            historyManager.remove(id);
            timeSortedSet.remove(subTask);
        }

    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        historyManager.removeAllTasks();
        timeSortedSet.clear();
        System.out.println("All tasks have been deleted");
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
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

    @Override
    public Collection<Task> getPrioritizedTasks() {
        return timeSortedSet;
    }

    public boolean isTimeSlotAvailable(Task newTask) {
        boolean isAvalible = true;

        if (newTask.getType().equals(Type.EPIC)) {
            return isAvalible;
        }
        if (newTask.getStartTime() == null) {
            System.out.println("Дата и время не заданны, задачу можно добавить");
            return isAvalible;
        }
        LocalDateTime newTaskStartTime = newTask.getStartTime();
        LocalDateTime newTaskEndTime = newTask.getEndTime();

        for (Task priorityTask : getPrioritizedTasks()) {

            LocalDateTime existTaskStartTime = priorityTask.getStartTime();
            LocalDateTime existTaskEndTime = priorityTask.getEndTime();

            if (existTaskStartTime != null) {

                boolean NewStartIsAfterExEnd = newTaskStartTime.isBefore(existTaskEndTime);
                boolean NewEndIsBeforeExStart = newTaskEndTime.isAfter(existTaskStartTime);

                if (NewEndIsBeforeExStart && NewStartIsAfterExEnd) {
                    isAvalible = false;
                    System.out.println("Вы не можете добавить задачу " + newTask.getName()
                            + ", в это время выполняется задача " + priorityTask.getName());
                }
            }
        }
        return isAvalible;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

