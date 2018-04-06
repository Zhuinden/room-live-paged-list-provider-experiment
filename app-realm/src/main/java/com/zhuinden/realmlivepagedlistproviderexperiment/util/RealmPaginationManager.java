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

import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by Zhuinden on 2017.10.27..
 */

@Singleton
public class RealmPaginationManager {
    private static class RealmManagement {
        private HandlerThread handlerThread;
        private Handler handler;
        private Realm workerRealm;

        public RealmManagement(HandlerThread handlerThread, Handler handler, Realm workerRealm) {
            this.handlerThread = handlerThread;
            this.handler = handler;
            this.workerRealm = workerRealm;
        }
    }

    private final Executor mainThreadExecutor = new RealmMainThreadExecutor();
    private AtomicReference<HandlerThread> handlerThread = new AtomicReference<>();
    private AtomicReference<Handler> handler = new AtomicReference<>();
    private AtomicReference<RealmQueryExecutor> realmQueryExecutor = new AtomicReference<>();
    private AtomicReference<RealmManagement> realmManagement = new AtomicReference<>();
    private AtomicInteger openCount = new AtomicInteger();

    @Inject
    public RealmPaginationManager() {
    }

    public void open() {
        if(openCount.getAndIncrement() == 0) {
            HandlerThread handlerThread = new HandlerThread("REALM_PAGINATION_THREAD");
            this.handlerThread.set(handlerThread);
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized(handlerThread) {
                handlerThread.start();
                try {
                    handlerThread.wait();
                } catch(InterruptedException e) {
                    // Ignored
                }
            }
            Handler handler = new Handler(handlerThread.getLooper());
            this.handler.set(handler);
            this.realmQueryExecutor.set(new RealmQueryExecutor(this, handler));
            handler.post(() -> {
                Realm realm = Realm.getDefaultInstance(); // TODO: Support other configurations.
                realmManagement.set(new RealmManagement(handlerThread, handler, realm));
            });
        }
    }

    public void close() {
        if(openCount.decrementAndGet() == 0) {
            final RealmManagement management = realmManagement.get();
            if(management != null) { // let's hope for the best
                Handler handler = management.handler;
                handler.post(() -> {
                    Realm realm = management.workerRealm;
                    realm.close();
                    HandlerThread handlerThread = management.handlerThread;
                    handlerThread.quit();
                    realmManagement.set(null);
                    realmQueryExecutor.set(null);
                    this.handlerThread.set(null);
                    this.handler.set(null);
                });
            }
        }
    }

    Realm getWorkerRealm() {
        return realmManagement.get().workerRealm;
    }

    Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }

    public <T extends RealmModel> LivePagedListBuilder<Integer, T> createPagedListBuilder(PagedList.Config config, RealmQueryDefinition<T> queryDefinition) {
        return new LivePagedListBuilder<>(createDataSourceFactory(queryDefinition), config) //
                .setFetchExecutor(getRealmQueryExecutor());
    }

    <T extends RealmModel> DataSource.Factory<Integer, T> createDataSourceFactory(RealmQueryDefinition<T> queryDefinition) {
        return new RealmDataSourceFactory<>(this, queryDefinition);
    }

    RealmQueryExecutor getRealmQueryExecutor() {
        return realmQueryExecutor.get();
    }

    boolean isHandlerThreadOpen() {
        return handlerThread.get() != null;
    }
}
