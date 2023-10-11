package manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Managers {

    private Managers() {

    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();

    }

    public static TaskManager getDefault() {

        return new InMemoryTaskManager(getDefaultHistory());
    }
    public static TaskManager getDefaultFileBackedTaskManager() {
        File file= Paths.get("save.csv").toFile();
        return FileBackedTasksManager.loadFromFile(file);
    }
    public static TaskManager getDefaultHttpTaskManager() throws IOException {

        return new HttpTaskManager("http://localhost:8077/");
    }


}
