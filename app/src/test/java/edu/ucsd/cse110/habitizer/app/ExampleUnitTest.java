package edu.ucsd.cse110.habitizer.app;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.TaskDao;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class ExampleUnitTest {
    private MainViewModel viewModel;
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;
    private TaskDao taskDao;

    @Before
    public void setup() {
        InMemoryDataSource dataSource = new InMemoryDataSource();
        routineRepository = new SimpleRoutineRepository(dataSource);
        taskRepository = new SimpleTaskRepository(dataSource);
        viewModel = new MainViewModel(taskRepository, routineRepository);
        List<Task> taskListTemp = new ArrayList<>();
        Routine routineTemp = new Routine(1, "R1", taskListTemp);
        taskListTemp.add(new Task(1, "Task1", false, 1, 0, 1));
        taskListTemp.add(new Task(2, "Task2", false, 1, 0, 2));
    }

    @Test
    public void testSetRoutine() {
        Routine routine = saveRoutineWithTasks(1, "Morning Routine", generateTasks(2));
        viewModel.setRoutine(routine.id());
        assertEquals(routine, viewModel.getCurrentRoutine().getValue());
    }

    @Test
    public void testSetCurrentTask() {
        Routine routine = saveRoutineWithTasks(1, "Morning Routine", generateTasks(2));
        viewModel.setRoutine(routine.id());
        viewModel.setCurrentTask(1);

        assertEquals(1, (int)viewModel.getCurrentTask().getValue().id());
    }


    @Test
    public void testCheckOffTask() {
        List<Task> taskList = generateTasks(2);
        saveRoutineWithTasks(1, "Routine1", taskList);

        viewModel.setRoutine(1);
        viewModel.setCurrentTask(1);
        viewModel.checkOffTask();

        assertTrue(viewModel.getCurrentTask().getValue().isCompleted());
    }


    @Test
    public void testRemoveTask() {
        Task task = new Task(1, "Test Task", false, 1, 0, 0);
        Routine routine = new Routine(1, "Test Routine", List.of(task));
        routineRepository.save(routine);
        taskRepository.save(task, routine);
        viewModel.setRoutine(1);
        viewModel.deleteTask(task);
        assertFalse(viewModel.getTaskList().getValue().contains(task));
    }

    @Test
    public void testElapsedRoutineTime() throws InterruptedException {
        saveRoutineWithTasks(1, "Routine1", generateTasks(2));
        viewModel.setRoutine(1);
        viewModel.startRoutineTimer();

        Thread.sleep(3500);

        viewModel.stopRoutineTimer();
        int elapsedTime = viewModel.getElapsedRoutineTime().getValue();

        assertTrue("Elapsed time should be at least 3 seconds", elapsedTime >= 3);
    }

    @Test
    public void testPauseRoutine() {
        saveRoutineWithTasks(1, "Routine1", generateTasks(2));
        viewModel.setRoutine(1);
        viewModel.startRoutineTimer();
        viewModel.stopRoutineTimer();
        int elapsedTime = viewModel.getElapsedRoutineTime().getValue();
        assertTrue(elapsedTime >= 0);
    }

    @Test
    public void testSetGoalTime() {
        viewModel.setRoutineGoalTime(120);
        assertEquals(120, (int) viewModel.getRoutineGoalTime().getValue());
    }

    @Test
    public void testAddRoutine() {
        Routine routine = new Routine(null, "New Routine", new ArrayList<>());
        viewModel.addRoutine(routine);
        List<Routine> routines = viewModel.getRoutineList().getValue();
        assertTrue(routines.stream().anyMatch(r -> "New Routine".equals(r.name())));
    }

    @Test
    public void testMoveTaskUp() {
        List<Task> tasks = generateTasks(3);
        saveRoutineWithTasks(1, "Routine1", tasks);
        viewModel.setRoutine(1);

        viewModel.moveTaskUp(tasks.get(1));

        List<Task> updatedTasks = viewModel.getTaskList().getValue();
        assertEquals("Task 1 should be in position 0", 1, updatedTasks.get(0).id().intValue());
    }

    @Test
    public void testMoveTaskDown() {
        List<Task> tasks = generateTasks(3);
        saveRoutineWithTasks(1, "Routine1", tasks);
        viewModel.setRoutine(1);
        viewModel.moveTaskDown(tasks.get(0));
        assertEquals(1, viewModel.getTaskList().getValue().get(0).id().intValue());
    }

    @Test
    public void testRenameTask() {
        saveRoutineWithTasks(1, "Routine1", generateTasks(2));

        viewModel.setRoutine(1);
        viewModel.renameTask(0, "New Task Name");

        Task renamedTask = taskRepository.find(0).getValue();
        assertNotNull(renamedTask);
        assertEquals("New Task Name", renamedTask.name());
    }


    private List<Task> generateTasks(int count) {
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            taskList.add(new Task(i, "Task " + i, false, 1, 0, i));
        }
        return taskList;
    }

    private Routine saveRoutineWithTasks(int id, String name, List<Task> taskList) {
        Routine routine = new Routine(id, name, taskList);
        routineRepository.save(routine);
        for (Task task : taskList) taskRepository.save(task, routine);
        return routine;
    }
}
