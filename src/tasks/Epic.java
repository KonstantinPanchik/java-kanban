package tasks;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<SubTask> subTasksInEpic;
    Type type = Type.EPIC;


    public Epic(String name, String description) {
        super(name, description);
        subTasksInEpic = new ArrayList<>();
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, status, description);
        this.subTasksInEpic = new ArrayList<>();
    }

    @Override
    public void setStatus(Status status) {

        System.out.println("You can not change Epic's status by yourself");
    }

    public void updateStatus() {
        Status status = Status.NEW;
        if (!subTasksInEpic.isEmpty()) {
            status = subTasksInEpic.get(0).getStatus();
            for (int i = 1; i < subTasksInEpic.size(); i++) {
                if (status != subTasksInEpic.get(i).getStatus()) {
                    this.status = Status.IN_PROGRESS;
                    return;
                }
            }
        }
        this.status = status;
    }

    public List<SubTask> getSubTasksInEpic() {
        return new ArrayList<>(subTasksInEpic);
    }

    public void removeSubTask(SubTask subTask) {
        subTasksInEpic.remove(subTask);
        updateStatus();
    }

    public void addSubTaskInEpic(SubTask subTask) {
        subTasksInEpic.add(subTask);
        updateStatus();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {

        String result = getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," + " ";

        return result;
    }
}
