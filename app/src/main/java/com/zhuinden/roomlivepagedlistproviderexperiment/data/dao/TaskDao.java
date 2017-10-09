package com.zhuinden.roomlivepagedlistproviderexperiment.data.dao;

import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zhuinden.roomlivepagedlistproviderexperiment.data.entity.Task;

import java.util.List;

/**
 * Created by Owner on 2017. 10. 09..
 */
@Dao
public interface TaskDao {
    @Query("SELECT * FROM " + Task.TABLE_NAME + " ORDER BY " + Task.COLUMN_DATE + " ASC ")
    LivePagedListProvider<Integer, Task> tasksSortedByDate();

    @Query("SELECT COUNT(*) FROM " + Task.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task... tasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Task> tasks);
}
