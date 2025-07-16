package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;


public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private RoutineRepository routineRepository;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                HabitizerDatabase.class,
                "habitizer-database"
        )
                .allowMainThreadQueries()
                .build();

        this.routineRepository = new RoomRoutineRepository(database.routineDao());
        this.taskRepository = new RoomTaskRepository(database.taskDao());


        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if(isFirstRun && database.routineDao().count() == 0){
            routineRepository.save(InMemoryDataSource.DEFAULT_ROUTINES);
            taskRepository.save(InMemoryDataSource.DEFAULT_MORNING_TASKS,routineRepository.find(0).getValue());
            taskRepository.save(InMemoryDataSource.DEFAULT_EVENING_TASKS,routineRepository.find(1).getValue());
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
        public TaskRepository getTaskRepository () {
            return taskRepository;
        }

        public RoutineRepository getRoutineRepository () {
            return routineRepository;
        }


}
