package greenbot.rule.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponse {
	
	@Singular
	private  List<String> infoMessages = new ArrayList<>();
	
	@Singular
	private  List<String> warningMessages = new ArrayList<>();
	
	@Singular
	private  List<String> errorMessages = new ArrayList<>();
	
	@Singular
	private  List<RuleResponseItem> items = new ArrayList<>();
}
