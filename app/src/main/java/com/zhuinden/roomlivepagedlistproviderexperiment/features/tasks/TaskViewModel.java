package com.zhuinden.roomlivepagedlistproviderexperiment.features.tasks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.zhuinden.roomlivepagedlistproviderexperiment.application.Injector;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.entity.Task;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class TaskViewModel
        extends ViewModel {
    private final TaskDao taskDao;

    private LiveData<PagedList<Task>> liveResults;

    public TaskViewModel() {
        taskDao = Injector.get().taskDao(); // should be provided by ViewModelProviders.Factory
        liveResults = new LivePagedListBuilder<>(taskDao.tasksSortedByDate(),
                                                 new PagedList.Config.Builder() //
                                                         .setPageSize(20) //
                                                         .setPrefetchDistance(20) //
                                                         .setEnablePlaceholders(true) //
                                                         .build())
                .setInitialLoadKey(0)
                .build();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<PagedList<Task>> getTasks() {
        return liveResults;
    }
}
