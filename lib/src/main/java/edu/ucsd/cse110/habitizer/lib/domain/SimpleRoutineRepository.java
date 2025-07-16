package edu.ucsd.cse110.habitizer.lib.domain;



import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

import edu.ucsd.cse110.observables.Subject;

public class SimpleRoutineRepository implements RoutineRepository {
    private final InMemoryDataSource dataSource;

    public SimpleRoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Subject<List<Routine>> findAll() {
        return dataSource.getAllRoutinesSubject();
    }

    @Override
    public Subject<Routine> find(int id) {
        return dataSource.getRoutineSubject(id);
    }

    @Override
    public void save(Routine routine){
        dataSource.putRoutine(routine);
    }
    @Override
    public void save(List<Routine> routines){
        dataSource.putRoutines(routines);
    }

    @Override
    public void remove(int id){
        dataSource.removeRoutine(id);
    }

    @Override
    public int getNextRoutineId() {
        List<Routine> routines = findAll().getValue();
        if (routines == null || routines.isEmpty()) {
            return 0;
        }

        int maxId = routines.stream()
                .mapToInt(routine -> routine.id() != null ? routine.id() : 0)
                .max()
                .orElse(-1);

        return maxId + 1;
    }
}