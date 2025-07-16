package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoutineEntity.class, TaskEntity.class}, version = 2)
public abstract class HabitizerDatabase extends RoomDatabase{
    public abstract RoutineDao routineDao();
    public abstract TaskDao taskDao();
}