/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenbot.main.dataprovider;

import greenbot.rule.model.Provider;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.cloud.Tag;
import lombok.experimental.UtilityClass;

/**
 * @author Vinay Lodha
 */
@UtilityClass
public class RuleRequestDataProvider {

    public RuleRequest simple() {
        return RuleRequest.builder()
                .includedTag(Tag.builder().key("owner").value("greenbot").build())
                .amiThreshold(0)
                .cloudwatchTimeframeDuration(1440)
                .provider(Provider.AWS)
                .cpuThresholdInstance(20d)
                .cpuThresholdDatabase(5d)
                .build();
    }
}
