package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.Subject;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Subject<List<Task>> findAll() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
              return entities.stream()
                     .map(TaskEntity::toTask)
                     .collect(Collectors.toList());
              });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public Subject<Task> find(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);


    }

        @Override
        public void save(Task task, Routine routine) {
            taskDao.insert(TaskEntity.fromTask(task));
        }

        @Override
        public void save(List<Task> tasks, Routine routine) {
            var entities = tasks.stream()
                    .map(TaskEntity::fromTask)
                    .collect(Collectors.toList());
            taskDao.insert(entities);
        }



    @Override
    public void remove(int id) {
    }

    @Override
    public List<Task> findTasksByRoutine(int routineId) {
        return taskDao.findTasksByRoutine(routineId).stream()
                .map(TaskEntity::toTask)
                .collect(Collectors.toList());
    }

    @Override
    public int getNextTaskId() {
        return taskDao.count() +1;
    }

    @Override
    public void renameTask(int id, String newName) {
        var task = taskDao.find(id);
        if(task != null){
            task.name = newName;
            taskDao.update(task);
        }
    }

    @Override
    public void delete(Task task) {
        taskDao.delete(task.id());
    }
}
