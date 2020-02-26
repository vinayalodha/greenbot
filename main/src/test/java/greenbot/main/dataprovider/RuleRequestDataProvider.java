package greenbot.main.dataprovider;

import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RuleRequestDataProvider {

	public RuleRequest simple() {
		return RuleRequest.builder()
				.includedTag(Tag.builder().key("owner").value("greenbot").build())
				.build();
	}

}
