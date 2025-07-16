package edu.ucsd.cse110.habitizer.app.ui.routinelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.SingleRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.task.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class RoutineListAdapter extends ArrayAdapter<Routine> {

    private MainViewModel activityModel;
    public RoutineListAdapter(Context context, List<Routine> routines, MainViewModel activityModel) {
        super(context, 0, new ArrayList<>(routines));
        this.activityModel = activityModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Routine routine = getItem(position);
        assert routine != null;

        SingleRoutineBinding binding;
        if (convertView != null) {
            binding = SingleRoutineBinding.bind(convertView);
        } else {
            binding = SingleRoutineBinding.inflate(
                    LayoutInflater.from(getContext()), parent, false
            );
        }

        binding.routineName.setText(routine.name());

        binding.enterRoutineButton.setOnClickListener(v -> {
            var fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            activityModel.startRoutineTimer();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TaskListFragment.newInstance(routine.id()))
                    .addToBackStack(null)
                    .commit();
        });

        return binding.getRoot();
    }
}
