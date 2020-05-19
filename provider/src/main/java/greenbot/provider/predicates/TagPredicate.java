/*
 * Copyright 2019-2020 Vinay Lodha (https://github.com/vinay-lodha)
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
package greenbot.provider.predicates;

import greenbot.rule.model.cloud.Resource;
import greenbot.rule.model.cloud.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.function.Predicate;

/**
 * @author Vinay Lodha
 */
@Data
@Builder
public class TagPredicate implements Predicate<Resource> {
    private final Tag includedTag;
    private final Tag excludedTag;

    @Override
    public boolean test(Resource compute) {
        return (includedTag == null || compute.getTags().values().contains(includedTag))
                && (excludedTag == null || !compute.getTags().values().contains(excludedTag));
    }
}
