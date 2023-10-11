package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class SubTask extends Task {
    protected int epicId;



    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        type = Type.SUBTASK;
    }
    public SubTask(String name, String description, int epicId,LocalDateTime startTime,int duration) {
        super(name, description,startTime,duration);
        this.epicId = epicId;
        type = Type.SUBTASK;
    }

    public SubTask(int id
            , String name
            , Status status
            , String description
            , int epicId
            , LocalDateTime startTime
            ,int duration) {
        super(id, name, status, description,startTime,duration);
        this.epicId = epicId;
        type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {




        String result = getId() + "," + Type.SUBTASK + "," + getName() + "," + getStatus() + "," + getDescription()
                + "," + getEpicId()+" "+getStartTime();
        return result;
    }
}

