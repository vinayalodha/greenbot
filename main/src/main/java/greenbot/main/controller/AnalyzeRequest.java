package greenbot.main.controller;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AnalyzeRequest {
	private final long timeframe;
	private final String configJson;
}
