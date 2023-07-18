package tasks;

import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, SubTask> subTasksInEpic;

    public Epic(String name, String description) {
        super(name, description);
        subTasksInEpic = new HashMap<>();
    }

    @Override
    public void setStatus(String status) {
        System.out.println("You can not change Epic's status by yourself");
    }

    public void updateStatus() {
        String status = "NEW";
        if (!subTasksInEpic.isEmpty()) {
            int i = 0;
            String statusArray[] = new String[subTasksInEpic.size()];
            for (SubTask subTask : subTasksInEpic.values()) {
                statusArray[i++] = subTask.status;
            }
            status = statusArray[0];
            for (int j = 1; j < statusArray.length; j++) {
                if (!status.equals(statusArray[j])) {
                    status = "IN_PROGRESS";
                    break;
                }
            }
        }
        this.status = status;
    }

    public HashMap<Integer, SubTask> getSubTasksInEpic() {
        return subTasksInEpic;
    }

    protected void addSubTaskOnes(SubTask subTask) {
        subTasksInEpic.put(subTask.getId(), subTask);
        updateStatus();
    }

    public void addSubTask(SubTask... subTasks) {
        for (SubTask subTask : subTasks) {
            if (subTask == null) {
                continue;
            }
            subTasksInEpic.put(subTask.getId(), subTask);
            subTask.setEpicOnes(this);
        }
        updateStatus();
    }

    @Override
    public String toString() {

        String result = "Epic:\n(Name = " + getName() + ",\n Description = " + getDescription()
                + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")\n Subtasks in this Epic:\n{\n";
        for (SubTask subTask : subTasksInEpic.values()) {
            result = result + subTask.toString();

        }
        result = result + " }\n";
        return result;
    }
}
