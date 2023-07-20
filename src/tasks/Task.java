package tasks;

import java.util.Objects;


public class Task {
    private String name;
    private String description;
    private int id;
    protected Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;;
        status = Status.NEW;
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
        return Objects.hash(name, description, id,status);
    }

    @Override
    public String toString() {
        return "Task:\n(Name = " + getName() + ",\n Description = " + getDescription()
                + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")";

    }

}
