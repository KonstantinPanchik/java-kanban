package tasks;

import java.util.ArrayList;

public class SubTask extends Task {
    Epic epic;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        String EpicName = null;
        if (!(epic == null)) {
            EpicName = epic.getName();
        }
        return "Epic name = " + EpicName + ",\n SubTask:\n(Name = " + getName() + ",\n Description = "
                + getDescription() + ",\n Id = " + getId() + ",\n Status = " + getStatus() + ")\n";

    }

    protected void setEpicOnes(Epic epic) {
        this.epic = epic;
    }

    public void setEpic(Epic epic) {
        if(epic==null){
            return;
        }
        this.epic = epic;
        epic.addSubTaskOnes(this);
    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status);
        if (!(epic == null)) {
            epic.updateStatus();
        }
    }
}

