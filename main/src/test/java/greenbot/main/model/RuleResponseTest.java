package greenbot.main.model;

import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RuleResponseTest {
    @Test
    public void sort() {
        List<RuleResponseItem> items = Arrays.asList(RuleResponseItem.builder().confidence(AnalysisConfidence.HIGH).build(),
                RuleResponseItem.builder().confidence(null).build(),
                RuleResponseItem.builder().confidence(AnalysisConfidence.LOW).build(),
                RuleResponseItem.builder().confidence(AnalysisConfidence.MEDIUM).build());
        RuleResponse build = RuleResponse.build(items);
        Assertions.assertEquals(AnalysisConfidence.HIGH, build.getItems().first().getConfidence());
        Assertions.assertEquals(null, build.getItems().last().getConfidence());

    }
}
