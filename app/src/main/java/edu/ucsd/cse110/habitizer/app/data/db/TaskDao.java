package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> tasks);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY id")
    List<TaskEntity> findALL();

    @Query("SELECT * FROM tasks WHERE id= :id")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks ORDER BY id")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Update
    void update(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE routine_id = :routineId ORDER BY order_index")
    List<TaskEntity> findTasksByRoutine(int routineId);
}
