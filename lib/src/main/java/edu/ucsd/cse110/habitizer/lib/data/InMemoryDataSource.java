package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class InMemoryDataSource {
    private int nextTaskID = 0;
    private int nextRoutineID = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, PlainMutableSubject<Task>> taskSubjects = new HashMap<>();
    private final PlainMutableSubject<List<Task>> allTasksSubject = new PlainMutableSubject<>();
    private final Map<Integer, Routine> routines = new HashMap<>();
    private final Map<Integer, PlainMutableSubject<Routine>> routineSubjects = new HashMap<>();
    private final PlainMutableSubject<List<Routine>> allRoutinesSubject = new PlainMutableSubject<>();

    public InMemoryDataSource() {}

    public static final List<Task> DEFAULT_MORNING_TASKS = List.of(
            new Task(0, "Brush Teeth", false, 0, 0, 0),
            new Task(1, "Make Bed", false, 0, 0, 1),
            new Task(2, "Stretch", false, 0, 0, 2),
            new Task(3, "Read a Book", false, 0, 0,3),
            new Task(4, "Plan the Day", false, 0, 0,4)
    );

    public static final List<Task> DEFAULT_EVENING_TASKS = List.of(
            new Task(5, "Do Homework", false, 1, 0, 5),
            new Task(6, "Make Dinner", false, 1, 0, 6),
            new Task(7, "Check Schedule", false, 1, 0,7),
            new Task(8, "Take a Shower", false, 1, 0,8),
            new Task(9, "Go to Bed", false, 1, 0,9)
    );


    public static final List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine(0, "Morning Routine", new ArrayList<>()),
            new Routine(1, "Evening Routine", new ArrayList<>())
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();

        data.putRoutines(DEFAULT_ROUTINES);

        var morningRoutine = data.getRoutine(0);
        var eveningRoutine = data.getRoutine(1);
        if (morningRoutine != null) {
            data.putTasks(DEFAULT_MORNING_TASKS, morningRoutine);
        }

        if (eveningRoutine != null) {
            data.putTasks(DEFAULT_EVENING_TASKS, eveningRoutine);
        }

        return data;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subject<Task> getTaskSubject(int id) {
        if (!taskSubjects.containsKey(id)) {
            PlainMutableSubject<Task> subject = new PlainMutableSubject<>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public Subject<List<Task>> getAllTasksSubject() {
        return allTasksSubject;
    }

    public void putTask(Task task, Routine routine) {
        var fixedTask = preInsertTask(task);
        if (!tasks.containsKey(fixedTask.id())){
            routine.addTask(task);
        }

        else {
            routine.replaceTask(fixedTask);
        }

        tasks.put(fixedTask.id(), fixedTask);
        if (taskSubjects.containsKey(fixedTask.id())) {
            taskSubjects.get(fixedTask.id()).setValue(fixedTask);
        }
        allTasksSubject.setValue(getTasks());
    }

    public void putTasks(List<Task> taskList, Routine routine) {
        taskList.forEach(task -> putTask(task, routine));
    }

    public void removeTask(int id) {
        tasks.remove(id);
        taskSubjects.remove(id);
        allTasksSubject.setValue(getTasks());
    }

    private Task preInsertTask(Task task) {
        var id = task.id();
        if (id == null) {
            task = task.withId(nextTaskID++);
        } else if (id >= nextTaskID) {
            nextTaskID = id + 1;
        }
        return task;
    }

    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public Subject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            PlainMutableSubject<Routine> subject = new PlainMutableSubject<>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public void putRoutine(Routine routine) {
        var fixedRoutine = preInsertRoutine(routine);
        routines.put(fixedRoutine.id(), fixedRoutine);
        if (routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putRoutines(List<Routine> routineList) {
        routineList.forEach(this::putRoutine);
    }

    private Routine preInsertRoutine(Routine routine) {
        var id = routine.id();
        if (id == null) {
            routine = routine.withId(nextRoutineID++);
        } else if (id >= nextRoutineID) {
            nextRoutineID = id + 1;
        }
        return routine;
    }

    public void removeRoutine(int id){
        routines.remove(id);
        routineSubjects.remove(id);
        allRoutinesSubject.setValue(getRoutines());
    }


    public List<Task> getTasksByRoutine(int routineId) {
        return tasks.values().stream()
                .filter(task -> task.routineBelongTo() == routineId)
                .collect(Collectors.toList());
    }


    public void renameTask(int id, String newName) {
        if (!tasks.containsKey(id)) return;

        Task updatedTask = tasks.get(id).rename(newName);
        tasks.put(id, updatedTask);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(updatedTask);
        }
        allTasksSubject.setValue(getTasks());
    }

}
