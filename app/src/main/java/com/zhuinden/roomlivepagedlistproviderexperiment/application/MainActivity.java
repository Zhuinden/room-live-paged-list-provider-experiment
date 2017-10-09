package com.zhuinden.roomlivepagedlistproviderexperiment.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.zhuinden.roomlivepagedlistproviderexperiment.R;
import com.zhuinden.roomlivepagedlistproviderexperiment.features.tasks.TaskFragment;

public class MainActivity
        extends AppCompatActivity {
    ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
