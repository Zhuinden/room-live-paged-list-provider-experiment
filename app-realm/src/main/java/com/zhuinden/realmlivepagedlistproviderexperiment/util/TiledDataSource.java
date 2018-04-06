package com.zhuinden.realmlivepagedlistproviderexperiment.util;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.Collections;
import java.util.List;

/**
 * From Paging Library
 */
public abstract class TiledDataSource<T> extends PositionalDataSource<T> {
    @WorkerThread
    public abstract int countItems();

    @WorkerThread
    public abstract List<T> loadRange(int startPosition, int count);

    @Override
    public final void loadInitial(@NonNull LoadInitialParams params,
                            @NonNull LoadInitialCallback<T> callback) {
        int totalCount = countItems();
        if (totalCount == 0) {
            callback.onResult(Collections.<T>emptyList(), 0, 0);
            return;
        }

        // bound the size requested, based on known count
        final int firstLoadPosition = computeInitialLoadPosition(params, totalCount);
        final int firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount);

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
    public final void loadRange(@NonNull LoadRangeParams params,
                          @NonNull LoadRangeCallback<T> callback) {
        List<T> list = loadRange(params.startPosition, params.loadSize);
        if (list != null) {
            callback.onResult(list);
        } else {
            invalidate();
        }
    }
}
