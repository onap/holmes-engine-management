/*
 * Copyright 2017-2022 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.holmes.engine.dmaap;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.onap.holmes.dsa.dmaappolling.Subscriber;
import org.onap.holmes.engine.db.AlarmInfoDaoService;
import org.onap.holmes.engine.manager.DroolsEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class SubscriberAction {

    @Autowired
    private DroolsEngine droolsEngine;
    @Autowired
    private AlarmInfoDaoService alarmInfoDaoService;

    private HashMap<String, DMaaPAlarmPolling> pollingTasks = new HashMap<>();

    public synchronized void addSubscriber(Subscriber subscriber) {
        String topic = subscriber.getTopic();
        if (topic != null) {
            if (pollingTasks.containsKey(topic)) {
                removeSubscriber(subscriber);
            }
            DMaaPAlarmPolling pollingTask = new DMaaPAlarmPolling(subscriber, droolsEngine, alarmInfoDaoService);
            Thread thread = new Thread(pollingTask);
            thread.start();
            pollingTasks.put(topic, pollingTask);
            log.info("Subscribed to topic: " + subscriber.getUrl());
        } else {
            log.info("The topic is null. Operation aborted.");
        }
    }

    public synchronized void removeSubscriber(Subscriber subscriber) {
        String topic = subscriber.getTopic();
        if (topic != null && pollingTasks.containsKey(topic)) {
            pollingTasks.get(topic).stopTask();
            pollingTasks.remove(topic);
        }
        log.info("Topic unsubscribed: " + subscriber.getUrl());
    }

    @PreDestroy
    public void stopPollingTasks() {
        Iterator iterator = pollingTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            pollingTasks.get(key).stopTask();
        }
        pollingTasks.clear();
    }
}
