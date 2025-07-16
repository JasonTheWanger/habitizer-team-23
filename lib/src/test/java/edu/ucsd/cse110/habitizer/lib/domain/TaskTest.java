package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        Task task = new Task(1, "Brush Teeth", false, 1, 0, 0);
        assertNotNull(task);
        assertEquals(Integer.valueOf(1), task.id());
        assertEquals("Brush Teeth", task.name());
        assertFalse(task.isCompleted());
        assertEquals(1, task.routineBelongTo());
        assertEquals(0, task.elapsedTime());
    }

    @Test
    public void testTaskMarkCompleted() {
        Task task = new Task(1, "Brush Teeth", false, 1, 0, 0);
        Task completedTask = task.markCompleted();
        assertTrue(completedTask.isCompleted());
        assertEquals(task.id(), completedTask.id());
        assertEquals(0, completedTask.elapsedTime());
    }

    @Test
    public void testTaskCompleteWithTime() {
        Task task = new Task(1, "Brush Teeth", false, 1, 0, 0);
        Task completedTask = task.completeWithTime(75);
        assertTrue(completedTask.isCompleted());
        assertEquals(75, completedTask.elapsedTime());
    }

    @Test
    public void testTaskRename() {
        Task task = new Task(1, "Brush Teeth", false, 1, 0, 0);
        Task renamedTask = task.rename("Floss Teeth");
        assertEquals("Floss Teeth", renamedTask.name());
        assertEquals(task.id(), renamedTask.id());
        assertEquals(task.elapsedTime(), renamedTask.elapsedTime());
    }

    @Test
    public void testTaskEquality() {
        Task task1 = new Task(1, "Brush Teeth", false, 1, 0, 0);
        Task task2 = new Task(1, "Brush Teeth", false, 1, 0, 1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void testTaskWithId() {
        Task task = new Task(null, "New Task", false, 1, 0, 0);
        Task assignedTask = task.withId(5);
        assertEquals(Integer.valueOf(5), assignedTask.id());
        assertEquals(task.name(), assignedTask.name());
    }
}
