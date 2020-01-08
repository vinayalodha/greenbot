package greenbot.main.model;

import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AnalysisRequest {
	private final List<ConfigParam> configParams;
}
