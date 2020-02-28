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
package greenbot.main.testutils;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.ec2.model.InstanceType;

/**
 * @author Vinay Lodha
 */
public class PrintInstanceTypes {

	// https://docs.aws.amazon.com/cli/latest/reference/ec2/describe-instance-types.html
	@Test
	public void sanity() {

		Collection<String> set = InstanceType.knownValues()
				.stream()
				.map(InstanceType::toString)
				.collect(toCollection(TreeSet::new));

		set.forEach(a -> {
			// System.err.println(a);
		});

		assertEquals(271, set.size());
	}
}
