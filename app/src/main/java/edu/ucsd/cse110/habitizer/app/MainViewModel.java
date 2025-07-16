package edu.ucsd.cse110.habitizer.app;

import androidx.lifecycle.ViewModel;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.ViewModel.*;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private final TaskRepository taskRepository;
    private final RoutineRepository routineRepository;

    private PlainMutableSubject<List<Task>> tasks;
    private final PlainMutableSubject<List<Routine>> routines;

    private final PlainMutableSubject<Routine> currentRoutine;
    private final PlainMutableSubject<Task> currentTask;
    private final PlainMutableSubject<Boolean> isRoutineActive;

    private int IDtobeChecked;

    private PlainMutableSubject<Integer> elapsedTaskTime;

    private PlainMutableSubject<Integer> elapsedTime;
    private boolean isTaskTimerRunning = false;
    private Thread taskTimerThread;
    private boolean isMockMode;

    private final PlainMutableSubject<Integer> routineGoalTime;

    private PlainMutableSubject<Boolean> showRoutineButton;

    private boolean isRoutineTimerRunning = false;
    private Thread routineTimerThread;
    private boolean TaskRunningBeforeStop = false;
    private Integer currentObservedTaskId = null;

    private boolean isProcessingTaskCompletion = false;

    public MainViewModel(TaskRepository taskRepository, RoutineRepository routineRepository){
        this.taskRepository = taskRepository;
        this.routineRepository = routineRepository;

        this.tasks = new PlainMutableSubject<>();
        this.routines = new PlainMutableSubject<>();
        this.currentRoutine = new PlainMutableSubject<>();
        this.currentTask = new PlainMutableSubject<>();

        this.isRoutineActive = new PlainMutableSubject<>();
        this.isRoutineActive.setValue(false);

        this.elapsedTime = new PlainMutableSubject<>();
        this.elapsedTime.setValue(0);
        this.elapsedTaskTime = new PlainMutableSubject<>();
        this.isMockMode = false;

        this.routineGoalTime = new PlainMutableSubject<>();
        this.routineGoalTime.setValue(0);
        this.showRoutineButton = new PlainMutableSubject<>();
        showRoutineButton.setValue(false);
        initializeRoutines();

        routineRepository.findAll().observe(allroutines -> {
            if (allroutines == null) {
                routines.setValue(new ArrayList<>());
                return;
            }

            routines.setValue(allroutines);
        });
    }

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getRoutineRepository());
                    });

    public Subject<Routine> getCurrentRoutine() {
        return currentRoutine;
    }

    public Subject<Task> getCurrentTask() {
        return currentTask;
    }

    public void setRoutine(int routineId) {
        routineRepository.find(routineId).observe(updatedRoutine -> {
            if (updatedRoutine == null) {
                return;
            }

            currentRoutine.setValue(updatedRoutine);

            if(currentTask.getValue() != null){
                currentTask.setValue(new Task(-1, "sb", false, -1, -1, -2));
            }

            List<Task> routineTasks = taskRepository.findTasksByRoutine(routineId);
            tasks.setValue(routineTasks);

        });
        isRoutineActive.setValue(true);

    }

    public PlainMutableSubject<List<Task>> getTaskList() {
        return tasks;
    }
    public PlainMutableSubject<List<Routine>> getRoutineList() {
        return routines;
    }

    public void checkOffTask() {
            Task task = currentTask.getValue();

            Integer taskTime = elapsedTaskTime.getValue() != null ? elapsedTaskTime.getValue() : 0;

            task.updateElapsedTime(taskTime);

            var updatedTask = task.markCompleted();
            taskRepository.save(updatedTask, currentRoutine.getValue());
            tasks.setValue(taskRepository.findTasksByRoutine(currentRoutine.getValue().id()));
            currentTask.setValue(updatedTask);
            checkRoutineCompletion();

    }


    public void setCurrentTask(int taskId) {
        if (isRoutineActive.getValue() == null || !isRoutineActive.getValue()) {
            return;
        }

        if (IDtobeChecked == taskId && currentTask.getValue() != null &&
                currentTask.getValue().id() == taskId) {
            return;
        }

        IDtobeChecked = taskId;

        Task task = taskRepository.find(taskId).getValue();
        if (task != null) {
            currentTask.setValue(task);
        } else {
            taskRepository.find(taskId).observe(loadedTask -> {
                if (loadedTask == null) {
                    return;
                }

                if (taskId == IDtobeChecked) {
                    currentTask.setValue(loadedTask);
                    if(currentTask.getValue().isCompleted() == true){
                        stopTaskTimer();
                        setElapsedTaskTime(currentTask.getValue().elapsedTime());
                        return;
                    }else{
                        elapsedTaskTime.setValue(0);
                        startTaskTimer();
                    }

                }
            });
        }
    }

    public void startTaskTimer() {
        if (isTaskTimerRunning) return;

        isTaskTimerRunning = true;
        if(elapsedTaskTime == null){
            elapsedTaskTime.setValue(0);
        }


        taskTimerThread = new Thread(() -> {
            try {
                while (isTaskTimerRunning) {
                    Thread.sleep(1000);

                    var current = currentTask.getValue();
                    if (current != null && current.id() == IDtobeChecked) {
                        elapsedTaskTime.setValue(elapsedTaskTime.getValue() + 1);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        taskTimerThread.start();
    }


    public void stopTaskTimer() {
        Integer taskTime = elapsedTaskTime.getValue() != null ? elapsedTaskTime.getValue() : 0;
        isTaskTimerRunning = false;
        if (taskTimerThread != null) {
            taskTimerThread.interrupt();
            taskTimerThread = null;
        }
    }


    public boolean isTaskTimerRunning() {
        return isTaskTimerRunning;
    }
    public Subject<Integer> getElapsedTime(){ return elapsedTime;}

    public void checkRoutineCompletion() {
        var routine = currentRoutine.getValue();
        if (routine == null) return;

        boolean allTasksCompleted = tasks.getValue() != null &&
                tasks.getValue().stream().allMatch(Task::isCompleted);

        if (allTasksCompleted) {
            endRoutine();
        }
    }

    public void endRoutine() {
        if(currentRoutine.getValue() == null || currentTask.getValue() == null){
            return;
        }
        if (!Boolean.TRUE.equals(isRoutineActive.getValue())) return;

        isRoutineActive.setValue(false);
        if(!currentTask.getValue().isCompleted()) {
            checkOffTask();
        }

    }

    public void resetRoutine() {
        if(currentRoutine.getValue() == null || currentTask.getValue() == null){
            return;
        }
        isMockMode = false;
        isRoutineActive.setValue(false);
        int routineId = currentRoutine.getValue().id();
        Routine routine = currentRoutine.getValue();
        List<Task> routineTasks = taskRepository.findTasksByRoutine(routineId);




        List<Task> resetTasks = new ArrayList<>();
        for (Task task : routineTasks) {
            task.updateElapsedTime(0);
            Task resetTask = task.markInCompleted();

            taskRepository.save(resetTask, routine);
            resetTasks.add(resetTask);

        }

        tasks.setValue(resetTasks);

        elapsedTaskTime.setValue(0);
        elapsedTime.setValue(0);
    }
    public Subject<Boolean> getRoutineStatus() {
        return isRoutineActive;
    }
    public Subject<Integer> getElapsedTaskTime() {
        return elapsedTaskTime;
    }
    public void setElapsedTaskTime(int time) {
        this.elapsedTaskTime.setValue(time);
    }
    public boolean isMockModeEnabled() {
        return isMockMode;
    }

    public void toggleMockMode(boolean enable) {
        if (currentRoutine.getValue() == null) {
            return;
        }
        this.isMockMode = enable;

        if (enable) {
            TaskRunningBeforeStop = isTaskTimerRunning;
            stopTaskTimer();
            stopRoutineTimer();
        } else {
            if (Boolean.TRUE.equals(isRoutineActive.getValue())) {
                startRoutineTimer();
            }
            var currTask = currentTask.getValue();
            if(currTask != null && !currTask.isCompleted() && TaskRunningBeforeStop) {
                startTaskTimer();
            }
        }
    }

    public void setMockElapsedTime(int seconds) {
        if (isMockMode) {
            elapsedTaskTime.setValue(seconds);
        }
    }

    public void advanceMockTime() {
        if (currentRoutine.getValue() == null){
            return;
        }
        int routineElapsed = elapsedTime.getValue() != null ? elapsedTime.getValue() : 0;
        elapsedTime.setValue(routineElapsed + 15);
        elapsedTime.setValue(elapsedTime.getValue());
    }

    public void setRoutineGoalTime(int minutes) {
        routineGoalTime.setValue(minutes);
    }
    public Subject<Integer> getRoutineGoalTime() {
        return routineGoalTime;
    }

    public void initializeRoutines() {

        if(routineRepository.findAll().getValue() == null){
           return;
        }
        routines.setValue(routineRepository.findAll().getValue());


    }

    public void addTaskToRoutine(String taskName) {
        var routine = currentRoutine.getValue();
        if (routine == null) return;

        int newTaskId = taskRepository.getNextTaskId();
        Task newTask = new Task(null, taskName, false, routine.id(), 0, newTaskId);
        taskRepository.save(newTask, routine);


        tasks.setValue(taskRepository.findTasksByRoutine(routine.id()));
    }

    public void renameTask(int taskId, String newName) {
        var task = taskRepository.find(taskId).getValue();
        if (task != null) {
            var updatedTask = task.rename(newName);
            taskRepository.save(updatedTask, currentRoutine.getValue());
        }

        tasks.setValue(taskRepository.findTasksByRoutine(currentRoutine.getValue().id()));
    }

    public void addRoutine(Routine routine) {
        if (routine == null) return;

        int newRoutineId = routineRepository.getNextRoutineId();
        Routine routineWithId = routine.withId(newRoutineId);

        routineRepository.save(routineWithId);

        List<Routine> currentRoutines = new ArrayList<>(routines.getValue());
        currentRoutines.add(routineWithId);
        routines.setValue(currentRoutines);
    }

    public void deleteTask(Task task) {
        if (task == null) {
            return;
        }

        var routine = currentRoutine.getValue();
        if (routine == null) {
            return;
        }


        taskRepository.delete(task);

        List<Task> updatedTasks = taskRepository.findTasksByRoutine(routine.id());


        tasks.setValue(updatedTasks);

        if (currentTask.getValue() != null && currentTask.getValue().id().equals(task.id())) {
            currentTask.setValue(null);
        }
    }

    public void startRoutineTimer() {
        if (isRoutineTimerRunning) return;

        isRoutineTimerRunning = true;
        routineTimerThread = new Thread(() -> {
            try {
                while (isRoutineTimerRunning) {
                    Thread.sleep(1000);
                    elapsedTime.setValue(elapsedTime.getValue() + 1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        routineTimerThread.start();
    }

    public void stopRoutineTimer() {


        if(!isMockMode){
            Integer elapsed = elapsedTime.getValue() != null ? elapsedTime.getValue() : 0;
            elapsedTime.setValue(elapsed);
        }

        isRoutineTimerRunning = false;
        if (routineTimerThread != null) {
            routineTimerThread.interrupt();
            routineTimerThread = null;
        }
    }

    public Subject<Integer> getElapsedRoutineTime() {
        return elapsedTime;
    }

    public boolean isRoutineTimerRunning() {
        return isRoutineTimerRunning;
    }

    public Subject<Boolean> shownRoutine(){
        return showRoutineButton;
    }

    public void moveTaskUp(Task task) {
        List<Task> currentTasks = tasks.getValue();
        if (task == null || currentTasks == null) return;

        int index = currentTasks.indexOf(task);
        if (index > 0) {
            List<Task> updatedTasks = new ArrayList<>(currentTasks);

            Task currentTask = updatedTasks.get(index);
            Task aboveTask = updatedTasks.get(index - 1);

            Collections.swap(updatedTasks, index, index - 1);

            int currentOrder = currentTask.orderIndex();
            int aboveOrder = aboveTask.orderIndex();

            Task updatedCurrentTask = currentTask.withOrderIndex(aboveOrder);
            Task updatedAboveTask = aboveTask.withOrderIndex(currentOrder);

            updatedTasks.set(index - 1, updatedCurrentTask);
            updatedTasks.set(index, updatedAboveTask);

            Routine routine = currentRoutine.getValue();
            if (routine != null) {
                taskRepository.save(updatedCurrentTask, routine);
                taskRepository.save(updatedAboveTask, routine);
            }

            tasks.setValue(updatedTasks);
        }
    }

    public void moveTaskDown(Task task) {
        List<Task> currentTasks = tasks.getValue();
        if (task == null || currentTasks == null) return;

        int index = currentTasks.indexOf(task);
        if (index >= 0 && index < currentTasks.size() - 1) {
            List<Task> updatedTasks = new ArrayList<>(currentTasks);

            Task currentTask = updatedTasks.get(index);
            Task belowTask = updatedTasks.get(index + 1);

            Collections.swap(updatedTasks, index, index + 1);

            int currentOrder = currentTask.orderIndex();
            int belowOrder = belowTask.orderIndex();

            Task updatedCurrentTask = currentTask.withOrderIndex(belowOrder);
            Task updatedBelowTask = belowTask.withOrderIndex(currentOrder);

            updatedTasks.set(index, updatedBelowTask);
            updatedTasks.set(index + 1, updatedCurrentTask);

            Routine routine = currentRoutine.getValue();
            if (routine != null) {
                taskRepository.save(updatedCurrentTask, routine);
                taskRepository.save(updatedBelowTask, routine);
            }

            tasks.setValue(updatedTasks);
        }
    }


    public void setElapsedTime(int seconds) {
        this.elapsedTime.setValue(seconds);
    }
}
