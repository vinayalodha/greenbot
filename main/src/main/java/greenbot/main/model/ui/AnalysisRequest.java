package greenbot.main.model.ui;

import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AnalysisRequest {

    private final List<ConfigParam> configParams;

}
