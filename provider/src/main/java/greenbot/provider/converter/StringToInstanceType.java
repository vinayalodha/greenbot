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
package greenbot.provider.converter;

import greenbot.rule.model.cloud.InstanceType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * cache.t2.medium
 * or t3a.large
 *
 * @author Vinay Lodha
 */
@Component
public class StringToInstanceType implements Converter<String, InstanceType> {

    @Override
    public InstanceType convert(String source) {
        if (StringUtils.isBlank(source))
            return InstanceType.builder().family("").size("").build();
        return InstanceType.builder()
                .family(StringUtils.substringBeforeLast(source, "."))
                .size(StringUtils.substringAfterLast(source, "."))
                .build();
    }
}
