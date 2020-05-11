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
package greenbot.main.repository;

import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import greenbot.rule.model.RuleResponse;

/**
 * @author Vinay Lodha
 */
@Repository
public class ReportRepository {

	private @NonNull Cache<Integer, RuleResponse> cache = Caffeine.newBuilder()
			.expireAfterWrite(365, TimeUnit.DAYS)
			.maximumSize(100)
			.build();

	public void save(RuleResponse ruleResponse) {
		cache.put(ruleResponse.getId(), ruleResponse);
	}

	public RuleResponse get(int id) {
		return cache.getIfPresent(id);
	}

}
