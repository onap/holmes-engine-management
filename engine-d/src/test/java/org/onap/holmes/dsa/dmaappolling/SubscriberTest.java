/*
 * Copyright 2017-2021 ZTE Corporation.
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
package org.onap.holmes.dsa.dmaappolling;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.onap.holmes.common.api.stat.VesAlarm;
import org.onap.holmes.common.exception.CorrelationException;
import org.onap.holmes.common.utils.JerseyClient;
import org.onap.holmes.common.utils.SpringContextUtil;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringContextUtil.class, JerseyClient.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SubscriberTest {

    private DMaaPResponseUtil util = new DMaaPResponseUtil();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        PowerMock.mockStatic(SpringContextUtil.class);
        expect(SpringContextUtil.getBean(DMaaPResponseUtil.class)).andReturn(util).anyTimes();
    }

    @Test
    public void subscribe_normal() throws Exception {
        VesAlarm vesAlarm = new VesAlarm();
        vesAlarm.setDomain("ONAP");
        vesAlarm.setEventId("123");
        vesAlarm.setEventName("Event-123");
        vesAlarm.setEventType("EventType");
        vesAlarm.setLastEpochMicrosec(1000L);
        vesAlarm.setNfcNamingCode("123");
        vesAlarm.setNfNamingCode("123");
        vesAlarm.setPriority("high");
        vesAlarm.setReportingEntityId("ID-123");
        vesAlarm.setReportingEntityName("Name-123");
        vesAlarm.setSequence(1);
        vesAlarm.setSourceId("Source-123");
        vesAlarm.setSourceName("Source-123");
        vesAlarm.setStartEpochMicrosec(500L);
        vesAlarm.setVersion("4.0");
        Map alarmAdditionalFields = new HashMap<String, String>();
        alarmAdditionalFields.put("addInfo", "addInfo");
        vesAlarm.setAlarmAdditionalInformation(alarmAdditionalFields);
        vesAlarm.setAlarmCondition("alarmCondition");
        vesAlarm.setAlarmInterfaceA("alarmInterfaceA");
        vesAlarm.setEventCategory("eventCategory");
        vesAlarm.setEventSeverity("eventSeverity");
        vesAlarm.setEventSourceType("eventSourceType");
        vesAlarm.setFaultFieldsVersion("2.0");
        vesAlarm.setSpecificProblem("specificProblem");
        vesAlarm.setVfStatus("vfStatus");

        String eventString = "{\"event\": {\"commonEventHeader\": {" +
                "\"domain\": \"ONAP\"," +
                "\"eventId\": \"123\"," +
                "\"eventName\": \"Event-123\"," +
                "\"eventType\": \"EventType\"," +
                "\"lastEpochMicrosec\": 1000," +
                "\"nfcNamingCode\": \"123\"," +
                "\"nfNamingCode\": \"123\"," +
                "\"priority\": \"high\"," +
                "\"reportingEntityId\": \"ID-123\"," +
                "\"reportingEntityName\": \"Name-123\"," +
                "\"sequence\": 1," +
                "\"sourceId\": \"Source-123\"," +
                "\"sourceName\": \"Source-123\"," +
                "\"startEpochMicrosec\": 500," +
                "\"version\": \"4.0\"" +
                "}," +
                " \"faultFields\" : {" +
                "\"alarmAdditionalInformatiddon\":[{\"name\":\"addInfo\", \"value\":\"addInfo\"}]," +
                "\"alarmCondition\": \"alarmCondition\"," +
                "\"alarmInterfaceA\": \"alarmInterfaceA\"," +
                "\"eventCategory\": \"eventCategory\"," +
                "\"eventSeverity\": \"eventSeverity\"," +
                "\"eventSourceType\": \"eventSourceType\"," +
                "\"faultFieldsVersion\": \"2.0\"," +
                "\"specificProblem\": \"specificProblem\"," +
                "\"vfStatus\": \"vfStatus\"" +
                "}}}";

        List<String> responseList = new ArrayList<>();
        responseList.add(eventString);

        JerseyClient mockedClient = PowerMock.createMock(JerseyClient.class);
        PowerMock.expectNew(JerseyClient.class).andReturn(mockedClient);
        expect(mockedClient.path(anyString())).andReturn(mockedClient).times(2);
        expect(mockedClient.queryParam(anyString(), anyInt())).andReturn(mockedClient);
        expect(mockedClient.get(anyString(), anyObject())).andReturn(responseList);

        PowerMock.replayAll();

        Subscriber subscriber = new Subscriber();
        subscriber.setUrl("https://www.onap.org");
        subscriber.setConsumerGroup("group");
        subscriber.setConsumer("consumer");
        List<VesAlarm> vesAlarms = subscriber.subscribe();

        PowerMock.verifyAll();

        assertThat(vesAlarm.getEventName(), equalTo(vesAlarms.get(0).getEventName()));
    }

    @Test
    public void subscribe_retrieve_data_exception() throws Exception {
        thrown.expect(CorrelationException.class);
        thrown.expectMessage("Failed to get data from DMaaP.");

        VesAlarm vesAlarm = new VesAlarm();
        vesAlarm.setDomain("ONAP");
        vesAlarm.setEventId("123");
        vesAlarm.setEventName("Event-123");
        vesAlarm.setEventType("EventType");
        vesAlarm.setLastEpochMicrosec(1000L);
        vesAlarm.setNfcNamingCode("123");
        vesAlarm.setNfNamingCode("123");
        vesAlarm.setPriority("high");
        vesAlarm.setReportingEntityId("ID-123");
        vesAlarm.setReportingEntityName("Name-123");
        vesAlarm.setSequence(1);
        vesAlarm.setSourceId("Source-123");
        vesAlarm.setSourceName("Source-123");
        vesAlarm.setStartEpochMicrosec(500L);
        vesAlarm.setVersion("4.0");
        Map alarmAdditionalFields = new HashMap<String, String>();
        alarmAdditionalFields.put("addInfo", "addInfo");
        vesAlarm.setAlarmAdditionalInformation(alarmAdditionalFields);
        vesAlarm.setAlarmCondition("alarmCondition");
        vesAlarm.setAlarmInterfaceA("alarmInterfaceA");
        vesAlarm.setEventCategory("eventCategory");
        vesAlarm.setEventSeverity("eventSeverity");
        vesAlarm.setEventSourceType("eventSourceType");
        vesAlarm.setFaultFieldsVersion("2.0");
        vesAlarm.setSpecificProblem("specificProblem");
        vesAlarm.setVfStatus("vfStatus");

        String eventString = "{\"event\": {\"commonEventHeader\": {" +
                "\"domain\": \"ONAP\"," +
                "\"eventId\": \"123\"," +
                "\"eventName\": \"Event-123\"," +
                "\"eventType\": \"EventType\"," +
                "\"lastEpochMicrosec\": 1000," +
                "\"nfcNamingCode\": \"123\"," +
                "\"nfNamingCode\": \"123\"," +
                "\"priority\": \"high\"," +
                "\"reportingEntityId\": \"ID-123\"," +
                "\"reportingEntityName\": \"Name-123\"," +
                "\"sequence\": 1," +
                "\"sourceId\": \"Source-123\"," +
                "\"sourceName\": \"Source-123\"," +
                "\"startEpochMicrosec\": 500," +
                "\"version\": \"4.0\"" +
                "}," +
                " \"faultFields\" : {" +
                "\"alarmAdditionalInformation\": [{\"name\":\"addInfo\", \"value\":\"addInfo\"}]," +
                "\"alarmCondition\": \"alarmCondition\"," +
                "\"alarmInterfaceA\": \"alarmInterfaceA\"," +
                "\"eventCategory\": \"eventCategory\"," +
                "\"eventSeverity\": \"eventSeverity\"," +
                "\"eventSourceType\": \"eventSourceType\"," +
                "\"faultFieldsVersion\": \"2.0\"," +
                "\"specificProblem\": \"specificProblem\"," +
                "\"vfStatus\": \"vfStatus\"" +
                "}}}";

        List<String> responseList = new ArrayList<>();
        responseList.add(eventString);

        JerseyClient mockedClient = PowerMock.createMock(JerseyClient.class);
        PowerMock.expectNew(JerseyClient.class).andReturn(mockedClient);
        expect(mockedClient.path(anyString())).andReturn(mockedClient).times(2);
        expect(mockedClient.queryParam(anyString(), anyInt())).andReturn(mockedClient);
        expect(mockedClient.get(anyString(), anyObject())).andThrow(new RuntimeException());

        PowerMock.replayAll();

        Subscriber subscriber = new Subscriber();
        subscriber.setUrl("https://www.onap.org");
        subscriber.setConsumerGroup("group");
        subscriber.setConsumer("consumer");

        subscriber.subscribe();

        PowerMock.verifyAll();
    }

    @Test
    public void testSetterAndGetter() {

        PowerMock.replayAll();

        Subscriber subscriber = new Subscriber();
        subscriber.setTimeout(100);
        subscriber.setLimit(10);
        subscriber.setPeriod(10);
        subscriber.setSecure(false);
        subscriber.setTopic("test");
        subscriber.setUrl("http://localhost");
        subscriber.setConsumerGroup("Group1");
        subscriber.setConsumer("Consumer1");
        subscriber.setAuthInfo(null);
        subscriber.setAuthExpDate(null);

        assertThat(subscriber.getTimeout(), is(100));
        assertThat(subscriber.getLimit(), is(10));
        assertThat(subscriber.getPeriod(), is(10));
        assertThat(subscriber.isSecure(), is(false));
        assertThat(subscriber.getTopic(), equalTo("test"));
        assertThat(subscriber.getUrl(), equalTo("http://localhost"));
        assertThat(subscriber.getConsumerGroup(), equalTo("Group1"));
        assertThat(subscriber.getConsumer(), equalTo("Consumer1"));
        assertThat(subscriber.getAuthInfo(), nullValue());
        assertThat(subscriber.getAuthExpDate(), nullValue());

        PowerMock.verifyAll();
    }

}
