package com.zhuinden.realmlivepagedlistproviderexperiment.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.zhuinden.realmlivepagedlistproviderexperiment.R;
import com.zhuinden.realmlivepagedlistproviderexperiment.features.tasks.TaskFragment;
import com.zhuinden.realmlivepagedlistproviderexperiment.util.RealmPaginationManager;

public class MainActivity
        extends AppCompatActivity {
    ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RealmPaginationManager realmPaginationManager = Injector.get().realmPaginationManager();
        realmPaginationManager.open();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        container = findViewById(R.id.container);
        if(savedInstanceState == null) { // TODO: navigation.
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .add(R.id.container, new TaskFragment(), "TASK_FRAGMENT") //
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        RealmPaginationManager realmPaginationManager = Injector.get().realmPaginationManager();
        realmPaginationManager.close();
        super.onDestroy();
    }
}
