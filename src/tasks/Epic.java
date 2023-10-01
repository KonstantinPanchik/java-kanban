package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<SubTask> subTasksInEpic;

    LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTasksInEpic = new ArrayList<>();
        type = Type.EPIC;
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, status, description);
        this.subTasksInEpic = new ArrayList<>();
        type = Type.EPIC;
    }


    private void solveTimeInEpic() {
        duration = 0;
        startTime = null;
        endTime = null;

        if (getSubTasksInEpic().isEmpty()) {
            return;
        }
        for (SubTask subTask : getSubTasksInEpic()) {

            duration += subTask.getDuration();

            if(subTask.getStartTime()==null){
                continue;
            }
            if (startTime == null || startTime.isAfter(subTask.getStartTime())) {
                  startTime = subTask.getStartTime();
            }

            if (endTime == null || endTime.isBefore(subTask.getEndTime())) {
                this.endTime = subTask.getEndTime();
            }

        }


    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        solveTimeInEpic();
    }

    public void addSubTaskInEpic(SubTask subTask) {
        subTasksInEpic.add(subTask);
        updateStatus();
        solveTimeInEpic();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {

        String result = getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + ","
                + " " + getStartTime();

        return result;
    }
}
