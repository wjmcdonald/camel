/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @version $Revision$
 */
public class DefaultComponent<E extends Exchange> extends ServiceSupport implements Component<E> {
    private int defaultThreadPoolSize = 5;
    private CamelContext camelContext;
    private ScheduledExecutorService executorService;

    public DefaultComponent() {
    }

    public DefaultComponent(CamelContext context) {
        this.camelContext = context;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext context) {
        this.camelContext = context;
    }

    public ScheduledExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = createExecutorService();
        }
        return executorService;
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * A factory method to create a default thread pool and executor
     */
    protected ScheduledExecutorService createExecutorService() {
        return new ScheduledThreadPoolExecutor(defaultThreadPoolSize, new ThreadFactory() {
            int counter;

            public synchronized Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("Thread" + (++counter) + " " + DefaultComponent.this.toString());
                return thread;
            }
        });
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
