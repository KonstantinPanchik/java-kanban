package tasks;

public class SubTask extends Task {
   protected int epicId;

    public SubTask(String name, String description,int epicId) {
        super(name, description);
        this.epicId=epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    @Override
    public String toString() {
        return "Epic id = " + getEpicId() + ",\n SubTask:\n(Name = " + getName() + ",\n Description = "
                + getDescription() + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")\n";

    }
}

