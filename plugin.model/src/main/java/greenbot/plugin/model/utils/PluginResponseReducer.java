package greenbot.plugin.model.utils;

import java.util.function.BinaryOperator;

import greenbot.plugin.model.PluginResponse;

public class PluginResponseReducer implements BinaryOperator<PluginResponse>{
	
	@Override
	public PluginResponse apply(PluginResponse one, PluginResponse two) {
		return PluginResponse.builder()
				.infoMessages(one.getInfoMessages())
				.infoMessages(two.getInfoMessages())
				.warningMessages(one.getWarningMessages())
				.warningMessages(two.getWarningMessages())
				.errorMessages(one.getErrorMessages())
				.errorMessages(two.getErrorMessages())
				.items(one.getItems())
				.items(two.getItems())
				.build();
		
	}

}
