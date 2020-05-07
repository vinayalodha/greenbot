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
package greenbot.rule.model.utils;

import java.util.function.BinaryOperator;

import greenbot.rule.model.RuleResponse;

/**
 * 
 * @author Vinay Lodha
 */
public class RuleResponseReducer implements BinaryOperator<RuleResponse> {

	@Override
	public RuleResponse apply(RuleResponse one, RuleResponse two) {
		return RuleResponse.builder()
				.infoMessages(one.getInfoMessages())
				.infoMessages(two.getInfoMessages())
				.warningMessages(one.getWarningMessages())
				.warningMessages(two.getWarningMessages())
				.errorMessages(one.getErrorMessages())
				.errorMessages(two.getErrorMessages())
				.items(one.getItems())
				.items(two.getItems())
				.build();

	}

}
