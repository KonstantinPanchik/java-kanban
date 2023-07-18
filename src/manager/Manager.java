package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, SubTask> subTasks;
    HashMap<Integer, Epic> epics;
    static int createdId;

    public Manager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public static int getId() {
        return ++createdId;
    }

    public void setTasks(Task... tasks) {
        for (Task task : tasks) {
            if(task==null){
                continue;
            }
            if (task.getClass() == Task.class) {
                this.tasks.put(task.getId(), task);
            }
            if (task.getClass() == Epic.class) { // при сохраненение эпика происходит сохранение и субтасков
                Epic epic = (Epic) task;
                for (SubTask subTask : (epic.getSubTasksInEpic().values())) {
                    subTasks.put(subTask.getId(), subTask);
                }
                epics.put(epic.getId(), epic);
            }
            if (task.getClass() == SubTask.class) {
                // если у сабТаска нет эпика сохраниться сабТаск без эпика,если есть сохранится и эпик
                SubTask subTask = (SubTask) task;
                if (!(subTask.getEpic() == null)) {
                    setTasks(subTask.getEpic());
                } else {
                    subTasks.put(subTask.getId(), subTask);
                }
            }
        }
    }

    public ArrayList<Task> getAllUsualTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            listOfTasks.add(task);
        }
        return listOfTasks;
    }

    public ArrayList<Epic> getAllEpicTasks() {
        ArrayList<Epic> listOfEpics = new ArrayList<>();
        for (Epic task : epics.values()) {
            listOfEpics.add(task);
        }
        return listOfEpics;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> listOfSubTask = new ArrayList<>();
        for (SubTask task : subTasks.values()) {
            listOfSubTask.add(task);
        }
        return listOfSubTask;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = new ArrayList<>(getAllUsualTasks());
        list.addAll(getAllEpicTasks());
        list.addAll(getAllSubTasks());
        return list;
    }

    public Task getTask(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            return;
        }
        if (epics.containsKey(id)) {  // при удаление эпика все его субТаски также удаляются
            Epic epic = epics.get(id);
            for (Integer key : epic.getSubTasksInEpic().keySet()) {
                subTasks.remove(key);

            }
            epics.remove(id);
            return;
        }
        if (subTasks.containsKey(id)) {
            for (Epic epic : epics.values()) {
                epic.getSubTasksInEpic().remove(id);
                epic.updateStatus();
            }
            subTasks.remove(id);

        }
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        if(epic==null){
            return new ArrayList<>();
        }
        ArrayList<SubTask> listOfSubTask = new ArrayList<>();
        for (SubTask task : epic.getSubTasksInEpic().values()) {
            listOfSubTask.add(task);
        }
        return listOfSubTask;
    }

    public void updateTask(Task task) {
        if(task==null){
            return;
        }
        if (tasks.containsKey(task.getId())
                || subTasks.containsKey(task.getId())
                || epics.containsKey(task.getId())) {
            setTasks(task);
        }
    }

}

