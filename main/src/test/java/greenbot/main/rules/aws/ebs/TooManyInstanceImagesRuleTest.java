package greenbot.main.rules.aws.ebs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import greenbot.main.TerraformTest;
import greenbot.main.dataprovider.RuleRequestDataProvider;
import greenbot.main.terraform.TerraformUtils;
import greenbot.rule.model.RuleResponse;

@SpringBootTest
public class TooManyInstanceImagesRuleTest {

	@Autowired
	private TooManyInstanceImagesRule tooManyInstanceImagesRule;

	@Test
	@TerraformTest
	public void sanity() throws Exception {
		String path = "./src/test/resources/terraform/TooManyInstanceImagesRule";
		try {
			TerraformUtils.apply(path);
			RuleResponse response = tooManyInstanceImagesRule.doWork(RuleRequestDataProvider.simple());
			assertEquals(1, response.getItems().size());
		} finally {
			TerraformUtils.destroy(path);
		}
	}
}
