package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    final CustomLinkedList<Task> savedHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        remove(id);
        savedHistory.linkedLast(task);
        savedHistory.savedHistoryMap.put(id, savedHistory.getLast());

    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node iterationNode = savedHistory.getFirst();
        while (iterationNode != null) {
            tasks.add(iterationNode.task);
            iterationNode = iterationNode.next;
        }
        return tasks;
    }

    @Override
    public void remove(int id) {
        Node node = savedHistory.savedHistoryMap.remove(id);
        if (node == null) {
            return;
        }
        savedHistory.removeNode(node);
    }

    public static class CustomLinkedList<T extends Task> {
        private final Map<Integer, Node> savedHistoryMap = new HashMap<>();
        private Node first;
        private Node last;

        public Node getFirst() {
            return first;
        }

        public Node getLast() {
            return last;
        }

        void removeNode(Node node) {

            Node oldNose = node.next;
            Node oldTail = node.prev;

            if (oldTail != null) {
                oldTail.next = oldNose;
            } else {
                first = oldNose;
            }
            if (oldNose != null) {
                oldNose.prev = oldTail;
            } else {
                last = oldTail;
            }
        }

        void linkedLast(Task task) {
            Node node = new Node(task, last, null);
            if (first == null) {
                first = node;
            } else {
                last.next = node;
            }
            last = node;
        }
    }

    public static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

}
