package edu.ucsd.cse110.habitizer.lib.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class RoutineTest {
    @Test
    public void testRoutineCreation() {
        Routine routine = new Routine(1, "Morning Routine", List.of());
        assertNotNull(routine);
        assertEquals(Integer.valueOf(1), routine.id());
        assertEquals("Morning Routine", routine.name());
    }

    @Test
    public void testRoutineAddTask() {
        Routine routine = new Routine(1, "Morning Routine", List.of());
        Task task = new Task(1, "Brush Teeth", false, 1,0, 0);
        routine.addTask(task);

        assertTrue(routine.tasks().contains(task));
    }

    @Test
    public void testRoutineWithId() {
        Routine routine = new Routine(null, "Evening Routine", List.of());
        Routine newRoutine = routine.withId(2);
        assertEquals(Integer.valueOf(2), newRoutine.id());
        assertEquals("Evening Routine", newRoutine.name());
    }

    @Test
    public void testRoutineEquality() {
        Routine routine1 = new Routine(1, "Morning Routine", List.of());
        Routine routine2 = new Routine(1, "Morning Routine", List.of());

        assertEquals(routine1, routine2);
        assertEquals(routine1.hashCode(), routine2.hashCode());
    }
}
