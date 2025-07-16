package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface RoutineRepository {
    Subject<List<Routine>> findAll();

    Subject<Routine> find(int id);

    void save(Routine routine);

    void save(List<Routine> routines);

    void remove(int id);

    int getNextRoutineId();
}
