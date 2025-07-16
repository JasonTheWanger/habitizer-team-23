package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Routine {
    private final @Nullable Integer id;
    private final @NonNull String name;

    private @NonNull List<Task> tasks;


    public Routine(@Nullable Integer id, @NonNull String name, @NonNull List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>(tasks);
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String name() {
        return name;
    }

    public @NonNull List<Task> tasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }


    public void updateTask(int taskID, Task task){
        tasks.set(taskID, task);
    }

    public Routine withId(int id) {
        return new Routine(id, this.name, this.tasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routine routine = (Routine) o;
        return Objects.equals(id, routine.id) &&
                Objects.equals(name, routine.name) &&
                Objects.equals(tasks, routine.tasks);
    }



    public void replaceTask(Task newTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id() == newTask.id()) {
                tasks.set(i, newTask);
                return;
            }
        }
        tasks.add(newTask);
    }

    public void replaceTasks(List<Task> tasks){
        tasks.forEach(task -> {
            replaceTask(task);
        });
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, tasks);
    }
}
