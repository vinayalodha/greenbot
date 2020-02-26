package greenbot.main.rules.aws.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import greenbot.main.TerraformTest;
import greenbot.main.dataprovider.RuleRequestDataProvider;
import greenbot.main.terraform.TerraformUtils;
import greenbot.rule.model.RuleResponse;

@SpringBootTest
public class DevResourcesRuleTest {

	@Autowired
	private DevResourcesRule devResourcesRule;

	@Test
	@TerraformTest
	public void sanity() throws Exception {
		String path = "./src/test/resources/terraform/DevResourcesRule";
		try {
			TerraformUtils.apply(path);
			RuleResponse response = devResourcesRule.doWork(RuleRequestDataProvider.simple());
			assertEquals(1, response.getItems().size());
			assertEquals(1, response.getItems().get(0).getResourceIds().size());
		} finally {
			TerraformUtils.destroy(path);
		}
	}
}
