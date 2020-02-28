/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
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
package greenbot.provider.aws.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.model.Instance;

@Component
@AllArgsConstructor
public class InstanceToComputeConverter implements Converter<Instance, Compute> {

	private Ec2TagToTagConverter ec2TagToTagConverter;

	@Override
	public Compute convert(Instance instance) {
		List<Tag> tags = instance.tags()
				.stream()
				.map(ec2TagToTagConverter::convert)
				.collect(toList());

		return Compute.builder()
				.id(instance.instanceId())
				.tags(tags)
				.build();
	}
}
