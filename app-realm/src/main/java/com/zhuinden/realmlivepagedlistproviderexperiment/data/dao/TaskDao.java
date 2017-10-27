package com.zhuinden.realmlivepagedlistproviderexperiment.data.dao;

import android.arch.paging.LivePagedListProvider;

import com.zhuinden.realmlivepagedlistproviderexperiment.data.entity.Task;
import com.zhuinden.realmlivepagedlistproviderexperiment.util.RealmLivePagedListProvider;
import com.zhuinden.realmlivepagedlistproviderexperiment.util.RealmPaginationManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;

/**
 * Created by Owner on 2017. 10. 09..
 */
@Singleton
public class TaskDao {
    private final RealmPaginationManager realmPaginationManager;

    @Inject
    TaskDao(RealmPaginationManager realmPaginationManager) {
        this.realmPaginationManager = realmPaginationManager;
    }

    public LivePagedListProvider<Integer, Task> tasksSortedByDate() {
        return realmPaginationManager.createLivePagedListProvider(realm -> realm.where(Task.class)
                .findAllSortedAsync("date"));
    }

    public void insert(Task task) {
        try(Realm realm = Realm.getDefaultInstance()) {
            boolean isInTransaction = realm.isInTransaction();
            if(!isInTransaction) {
                realm.beginTransaction();
            }
            realm.insertOrUpdate(task);
            if(!isInTransaction) {
                realm.commitTransaction();
            }
        }
    }
}
