package greenbot.rule.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponse {
	
	@Singular
	@NonNull
	private  List<String> infoMessages;
	
	@Singular
	@NonNull
	private  List<String> warningMessages;
	
	@Singular
	@NonNull
	private  List<String> errorMessages;
	
	@Singular
	@NonNull
	private  List<RuleResponseItem> items;
}
