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
package greenbot.rule.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponse {

	@Singular
	@NonNull
	private List<String> infoMessages;

	@Singular
	@NonNull
	private List<String> warningMessages;

	@Singular
	@NonNull
	private List<String> errorMessages;

	@Singular
	@NonNull
	private List<RuleResponseItem> items;

	public static RuleResponse empty() {
		return RuleResponse.builder().build();
	}
}
