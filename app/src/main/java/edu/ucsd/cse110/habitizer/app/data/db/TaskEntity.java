package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "completed")
    public boolean isCompleted;

    @ColumnInfo(name = "routine_id")
    public int routineBelongTo;

    @ColumnInfo(name = "elapsed_time")
    public int elapsedTime;

    @ColumnInfo(name = "order_index")
    public int orderIndex;
    public TaskEntity(@NonNull String name, boolean isCompleted, int routineBelongTo, int elapsedTime, int orderIndex) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.routineBelongTo = routineBelongTo;
        this.elapsedTime = elapsedTime;
        this.orderIndex = orderIndex;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var v = new TaskEntity(task.name(),task.isCompleted(),task.routineBelongTo(),task.elapsedTime(),task.orderIndex());
        v.id = task.id();
        return v;
    }
    public @NonNull Task toTask() {
        return new Task(id, name,isCompleted,routineBelongTo,elapsedTime, orderIndex);
    }

}