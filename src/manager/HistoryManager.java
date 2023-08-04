package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    ArrayList<Task> savedHistory =new ArrayList<>();

   default void add(Task task){
       if (task == null) {
           return;
       }
       savedHistory.add(task);
       if (savedHistory.size()>10){
           savedHistory.remove(0);
       }
   }

   default List<Task> getHistory(){
       return new ArrayList<>(savedHistory);

   }
}
