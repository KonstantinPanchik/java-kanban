package tasks;

public class SubTask extends Task {
    protected int epicId;

    Type type = Type.SUBTASK;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, Status status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
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

        String result = getId() + "," + Type.SUBTASK + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getEpicId();
        return result;
    }
}

