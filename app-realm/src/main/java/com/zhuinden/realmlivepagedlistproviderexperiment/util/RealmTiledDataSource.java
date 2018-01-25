/*
 * Copyright (C) 2017 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.realmlivepagedlistproviderexperiment.util;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Zhuinden on 2017.10.27..
 */

public class RealmTiledDataSource<T extends RealmModel> extends PositionalDataSource<T> {
    private final Realm workerRealm;
    private final RealmResults<T> liveResults;

    private final RealmChangeListener<RealmResults<T>> realmChangeListener = results -> {
        if(results.isLoaded()) {
            Log.i("REALM TILED DATA SOURCE", "REALM DATA CHANGE DETECTED");
            invalidate();
        }
    };

    // WORKER THREAD
    public RealmTiledDataSource(Realm workerRealm, RealmQueryDefinition<T> queryDefinition) {
        Log.i("REALM TILED DATA SOURCE", "CREATED");
        this.workerRealm = workerRealm;
        this.liveResults = queryDefinition.createResults(workerRealm);
        if(!liveResults.isLoaded()) {
            liveResults.load(); // unavoidable
        }
        this.liveResults.addChangeListener(realmChangeListener);
    }

    @WorkerThread
    public int countItems() {
        Log.i("REALM TILED DATA SOURCE", "COUNTING ITEMS");
        if(workerRealm.isClosed() || !liveResults.isValid()) {
            Log.i("REALM TILED DATA SOURCE", "RESULTS ARE NOT VALID, OR REALM IS CLOSED.");
            return 0;
        }
        Log.i("REALM TILED DATA SOURCE", "ITEM SIZE [" + liveResults.size() + "]");
        return liveResults.size();
    }

    @Override
    public boolean isInvalid() {
        Log.i("REALM TILED DATA SOURCE", "REFRESHING REALM.");
        workerRealm.refresh();
        return super.isInvalid();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params,
                            @NonNull LoadInitialCallback<T> callback) {
        int totalCount = countItems();
        if (totalCount == 0) {
            Log.i("REALM TILED DATA SOURCE", "TOTAL COUNT IS 0");
            callback.onResult(Collections.<T>emptyList(), 0, 0);
            return;
        }

        // bound the size requested, based on known count
        final int firstLoadPosition = computeInitialLoadPosition(params, totalCount);
        final int firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount);
        Log.i("REALM TILED DATA SOURCE", "FIRST LOAD POSITION " + firstLoadPosition + " , FIRST LOAD SIZE " + firstLoadSize);

        // convert from legacy behavior
        List<T> list = loadRange(firstLoadPosition, firstLoadSize);
        if (list != null && list.size() == firstLoadSize) {
            callback.onResult(list, firstLoadPosition, totalCount);
        } else {
            // null list, or size doesn't match request
            // The size check is a WAR for Room 1.0, subsequent versions do the check in Room
            invalidate();
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params,
                          @NonNull LoadRangeCallback<T> callback) {
        Log.i("REALM TILED DATA SOURCE", "LOADING RANGE: " + params.startPosition + " , " + params.loadSize);
        List<T> list = loadRange(params.startPosition, params.loadSize);
        if (list != null) {
            callback.onResult(list);
        } else {
            invalidate();
        }
    }

    @WorkerThread
    public List<T> loadRange(int startPosition, int count) {
        Log.i("REALM TILED DATA SOURCE", "LOAD: " + startPosition + " , " + count);
        int countItems = countItems();
        if(countItems == 0) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(count);
        for(int i = startPosition; i < startPosition + count && i < countItems; i++) {
            // noinspection ConstantConditions
            list.add(workerRealm.copyFromRealm(liveResults.get(i)));
        }
        return Collections.unmodifiableList(list);
    }
}
