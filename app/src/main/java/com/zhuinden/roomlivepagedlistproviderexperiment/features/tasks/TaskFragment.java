package com.zhuinden.roomlivepagedlistproviderexperiment.features.tasks;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuinden.roomlivepagedlistproviderexperiment.R;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class TaskFragment
        extends Fragment {
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class); // should be in a fragment and stuff
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        final TaskAdapter taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);
        viewModel.observeResults(this, pagedList -> {
            //noinspection Convert2MethodRef
            taskAdapter.setList(pagedList);
        });
    }
}
