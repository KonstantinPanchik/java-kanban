package tasks;


import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class Task {
    private String name;
    private String description;
    private int id;

    protected long duration;

    protected LocalDateTime startTime;



    protected Status status;

    private Type type = Type.TASK;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }
    public Task(String name, String description,LocalDateTime startTime,int duration) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.startTime=startTime;
        this.duration=duration;
    }
    public Task(int id, String name, Status status, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;

    }
    public Task(int id, String name, Status status, String description,LocalDateTime startTime,int duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration=duration;
        this.startTime=startTime;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {

        String result = getId() + "," + Type.TASK + "," + getName() + "," + getStatus() + "," + getDescription() + ","
                + " "+getStartTime();
        return result;

    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getEndTime(){
        if (startTime==null){
            return null;
        }
        LocalDateTime endTime=startTime.plus(duration, ChronoUnit.MINUTES);
        return endTime;
    }
    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
