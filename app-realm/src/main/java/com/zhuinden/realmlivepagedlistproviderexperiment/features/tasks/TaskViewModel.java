package com.zhuinden.realmlivepagedlistproviderexperiment.features.tasks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.zhuinden.realmlivepagedlistproviderexperiment.application.Injector;
import com.zhuinden.realmlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.realmlivepagedlistproviderexperiment.data.entity.Task;
import com.zhuinden.realmlivepagedlistproviderexperiment.util.RealmPaginationManager;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class TaskViewModel
        extends ViewModel {
    private final TaskDao taskDao;

    private LiveData<PagedList<Task>> liveResults;

    public TaskViewModel() {
        RealmPaginationManager realmPaginationManager = Injector.get().realmPaginationManager();
        realmPaginationManager.open();

        taskDao = Injector.get().taskDao(); // should be provided by ViewModelProviders.Factory
        liveResults = taskDao.tasksSortedByDate(new PagedList.Config.Builder() //
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
        RealmPaginationManager realmPaginationManager = Injector.get().realmPaginationManager();
        realmPaginationManager.close();
    }

    public LiveData<PagedList<Task>> getTasks() {
        return liveResults;
    }
}
