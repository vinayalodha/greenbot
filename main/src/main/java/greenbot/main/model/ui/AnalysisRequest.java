package greenbot.main.model.ui;

import java.util.HashMap;
import java.util.Map;

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

    @Builder.Default
    private Map<String, ConfigParam> configParams = new HashMap<String, ConfigParam>();

}
