package greenbot.main.model.ui;

import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

	private List<ConfigParam> configParams;

}
