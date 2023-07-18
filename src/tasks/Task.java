package tasks;

import manager.Manager;

import java.util.Objects;


public class Task {
    private String name;
    private String description;
    private int id;
    protected String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = Manager.getId();
        this.status = "NEW";
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
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

    public void setStatus(String status) {
        if(status==null){
            return;
        }
        if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
            this.status = status;

        } else {
            System.out.println("The status hasn't been set");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id);
    }

    public String toString() {
        return "Task:\n(Name = " + getName() + ",\n Description = " + getDescription()
                + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")";

    }

}
