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
package greenbot.main.rules.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import greenbot.main.config.ConfigService;
import greenbot.provider.aws.service.RegionService;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.GreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.utils.RuleResponseReducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vinay Lodha
 */
@Slf4j
@Service
@AllArgsConstructor
public class RuleLifecycleManager {

	private final List<GreenbotRule> rules;
	private final RuleResponseReducer responseReducer;
	private final ConfigService configParamUtils;
	private final RegionService regionService;

	public RuleResponse execute(RuleRequest request) {
		List<String> errorMessages = new ArrayList<>();

		String message = checkIfAWSCliConfigured();
		if (message != null) {
			return RuleResponse.builder()
					.id(Math.abs(new Random().nextInt()))
					.errorMessage(
							"Unable to load AWS regions. most likely AWS CLI is not configured or network connectivity with AWS API is unavailable. Exception Stacktrace : "
									+ message)
					.build();
		}
		log.info("Rule execution started");
		RuleResponse response = rules.stream()
				.map(rule -> {
					String ruleId = rule.ruleInfo().getId();
					if (request.getRulesToIgnore().contains(ruleId)) {
						log.info("skipping rule:" + ruleId);
						return null;
					}
					try {
						log.info(String.format("Execution of rule:%s started", ruleId));
						RuleResponse retval = rule.doWork(request);
						log.info(String.format("Execution of rule:%s done", ruleId));
						return retval;
					} catch (Exception e) {
						log.error(String.format(
								"Exception occoured while executing rule:%s Please raise bug report if issue persist",
								ruleId), e);
						errorMessages.add("rule:" + ruleId + " - " + exceptionToString(e));
					}
					return null;
				})
				.filter(Objects::nonNull)
				.filter(ruleResponse -> CollectionUtils.isNotEmpty(ruleResponse.getItems()))
				.reduce(responseReducer)
				.orElse(RuleResponse.builder().build());
		log.info("Rule execution done");
		return response.toBuilder()
				.errorMessages(errorMessages)
				.id(Math.abs(new Random().nextInt()))
				.build();
	}

	private String checkIfAWSCliConfigured() {
		try {
			regionService.regions();
			return null;
		} catch (Exception e) {
			log.error("", e);
			return exceptionToString(e);
		}
	}

	private String exceptionToString(Exception e) {
		return StringUtils
				.abbreviate(ExceptionUtils.getRootCauseMessage(e) + ExceptionUtils.getRootCauseStackTrace(e), 400);
	}

	public List<ConfigParam> getConfigParams() {
		return configParamUtils.getDefaultConfig();
	}

	public List<RuleInfo> getRuleInfos() {
		return rules.stream().map(GreenbotRule::ruleInfo).collect(Collectors.toList());
	}

}
