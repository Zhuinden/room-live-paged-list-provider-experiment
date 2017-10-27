package com.zhuinden.realmlivepagedlistproviderexperiment.application.injection;

import android.app.Application;

import com.zhuinden.realmlivepagedlistproviderexperiment.application.CustomApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Module
public class AppModule {
    @Provides
    Application application(CustomApplication customApplication) {
        return customApplication;
    }
}
