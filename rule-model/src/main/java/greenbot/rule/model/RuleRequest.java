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
package greenbot.rule.model;

import greenbot.rule.model.cloud.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Lodha
 */
@Data
@Builder
public class RuleRequest {

    @Builder.Default
    private Provider provider = Provider.AWS;

    private Tag excludedTag;
    private Tag includedTag;

    @NonNull
    private Integer amiThreshold;

    @NonNull
    private Double cpuThresholdInstance;
    @NonNull
    private Double cpuThresholdDatabase;

    @NonNull
    private Integer cloudwatchTimeframeDuration;

    @Builder.Default
    private List<String> rulesToIgnore = new ArrayList<String>();

}
