package com.zhuinden.roomlivepagedlistproviderexperiment.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.zhuinden.roomlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.entity.Task;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Database(entities = {Task.class}, version = 1)
@TypeConverters({RoomTypeConverters.class})
public abstract class DatabaseManager
        extends RoomDatabase {
    public abstract TaskDao taskDao();
}
