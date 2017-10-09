package com.zhuinden.roomlivepagedlistproviderexperiment.application.injection;

import com.zhuinden.roomlivepagedlistproviderexperiment.application.CustomApplication;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.roomlivepagedlistproviderexperiment.data.database.DatabaseManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(CustomApplication application);

        AppComponent build();
    }

    DatabaseManager databaseManager();

    TaskDao taskDao();

    void inject(CustomApplication application);
}
