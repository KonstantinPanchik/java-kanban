# java-kanban
Repository for homework project.

--- 
## Трекер задач
  В данном проекте реализован бекэнд для **Трекера задач**.
Реализованны 3 типа задач:
* **__Task__** - Класс для обычных задач.
* **__Epic__** - Класс для задач в которых могут быть подзадачи(Эпик).
* **__SubTask__** - Класс для подзадач.

Каждый обьект класса Epic знает  SubTask включенные в него, и каждый SubTask знает свой Epic.
Храниние задач реализовно в HashMap где ключем является Id задачи и обьект задачи:
 ```java

public class Manager {

    HashMap<Integer, Task> tasks;
    HashMap<Integer, SubTask> subTasks;
    HashMap<Integer, Epic> epics;
}
```
--- 
## Менеджер
В проекте реализован класс Manager для работы с задачами, в нем реализованы следующие методы:

1. Получение списка всех задач.
Пример кода получения списка всех обычных задач:
```java

public class Manager {
    
    public ArrayList<Task> getAllUsualTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
            for (Task task : tasks.values()) {
                listOfTasks.add(task);
            }
    return listOfTasks;
    }
}
```
2. Удаление всех задач.
```java
public void removeTask(int id)
```
3. Получение по идентификатору.
```java
 public void removeTask(int id)
```
4. Создание. Сам объект должен передаваться в качестве параметра.
```java
 public void setTasks(Task... tasks)
```
5. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
```java
 public void updateTask(Task task)
```
6. Удаление по идентификатору.
```java
public void removeTaskById(int id)
```
### Дополнительные методы:
* Получение списка всех подзадач определённого эпика.
```java
public ArrayList<SubTask> getSubTasksOfEpic(Epic epic)
```

Управление статусами осуществляется по следующему правилу:
  Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
  По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.

---

Для эпиков:
* если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
* если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
* во всех остальных случаях статус должен быть IN_PROGRESS.

Пример методов setStatus() и updateStatus() в классе Epic:
```java
    @Override
    public void setStatus(String status) {
        System.out.println("You can not change Epic's status by yourself");
    }

    public void updateStatus() {
        String status = "NEW";
        if (!subTasksInEpic.isEmpty()) {
            int i = 0;
            String statusArray[] = new String[subTasksInEpic.size()];
            for (SubTask subTask : subTasksInEpic.values()) {
                statusArray[i++] = subTask.status;
            }
        status = statusArray[0];
            for (int j = 1; j < statusArray.length; j++) {
                if (!status.equals(statusArray[j])) {
                    status = "IN_PROGRESS";
                    break;
                }
            }
        }
        this.status = status;
    }
```
