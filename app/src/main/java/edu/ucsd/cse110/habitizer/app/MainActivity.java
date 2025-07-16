package edu.ucsd.cse110.habitizer.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.RoutineListFragment;
import edu.ucsd.cse110.habitizer.app.ui.task.TaskListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private boolean MorningRoutine = true;
    private MainViewModel activityModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);


        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(this, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("HabitizerPrefs", MODE_PRIVATE);
            String currentFragment = prefs.getString("current_fragment", null);

            if ("task_list".equals(currentFragment)) {
                if (prefs.contains("current_routine_id")) {
                    int routineId = prefs.getInt("current_routine_id", -1);
                    if (routineId != -1) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, TaskListFragment.newInstance(routineId))
                                .commit();
                        return;
                    }
                }
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, RoutineListFragment.newInstance())
                    .commit();
        }
    }


}