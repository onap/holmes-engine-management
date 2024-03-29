/**
 * Copyright 2017 ZTE Corporation.
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

package org.onap.holmes.engine.resources;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.onap.holmes.common.dmaap.store.ClosedLoopControlNameCache;
import org.onap.holmes.common.exception.CorrelationException;
import org.onap.holmes.engine.manager.DroolsEngine;
import org.onap.holmes.engine.request.CompileRuleRequest;
import org.onap.holmes.engine.request.DeployRuleRequest;
import org.powermock.api.easymock.PowerMock;

import jakarta.ws.rs.WebApplicationException;

import static org.easymock.EasyMock.*;

public class EngineResourcesTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private DroolsEngine droolsEngine;
    private EngineResources engineResources;
    private ClosedLoopControlNameCache closedLoopControlNameCache;

    @Before
    public void setUp() {
        droolsEngine = PowerMock.createMock(DroolsEngine.class);
        closedLoopControlNameCache = new ClosedLoopControlNameCache();
        engineResources = new EngineResources();
        engineResources.setClosedLoopControlNameCache(closedLoopControlNameCache);
        engineResources.setDroolsEngine(droolsEngine);
        PowerMock.resetAll();
    }

    @Test
    public void deployRule_exception() throws CorrelationException {
        DeployRuleRequest deployRuleRequest = new DeployRuleRequest();

        thrown.expect(WebApplicationException.class);

        expect(droolsEngine.deployRule(anyObject(DeployRuleRequest.class))).
                andThrow(new CorrelationException(""));
        PowerMock.replayAll();
        engineResources.deployRule(deployRuleRequest);
        PowerMock.verifyAll();
    }

    @Test
    public void deployRule_normal() throws CorrelationException {
        DeployRuleRequest deployRuleRequest = new DeployRuleRequest();
        deployRuleRequest.setContent("package packageName;\n\nimport xxx.xxx.xxx;");
        deployRuleRequest.setLoopControlName("loopControlName");

        expect(droolsEngine.deployRule(anyObject(DeployRuleRequest.class))).andReturn("packageName");
        PowerMock.replayAll();
        engineResources.deployRule(deployRuleRequest);
        PowerMock.verifyAll();
    }

    @Test
    public void undeployRule_exception() throws CorrelationException {
        String packageName = "packageName";

        thrown.expect(WebApplicationException.class);

        droolsEngine.undeployRule(anyObject(String.class));
        expectLastCall().andThrow(new CorrelationException(""));
        PowerMock.replayAll();
        engineResources.undeployRule(packageName);
        PowerMock.verifyAll();
    }

    @Test
    public void undeployRule_normal() throws CorrelationException {
        String packageName = "packageName";

        droolsEngine.undeployRule(anyObject(String.class));
        PowerMock.replayAll();
        engineResources.undeployRule(packageName);
        PowerMock.verifyAll();
    }

    @Test
    public void compileRule_exception() throws CorrelationException {
        CompileRuleRequest compileRuleRequest = new CompileRuleRequest();

        thrown.expect(WebApplicationException.class);

        droolsEngine.compileRule(anyObject(String.class));
        expectLastCall().andThrow(new CorrelationException(""));
        PowerMock.replayAll();
        engineResources.compileRule(compileRuleRequest);
        PowerMock.verifyAll();
    }

    @Test
    public void compileRule_normal() throws CorrelationException {
        CompileRuleRequest compileRuleRequest = new CompileRuleRequest();
        droolsEngine.compileRule(anyObject(String.class));
        PowerMock.replayAll();
        engineResources.compileRule(compileRuleRequest);
        PowerMock.verifyAll();
    }
}
