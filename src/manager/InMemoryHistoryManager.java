package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

     final List<Task> savedHistory = new ArrayList<>();

    static final int MAX_SIZE_OF_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        savedHistory.add(task);
        if (savedHistory.size() > MAX_SIZE_OF_HISTORY) {
            savedHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(savedHistory);

    }
}
