package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface TaskRepository {
    Subject<List<Task>> findAll();

    Subject<Task> find(int id);

    void save(Task task, Routine routine);

    void save(List<Task> tasks, Routine routine);

    void remove(int id);

    List<Task> findTasksByRoutine(int routineId);

    int getNextTaskId();

    void renameTask(int id, String newName);

    void delete(Task task);
}
