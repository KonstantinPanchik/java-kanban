import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    static Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    public void setEpic() {
        epic = new Epic("name", "description");
        subTask1 = new SubTask("subtask1", "descriptionOfSubtask", epic.getId());
        subTask2 = new SubTask("subtask2", "descriptionOfSubtask", epic.getId());
        subTask3 = new SubTask("subtask3", "descriptionOfSubtask", epic.getId());
        epic.addSubTaskInEpic(subTask1);
        epic.addSubTaskInEpic(subTask2);
        epic.addSubTaskInEpic(subTask3);
    }

    @Test
    public void shouldNewIfSubtasksEmpty() {
        epic.removeSubTask(subTask1);
        epic.removeSubTask(subTask3);
        epic.removeSubTask(subTask2);
        assertTrue(epic.getSubTasksInEpic().isEmpty());
        Assertions.assertEquals(Status.NEW, epic.getStatus());

    }

    @Test
    public void shouldNewSubtaskNew() {
        assertEquals(epic.getSubTasksInEpic(), List.of(subTask1, subTask2, subTask3));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldInProgresSubtaskDifferent() {
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        epic.updateStatus();
        assertEquals(epic.getSubTasksInEpic(), List.of(subTask1, subTask2, subTask3));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldDoneSubtaskDone() {

        subTask1.setStatus(Status.DONE);

        subTask2.setStatus(Status.DONE);

        subTask3.setStatus(Status.DONE);
        epic.updateStatus();
        assertEquals(epic.getSubTasksInEpic(), List.of(subTask1, subTask2, subTask3));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldInProgresSubtaskInProgress() {

        subTask1.setStatus(Status.IN_PROGRESS);

        subTask2.setStatus(Status.IN_PROGRESS);

        subTask3.setStatus(Status.IN_PROGRESS);
        epic.updateStatus();
        assertEquals(epic.getSubTasksInEpic(), List.of(subTask1, subTask2, subTask3));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}