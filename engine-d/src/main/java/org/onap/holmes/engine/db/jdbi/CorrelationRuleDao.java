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
package org.onap.holmes.engine.db.jdbi;


import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.onap.holmes.common.api.entity.CorrelationRule;
import org.onap.holmes.common.utils.CorrelationRuleMapper;

import java.util.List;

@RegisterRowMapper(CorrelationRuleMapper.class)
public interface CorrelationRuleDao {
    @SqlQuery("SELECT * FROM APLUS_RULE WHERE enable=:enable")
    public List<CorrelationRule> queryRuleByEnable(@Bind("enable") int enable);
}

