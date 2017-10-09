package com.zhuinden.roomlivepagedlistproviderexperiment.application.injection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.zhuinden.roomlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.database.DatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    DatabaseManager databaseManager(Application application) {
        return Room.databaseBuilder(application, DatabaseManager.class, "database") //
                .fallbackToDestructiveMigration() //
                .allowMainThreadQueries() // TODO: remove this!!!!!!
                .build();
    }

    @Provides
    @Singleton
    TaskDao taskDao(DatabaseManager databaseManager) {
        return databaseManager.taskDao();
    }
}
