package edu.ucsd.cse110.habitizer.lib.domain;



import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

import edu.ucsd.cse110.observables.Subject;
public class SimpleTaskRepository implements TaskRepository {

    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Subject<List<Task>> findAll(){
        return dataSource.getAllTasksSubject();
    }

    @Override
    public Subject<Task> find(int id){
        return dataSource.getTaskSubject(id);
    }

    @Override
    public void save(Task task, Routine routine){
        dataSource.putTask(task, routine);
    }

    @Override
    public void save(List<Task> tasks, Routine routine){
        dataSource.putTasks(tasks, routine);
    }

    @Override
    public void remove(int id){
        dataSource.removeTask(id);
    }


    @Override
    public List<Task> findTasksByRoutine(int routineId) {
        return dataSource.getTasksByRoutine(routineId);
    }


    @Override
    public int getNextTaskId() {
        List<Task> allTasks = dataSource.getAllTasksSubject().getValue();
        if (allTasks == null || allTasks.isEmpty()) {
            return 0;
        }

        return allTasks.stream().mapToInt(Task::id).max().orElse(0) + 1;
    }


    @Override
    public void renameTask(int id, String newName) {
        dataSource.renameTask(id, newName);
    }


    @Override
    public void delete(Task task) {
        if (task == null) return;
        dataSource.removeTask(task.id());
    }
}
