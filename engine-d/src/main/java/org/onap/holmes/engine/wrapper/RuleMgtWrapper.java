/**
 * Copyright 2017 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.holmes.engine.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.onap.holmes.common.api.entity.CorrelationRule;
import org.onap.holmes.common.exception.CorrelationException;
import org.onap.holmes.engine.db.CorrelationRuleDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class RuleMgtWrapper {
    private CorrelationRuleDaoService correlationRuleDaoService;

    @Autowired
    public RuleMgtWrapper(CorrelationRuleDaoService correlationRuleDaoService) {
        this.correlationRuleDaoService = correlationRuleDaoService;
    }

    public List<CorrelationRule> queryRuleByEnable(int enable) throws CorrelationException {

        List<CorrelationRule> ruleTemp = correlationRuleDaoService.queryRuleByRuleEnable(enable);
        return ruleTemp;
    }
}
