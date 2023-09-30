package tasks;

import manager.HistoryManager;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TaskBuilder {
    public static Task fromString(String value) {
        if (value == null) {
            return null;
        }
        String[] values = value.split(",");
        if (values.length < 6) {
            return null;
        }
        try {
            Type type = Type.valueOf(values[1]);

            Function<String, LocalDateTime> localDateTimeSupplier = (data) -> {
                if (data.equals("date is not set")) {
                    return null;
                } else {
                    return LocalDateTime.parse(data);
                }
            };
            switch (type) {

                case TASK: {
                    return new Task(Integer.parseInt(values[0]), values[2], Status.valueOf(values[3]), values[4]
                            , localDateTimeSupplier.apply(values[6]), Integer.parseInt(values[7]));
                }

                case EPIC: {
                    return new Epic(Integer.parseInt(values[0]), values[2], Status.valueOf(values[3]), values[4]);
                }
                case SUBTASK: {
                    return new SubTask(Integer.parseInt(values[0])
                            , values[2]
                            , Status.valueOf(values[3])
                            , values[4]
                            , Integer.parseInt(values[5])
                            , localDateTimeSupplier.apply(values[6])
                            , Integer.parseInt(values[7]));
                }
                default: {
                    return null;

                }
            }
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static String toString(Task t) {
        if (t == null) {
            return null;
        }
        Function<LocalDateTime, String> localDateTimeToStr = (localDateTime) -> {
            if (localDateTime == null) {
                return "date is not set";
            } else {
                return localDateTime.toString();
            }
        };
        switch (t.getType()) {

            case TASK: {
                return t.getId() + "," + t.getType() + "," + t.getName() + "," + t.getStatus() + "," + t.getDescription()
                        + "," + " " + "," + localDateTimeToStr.apply(t.getStartTime()) + "," + t.duration;


            }
            case EPIC: {
                Epic e = (Epic) t;
                return e.getId() + "," + e.getType() + "," + e.getName() + "," + e.getStatus() + "," + e.getDescription()
                        + "," + " ";
            }
            case SUBTASK: {
                SubTask s = (SubTask) t;
                String result = s.getId() + "," + s.getType() + "," + s.getName() + "," + s.getStatus() + ","
                        + s.getDescription() + "," + s.getEpicId() + ","
                        + localDateTimeToStr.apply(t.getStartTime()) + ","
                        + t.duration;
                ;
                return result;

            }
            default: {
                return null;
            }
        }

    }

    public static String historyToString(HistoryManager manager) {
        if (manager == null) {
            return null;
        }
        List<Task> tasks = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : tasks) {
            stringBuilder.append(task.getId() + ",");
        }
        if (stringBuilder.length() == 0) {
            return stringBuilder.toString();
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();

        if (value == null) {
            return history;
        }

        String[] values = value.split(",");
        for (String s : values) {

            try {
                history.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return history;
            }
        }
        return history;
    }
}
