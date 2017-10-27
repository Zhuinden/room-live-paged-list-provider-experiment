package com.zhuinden.realmlivepagedlistproviderexperiment.application.injection;

import com.zhuinden.realmlivepagedlistproviderexperiment.application.CustomApplication;
import com.zhuinden.realmlivepagedlistproviderexperiment.data.dao.TaskDao;
import com.zhuinden.realmlivepagedlistproviderexperiment.util.RealmPaginationManager;

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

    TaskDao taskDao();

    RealmPaginationManager realmPaginationManager();

    void inject(CustomApplication application);
}
