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
package greenbot.main.rules.storage.instance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import greenbot.main.TerraformTest;
import greenbot.main.dataprovider.RuleRequestDataProvider;
import greenbot.main.rules.storage.instance.DeleteOrphanInstanceStorageRule;
import greenbot.main.terraform.TerraformUtils;
import greenbot.rule.model.RuleResponse;

/**
 * 
 * @author Vinay Lodha
 */
@SpringBootTest
public class DeleteOrphanInstanceStorageRuleTest {

	@Autowired
	private DeleteOrphanInstanceStorageRule deleteOrphanInstanceStorageRule;

	@Test
	@TerraformTest
	public void sanity() throws Exception {
		String path = "./src/test/resources/terraform/DeleteOrphanInstanceStorageRule";
		try {
			TerraformUtils.apply(path);
			RuleResponse response = deleteOrphanInstanceStorageRule.doWork(RuleRequestDataProvider.simple());

			// No of rule voilations == 1
			assertEquals(3, response.getItems().size());
		} finally {
			TerraformUtils.destroy(path);
		}
	}
}
