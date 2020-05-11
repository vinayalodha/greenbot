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
package greenbot.main.controller;

import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import greenbot.main.model.ui.AnalysisRequest;
import greenbot.main.repository.ReportRepository;
import greenbot.main.rules.service.RuleLifecycleManager;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import lombok.AllArgsConstructor;

/**
 * 
 * @author Vinay Lodha
 */
@RestController
@AllArgsConstructor
public class RuleController {

	private final ConversionService conversionService;
	private final RuleLifecycleManager ruleLifecycleManager;
	private final ReportRepository reportRepository;

	@GetMapping("rule/info")
	public List<RuleInfo> getRulePermission() {
		return ruleLifecycleManager.getRuleInfos();
	}

	@GetMapping("rule/config")
	public List<ConfigParam> getConfig() {
		return ruleLifecycleManager.getConfigParams();
	}

	@GetMapping(value = "report/{id}", produces = "text/csv")
	public ResponseEntity<String> downloadCSV(@PathVariable(value = "id") int id) {
		RuleResponse response = reportRepository.get(id);
		String retVal = conversionService.convert(response, String.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "text/csv");
		headers.add("Content-disposition", "attachment;filename=report.csv");

		return new ResponseEntity<String>(retVal, headers, HttpStatus.OK);
	}

	@PostMapping("rule")
	public RuleResponse post(@RequestBody AnalysisRequest request) {
		RuleResponse response = ruleLifecycleManager.execute(conversionService.convert(request, RuleRequest.class));
		reportRepository.save(response);
		return response;
	}
}
