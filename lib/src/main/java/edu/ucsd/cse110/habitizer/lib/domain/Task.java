package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import java.util.Objects;

public class Task implements Serializable{
    private final @Nullable Integer id;
    private final @NonNull String name;
    private final boolean isCompleted;
    private final int routineBelongTo;

    private  int elapsedTime;

    private final int orderIndex;


    public Task(@Nullable Integer id, @NonNull String name, boolean isCompleted, int routineBelongTo, int elapsedTime, int orderIndex) {
        this.id = id;
        this.name = name;
        this.isCompleted = isCompleted;
        this.routineBelongTo = routineBelongTo;
        this.elapsedTime = elapsedTime;
        this.orderIndex = orderIndex;

    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String name() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int routineBelongTo() {
        return routineBelongTo;
    }


    public Task withId(int id) {
        return new Task(id, this.name, this.isCompleted, this.routineBelongTo, this.elapsedTime,this.orderIndex);
    }

    public Task markCompleted() {
        return new Task(this.id, this.name, true, this.routineBelongTo, this.elapsedTime, this.orderIndex);
    }


    public Task markInCompleted() {
        return new Task(this.id, this.name, false, this.routineBelongTo, this.elapsedTime, this.orderIndex);
    }


    public int elapsedTime() {
        return elapsedTime;
    }

    public int orderIndex(){ return orderIndex; }

    public Task withOrderIndex(int newOrderIndex) {
        return new Task(this.id, this.name, this.isCompleted, this.routineBelongTo, this.elapsedTime, newOrderIndex);
    }

    public void updateElapsedTime(int elapsedTaskTime) {

        this.elapsedTime = elapsedTaskTime;
    }
    public Task completeWithTime(int elapsedTime) {
        return new Task(this.id, this.name, true, this.routineBelongTo, elapsedTime, this.orderIndex);
    }

    public Task rename(String newName) {
        return new Task(this.id, newName, this.isCompleted, this.routineBelongTo, this.elapsedTime, this.orderIndex);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isCompleted == task.isCompleted &&
                routineBelongTo == task.routineBelongTo &&
                Objects.equals(id, task.id) &&
                Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isCompleted, routineBelongTo);
    }


}
