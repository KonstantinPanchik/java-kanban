package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    final CustomLinkedList<Task> savedHistory = new CustomLinkedList<>();
    private final Map<Integer, Node> savedHistoryMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        if (savedHistoryMap.containsKey(id)) {
            remove(id);
        }
        savedHistoryMap.put(id, savedHistory.linkedLast(task));

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
        Node node = savedHistoryMap.remove(id);
        if (node == null) {
            return;
        }
        savedHistory.removeNode(node);
    }

    @Override
    public void removeAllTasks() {

     for (int id:savedHistoryMap.keySet()){
         savedHistory.removeNode(savedHistoryMap.get(id));
     }
        savedHistoryMap.clear();
    }

    public static class CustomLinkedList<T extends Task> {

        private Node first;
        private Node last;

        public Node getFirst() {
            return first;
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

        Node linkedLast(Task task) {
            Node node = new Node(task, last, null);
            if (first == null) {
                first = node;
            } else {
                last.next = node;
            }
            last = node;
            return node;
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
