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
package greenbot.provider.aws.utils;

import static org.apache.commons.lang3.StringUtils.equalsAny;

import java.util.Arrays;
import java.util.List;

import greenbot.rule.model.cloud.InstanceType;

/**
 * https://aws.amazon.com/ec2/instance-types/
 * https://aws.amazon.com/ec2/previous-generation/
 * 
 * @author Vinay Lodha
 */
public class InstanceTypeUtils {

	public static final List<String> CPU_INTENSIVE_CPU_FAMILY_LIST = Arrays.asList("c5", "c5d", "c5n", "c4", "c3",
			"c1");
	public static final List<String> GENERAL_PURPOSE_FAMILY_LIST = Arrays.asList("a1", "t3", "t3a", "t2", "m6g", "m5d",
			"m5", "m5a",
			"m5ad",
			"m5n", "m5dn", "m4", "m1", "t1", "m3");

	public static boolean isG2G3(InstanceType instanceType) {
		return equalsAny(instanceType.getFamily(), "g2", "g3");
	}

	public static boolean isG4(InstanceType instanceType) {
		return equalsAny(instanceType.getFamily(), "g4");
	}

	public static boolean isAmd(InstanceType instanceType) {
		return equalsAny(instanceType.getFamily(), "t3a", "m5a", "m5ad");
	}

	public static boolean isCpuIntensive(InstanceType instanceType) {
		return CPU_INTENSIVE_CPU_FAMILY_LIST.contains(instanceType.getFamily());
	}

	public static boolean isGeneralPurpose(InstanceType instanceType) {
		return GENERAL_PURPOSE_FAMILY_LIST.contains(instanceType.getFamily());
	}
}
