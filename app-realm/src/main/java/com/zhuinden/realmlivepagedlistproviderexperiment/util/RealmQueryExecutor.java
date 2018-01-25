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

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by Zhuinden on 2017.10.27..
 */

public class RealmQueryExecutor
        implements Executor {
    private final RealmPaginationManager realmPaginationManager;
    private final Handler handler;

    public RealmQueryExecutor(RealmPaginationManager realmPaginationManager, Handler handler) {
        this.realmPaginationManager = realmPaginationManager;
        this.handler = handler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        if(Looper.myLooper() == handler.getLooper()) {
            command.run();
        } else {
            if(realmPaginationManager.isHandlerThreadOpen()) {
                handler.post(command);
            } else {
                throw new IllegalStateException("The handler thread is not open even though it should be");
            }
        }
    }
}
