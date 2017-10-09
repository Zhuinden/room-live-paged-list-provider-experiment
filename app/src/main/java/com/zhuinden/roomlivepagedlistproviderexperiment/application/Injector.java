package com.zhuinden.roomlivepagedlistproviderexperiment.application;

import com.zhuinden.roomlivepagedlistproviderexperiment.application.injection.AppComponent;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class Injector {
    private Injector() {
    }

    public static AppComponent get() {
        return CustomApplication.get().appComponent;
    }
}
