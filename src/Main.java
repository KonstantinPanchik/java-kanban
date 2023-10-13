import manager.InMemoryTaskManager;
import manager.Managers;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().startServer();
    }

}
