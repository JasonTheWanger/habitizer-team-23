package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListTaskBinding;
import edu.ucsd.cse110.habitizer.app.ui.task.RenameTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


public class TaskListAdapter extends ArrayAdapter<Task> {

    private boolean check = false;
    private boolean isPaused = false;
    private int taskID;
    private MainViewModel activityModel;
    public TaskListAdapter(Context context, List<Task> tasks, MainViewModel activityModel) {
        super(context, 0, new ArrayList<>(tasks));

        this.activityModel = activityModel;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        var task = getItem(position);
        assert task != null;

        ListTaskBinding binding;
        if (convertView != null) {
            binding = ListTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListTaskBinding.inflate(layoutInflater, parent, false);
        }


        binding.taskName.setText(task.name());
        binding.taskTimer.setText(RoundedUp(task.elapsedTime()));
        binding.taskId.setText(task.id().toString());
        binding.pauseButton.setEnabled(false);
        if (task.isCompleted()) {
            binding.checkview.setText("[Complete]");
            binding.pauseButton.setImageResource(R.drawable.ic_resume);
            binding.pauseButton.setEnabled(false);
        }

        binding.task.setOnClickListener(v -> {
            if (!activityModel.isRoutineTimerRunning()) {
                return;
            }


            int clickedTaskId = Integer.parseInt(binding.taskId.getText().toString());

            if(!check && !task.isCompleted()) {
                taskID = clickedTaskId;
                activityModel.setCurrentTask(taskID);
                activityModel.shownRoutine().setValue(true);
                binding.deleteButton.setEnabled(false);
                binding.pauseButton.setEnabled(true);
                check = true;

            }
            else if(check){
                int completedTaskId = activityModel.getCurrentTask().getValue().id();
                Task Current_Task = activityModel.getCurrentTask().getValue();
                Current_Task.updateElapsedTime(activityModel.getElapsedTaskTime().getValue());

                if (!activityModel.isRoutineTimerRunning()) {
                    return;
                }

                activityModel.checkOffTask();
                if (completedTaskId == clickedTaskId) {
                    binding.checkview.setText("[Complete]");
                    binding.pauseButton.setImageResource(R.drawable.ic_resume);
                    binding.pauseButton.setEnabled(false);
                }
                check = false;

            }
            else if (!check && task.isCompleted()){
                binding.checkview.setText("[Complete]");}
        });

        binding.positionUpButtom.setOnClickListener(v -> {
            activityModel.moveTaskUp(task);
        });

        binding.positionDownButtom.setOnClickListener(v -> {
            activityModel.moveTaskDown(task);
        });

        binding.pauseButton.setOnClickListener(v -> {

            if (!activityModel.isRoutineTimerRunning()) {
                return;
            }
            isPaused = !isPaused;
            if (isPaused) {
                binding.pauseButton.setImageResource(R.drawable.ic_resume);
                activityModel.stopTaskTimer();
            } else {
                binding.pauseButton.setImageResource(R.drawable.ic_pause);
                activityModel.startTaskTimer();
            }
        });

        activityModel.getElapsedTaskTime().observe(elapsedTime -> {
            if (activityModel.getCurrentTask().getValue() != null && activityModel.getCurrentTask().getValue().id() != null && activityModel.getCurrentTask().getValue().id() == task.id()) {
                if(activityModel.getCurrentTask().getValue().isCompleted()) {

                    binding.taskTimer.setText(RoundedUp(elapsedTime));
                }else{
                    binding.taskTimer.setText(formatTime(elapsedTime));
                }
            }
        });

        binding.renameButton.setOnClickListener(v -> {
            RenameTaskDialogFragment dialog = RenameTaskDialogFragment.newInstance(task.id());
            FragmentActivity activity = (FragmentActivity) getContext();
            dialog.show(activity.getSupportFragmentManager(), "RenameTaskDialog");
        });

        binding.deleteButton.setOnClickListener(v -> {
            Task taskToDelete = getItem(position);
            if (taskToDelete != null) {
                activityModel.deleteTask(taskToDelete);
            }
        });

        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var flashcard = getItem(position);
        assert flashcard != null;

        var id = flashcard.id();
        assert id != null;

        return id;
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    private String RoundedUp(int seconds) {
        if (seconds < 60){
            return String.format("%dsecs", (seconds + 4) / 5 * 5);
        }
        int minutes = (seconds + 59) / 60;
        return String.format("%dm", minutes);
    }

    public void resetCheckStatus(){
        check = false;
    }

}