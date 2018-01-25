package com.zhuinden.realmlivepagedlistproviderexperiment.util;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executor;

import io.realm.RealmModel;

/**
 * Created by Zhuinden on 2018.01.25..
 */

class RealmLivePagedListBuilder<T extends RealmModel> extends LivePagedListBuilder<Integer, T> {
    private final RealmPaginationManager realmPaginationManager;

    private final DataSource.Factory<Integer, T> dataSourceFactory;
    private final PagedList.Config config;

    private Integer initialLoadKey;
    private PagedList.BoundaryCallback boundaryCallback;
    private Executor backgroundThreadExecutor;

    public RealmLivePagedListBuilder(RealmPaginationManager realmPaginationManager, @NonNull DataSource.Factory<Integer, T> dataSourceFactory, @NonNull PagedList.Config config) {
        super(dataSourceFactory, config);
        this.realmPaginationManager = realmPaginationManager;
        this.dataSourceFactory = dataSourceFactory;
        this.config = config;
    }

    @NonNull
    @Override
    public LivePagedListBuilder<Integer, T> setBoundaryCallback(@Nullable PagedList.BoundaryCallback<T> boundaryCallback) {
        this.boundaryCallback = boundaryCallback;
        return super.setBoundaryCallback(boundaryCallback);
    }

    @NonNull
    @Override
    public LivePagedListBuilder<Integer, T> setBackgroundThreadExecutor(@NonNull Executor backgroundThreadExecutor) {
        if(backgroundThreadExecutor == null) {
            throw new IllegalStateException("Invalid background thread executor.");
        }
        this.backgroundThreadExecutor = backgroundThreadExecutor;
        return super.setBackgroundThreadExecutor(backgroundThreadExecutor);
    }

    @NonNull
    @Override
    public LiveData<PagedList<T>> build() {
        if (config == null) {
            throw new IllegalArgumentException("PagedList.Config must be provided");
        }
        if (dataSourceFactory == null) {
            throw new IllegalArgumentException("DataSource.Factory must be provided");
        }
        if (backgroundThreadExecutor == null) {
            backgroundThreadExecutor = ArchTaskExecutor.getIOThreadExecutor();
        }

        return new RealmComputableLiveData<PagedList<T>>(realmPaginationManager) {
            @Nullable
            private PagedList<T> mList;
            @Nullable
            private DataSource<Integer, T> mDataSource;

            private final DataSource.InvalidatedCallback mCallback =
                    new DataSource.InvalidatedCallback() {
                        @Override
                        public void onInvalidated() {
                            Log.i("REALM COMPUTABLE DATA", "INVALIDATING DATA SOURCE.");
                            invalidate();
                        }
                    };

            @Override
            protected PagedList<T> compute() {
                Log.i("REALM COMPUTABLE DATA", "COMPUTING....");
                @Nullable Integer initializeKey = initialLoadKey;
                if (mList != null) {
                    //noinspection unchecked
                    initializeKey = (Integer) mList.getLastKey();
                }

                do {
                    if (mDataSource != null) {
                        mDataSource.removeInvalidatedCallback(mCallback);
                    }

                    Log.i("REALM COMPUTABLE DATA", "RE-CREATING DATA SOURCE.");
                    mDataSource = dataSourceFactory.create();
                    mDataSource.addInvalidatedCallback(mCallback);

                    mList = new PagedList.Builder<>(mDataSource, config)
                            .setMainThreadExecutor(realmPaginationManager.getMainThreadExecutor())
                            .setBackgroundThreadExecutor(backgroundThreadExecutor)
                            .setBoundaryCallback(boundaryCallback)
                            .setInitialKey(initializeKey)
                            .build();
                    Log.i("REALM COMPUTABLE DATA", "PAGED LIST IS BUILT.");
                } while (mList.isDetached());
                return mList;
            }
        }.getLiveData();
    }
}
