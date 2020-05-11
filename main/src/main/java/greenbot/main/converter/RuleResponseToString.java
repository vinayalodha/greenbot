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
package greenbot.main.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.RuleResponse;

/**
 * Convert it to csv
 * 
 * @author Vinay Lodha
 */
@Component
public class RuleResponseToString implements Converter<RuleResponse, String> {

	@Override
	public String convert(RuleResponse ruleResponse) {

		String header = "ResourceId;Rule Id;Confidence;message\r\n";
		Optional<String> result = ruleResponse.getItems().stream()
				.map(i -> {
					return String.format("%s;%s;%s;%s", i.getResourceId(), i.getRuleId(), i.getConfidence(),
							i.getMessage());
				})
				.reduce((a, b) -> {
					return a + "\r\n" + b;
				});
		return header + result.orElse("");
	}
}
