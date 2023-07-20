package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<SubTask> subTasksInEpic;

    public Epic(String name, String description) {
        super(name, description);
        subTasksInEpic = new ArrayList<>();
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
    public ArrayList <SubTask> getSubTasksInEpic() {
        return subTasksInEpic;
    }

    public void addSubTaskInEpic(SubTask subTask) {
        subTasksInEpic.add(subTask);
        updateStatus();
    }

    @Override
    public String toString() {

        String result = "Epic:\n(Name = " + getName() + ",\n Description = " + getDescription()
                + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")\n Subtasks in this Epic:\n{\n";
        for (SubTask subTask : subTasksInEpic) {
            result = result + subTask.toString();

        }
        result = result + " }\n";
        return result;
    }
}
