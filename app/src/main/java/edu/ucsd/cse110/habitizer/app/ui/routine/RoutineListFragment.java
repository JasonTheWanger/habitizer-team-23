package edu.ucsd.cse110.habitizer.app.ui.routine;

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

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineListBinding;
import edu.ucsd.cse110.habitizer.app.ui.routinelist.RoutineListAdapter;

public class RoutineListFragment extends Fragment {
    private FragmentRoutineListBinding binding;
    private MainViewModel activityModel;
    private RoutineListAdapter adapter;

    public static RoutineListFragment newInstance() {
        return new RoutineListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        clearSavedState();
        this.adapter = new RoutineListAdapter(requireContext(), new ArrayList<>(), activityModel);

        activityModel.getRoutineList().observe(routines -> {
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines));
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRoutineListBinding.inflate(inflater, container, false);

        binding.listView.setAdapter(adapter);

        binding.AddRoutineButton.setOnClickListener(v -> {
            AddRoutineDialogFragment.newInstance().show(
                    getParentFragmentManager(), "AddRoutineDialog"
            );
        });

        return binding.getRoot();
    }

    private void clearSavedState() {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("HabitizerPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        editor.remove("current_fragment");
        editor.apply();
    }

}
