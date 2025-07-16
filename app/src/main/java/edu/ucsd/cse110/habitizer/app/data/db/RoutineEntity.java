package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

@Entity(tableName = "routines")
public class RoutineEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    public RoutineEntity(@NonNull String name) {
        this.name = name;
    }

    public static RoutineEntity fromRoutine(@NonNull Routine routine){
        var i = new RoutineEntity(routine.name());
        i.id = routine.id();
        return i;
    }

    public @NonNull Routine toRoutine(){
        return new Routine(id,name, new ArrayList<>());
    }


}