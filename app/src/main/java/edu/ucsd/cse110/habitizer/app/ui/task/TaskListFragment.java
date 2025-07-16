package edu.ucsd.cse110.habitizer.app.ui.task;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListTaskBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.RoutineListFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.TaskListAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


public class TaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;

    private TaskListAdapter adapter;
    private ListTaskBinding inside_view;

    private boolean check = false;

    private boolean checkRoutine = false;

    private static final String ARG_ROUTINE_ID = "routine_id";

    public TaskListFragment(){

    }

    public static TaskListFragment newInstance(int routineId) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);

        this.activityModel = modelProvider.get(MainViewModel.class);

        int routineId =0;
        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID, 0);
        }

        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("HabitizerPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        editor.putString("current_fragment", "task_list");

        if (activityModel.getCurrentRoutine().getValue() != null) {
            editor.putInt("current_routine_id", routineId);
        }

        int savedRoutineId = prefs.getInt("elapsed_time_routine_id", -1);

        if (savedRoutineId == routineId && prefs.contains("routine_elapsed_time")) {
            int elapsedTime = prefs.getInt("routine_elapsed_time", 0);
            activityModel.setElapsedTime(elapsedTime);
        } else {
            activityModel.setElapsedTime(0);
        }
        editor.apply();

        activityModel.setRoutine(routineId);
        this.adapter = new TaskListAdapter(requireContext(),  new ArrayList<>(), activityModel);

        activityModel.getTaskList().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks));
            adapter.notifyDataSetChanged();
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);

        view.listView.setAdapter(adapter);

        activityModel.getCurrentRoutine().observe(routine -> view.routineName.setText(routine.name()));

        activityModel.getElapsedTime().observe(time -> {
            if(activityModel.getElapsedTime().getValue() == null || activityModel.getElapsedTime().getValue() == 0 ){
                view.totalTimeDisplay.setText("0m");
            }
            else{
                if(!activityModel.getRoutineStatus().getValue()) {
                    view.totalTimeDisplay.setText(RoundedUp(activityModel.getElapsedTime().getValue()).toString());
                }else {
                    view.totalTimeDisplay.setText(RoundedDown(activityModel.getElapsedTime().getValue()).toString());
                }
            }

        });
        activityModel.shownRoutine().setValue(false);

        view.MockModeButton.setOnClickListener(v -> {

            boolean is_Mock = activityModel.isMockModeEnabled();
            if(!is_Mock){
                activityModel.toggleMockMode(true);
                view.MockModeButton.setText("ADVANCE");
                view.AdvanceBotton.setEnabled(true);
                if (activityModel != null && activityModel.getElapsedTime().getValue() != null &&
                        activityModel.getCurrentRoutine().getValue() != null) {

                    int routineId = activityModel.getCurrentRoutine().getValue().id();
                    int elapsedTime = activityModel.getElapsedTime().getValue();

                    SharedPreferences prefs = requireActivity().getSharedPreferences("HabitizerPrefs", android.content.Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("routine_elapsed_time", elapsedTime);
                    editor.putInt("elapsed_time_routine_id", routineId);
                    editor.apply();
                }
            } else {
                activityModel.advanceMockTime();
                SharedPreferences prefs = requireActivity().getSharedPreferences("HabitizerPrefs", android.content.Context.MODE_PRIVATE);

                int routineId = activityModel.getCurrentRoutine().getValue().id();
                int elapsedTime = activityModel.getElapsedTime().getValue();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("routine_elapsed_time", elapsedTime);
                editor.putInt("elapsed_time_routine_id", routineId);
                editor.apply();
            }

        });

        if(!activityModel.isMockModeEnabled()){
            view.AdvanceBotton.setEnabled(false);
        }

        view.AdvanceBotton.setOnClickListener(v ->{
            if (activityModel.isMockModeEnabled()){
                activityModel.toggleMockMode(false);
                view.MockModeButton.setText("Stop");
                view.AdvanceBotton.setEnabled(false);
            }

        });

        view.goalTimeDisplay.setOnClickListener(vi -> {
            var dialogFragment = SetGoalFragment.newInstance();
            view.goalTimeDisplay.setText(formatGoalTime(activityModel.getRoutineGoalTime().getValue()).toString());
            dialogFragment.show(getParentFragmentManager(), "SetGoalTimeFragment");
        });

        activityModel.getRoutineGoalTime().observe(goalTime -> {
            if(goalTime == null || goalTime == 0){
                view.goalTimeDisplay.setText("-");
            }else {
                view.goalTimeDisplay.setText(formatGoalTime(goalTime));
            }
        });

        view.endRoutineBotton.setOnClickListener(v -> {

            if(checkRoutine == false) {
                view.endRoutineBotton.setText("Routine Ended");
                view.endRoutineBotton.setEnabled(false);
                activityModel.endRoutine();
                adapter.notifyDataSetChanged();
                view.totalTimeDisplay.setText(RoundedDown(activityModel.getElapsedTime().getValue()).toString());
                checkRoutine = true;
            }
        });

        view.backToRoutinesButton.setOnClickListener(v -> {
            SharedPreferences preferences = requireActivity().getSharedPreferences("HabitizerPrefs", android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editorClear = preferences.edit();
            editorClear.remove("current_fragment");
            editorClear.remove("current_routine_id");
            editorClear.remove("routine_elapsed_time");
            editorClear.remove("elapsed_time_routine_id");
            editorClear.apply();

            activityModel.resetRoutine();

            adapter.resetCheckStatus();

            activityModel.stopTaskTimer();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, RoutineListFragment.newInstance())
                    .commit();
        });

        activityModel.getRoutineStatus().observe(isRoutineActive -> {

            if(!isRoutineActive){
                view.endRoutineBotton.setText("Routine Ended");
                view.endRoutineBotton.setEnabled(false);
                activityModel.stopRoutineTimer();
            }
        });
        activityModel.shownRoutine().observe(active -> {
            if(!active){
                view.endRoutineBotton.setText("Routine Not Started");
                view.endRoutineBotton.setEnabled(false);
            }
            else {
                view.endRoutineBotton.setText("END ROUTINE");
                view.endRoutineBotton.setEnabled(true);
            }
        });
        view.AddTaskButton.setOnClickListener(vi -> {
            var dialogFragment = AddTaskFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "AddTaskFragment");
        });

        return view.getRoot();
    }
    private String formatGoalTime(int seconds) {
        if(seconds == 0){
            return "-";
        }else {
            int minutes = seconds / 60;
            int sec = seconds % 60;
            return String.format("%02d:%02d", minutes, sec);
        }
    }

    private String RoundedDown(int seconds) {
        int minutes = seconds / 60;
        return String.format("%dm", minutes);
    }

    private String RoundedUp(int seconds) {
        int minutes = (seconds + 59) / 60;
        return String.format("%dm", minutes);
    }
}


