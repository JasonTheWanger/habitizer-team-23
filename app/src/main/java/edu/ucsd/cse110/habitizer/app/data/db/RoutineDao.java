package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

@Dao
public interface RoutineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RoutineEntity routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RoutineEntity> routines);


    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity find(int id);

    @Query("SELECT * FROM routines ORDER BY id")
    List<RoutineEntity> findALL();

    @Transaction
    @Query("SELECT * FROM routines WHERE id= :id")
    LiveData<RoutineEntity> findAsLiveData(int id);

    @Transaction
    @Query("SELECT * FROM routines ORDER BY id")
    LiveData<List<RoutineEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM routines")
    int count();


}
