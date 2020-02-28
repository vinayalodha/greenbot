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
package greenbot.main.rules.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.Tag;

/**
 * @author Vinay Lodha
 */
@Component
public class TagAnalyzer {
	private static final String[] POSSIBLE_DEV_TAG_VALUES = { "dev", "development", "staging", "test", "testing" };
	private static final String[] POSSIBLE_DEV_TAG_VALUES_SUBSTRING = append(prepend(POSSIBLE_DEV_TAG_VALUES));
	private static final String[] POSSIBLE_DEV_TAG_VALUES_START = append(POSSIBLE_DEV_TAG_VALUES);
	private static final String[] POSSIBLE_DEV_TAG_VALUES_END = prepend(POSSIBLE_DEV_TAG_VALUES);
	private static final Set<Tag> POSSIBLE_DEV_TAGS = buildPossibleTags(POSSIBLE_DEV_TAG_VALUES);

	public boolean isDevTagPresent(List<Tag> tags) {
		if (tags == null)
			return false;
		Optional<Boolean> searchResult = tags.stream().map(this::isDevTagPresent).filter(o -> o).findAny();
		return searchResult.isPresent();

	}

	private boolean isDevTagPresent(Tag tag) {
		if (POSSIBLE_DEV_TAGS.contains(tag))
			return true;

		String value = tag.getValue().toLowerCase();
		if (StringUtils.containsAny(value, POSSIBLE_DEV_TAG_VALUES_SUBSTRING)) {
			return true;
		}
		if (StringUtils.equalsAny(value, POSSIBLE_DEV_TAG_VALUES)) {
			return true;
		}
		if (StringUtils.startsWithAny(value, POSSIBLE_DEV_TAG_VALUES_START)) {
			return true;
		}
		if (StringUtils.endsWithAny(value, POSSIBLE_DEV_TAG_VALUES_END)) {
			return true;
		}
		return false;
	}

	private static Set<Tag> buildPossibleTags(String[] arr) {
		Set<Tag> tags = new HashSet<>();
		for (String s : arr) {
			tags.add(Tag.builder().key(s).value("true").build());
		}
		return tags;
	}

	private static String[] append(String[] arr) {
		String[] retval = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			retval[i] = arr[i] + "_";
		}
		return retval;
	}

	private static String[] prepend(String[] arr) {
		String[] retval = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			retval[i] = "_" + arr[i];
		}
		return retval;
	}
}
