package greenbot.main.rules.aws.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import greenbot.main.TerraformTest;
import greenbot.main.terraform.TerraformUtils;
import greenbot.rule.model.RuleResponse;

@SpringBootTest
public class DevResourcesRuleITTest {

	@Autowired
	private DevResourcesRule devResourcesRule;
	@Test
	@TerraformTest
	public void sanity() throws Exception {
		String path = "./src/test/resources/terraform/DevResourcesRule";
		try {
			TerraformUtils.apply(path);
			RuleResponse response = devResourcesRule.doWork(null);
			assertEquals(1, response.getItems().size());
		} finally {
			TerraformUtils.destroy(path);
		}
	}
}
