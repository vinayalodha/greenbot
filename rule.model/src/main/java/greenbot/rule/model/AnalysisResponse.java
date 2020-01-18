package greenbot.rule.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponse {

    @Singular
    @NonNull
    private List<String> infoMessages;

    @Singular
    @NonNull
    private List<String> warningMessages;

    @Singular
    @NonNull
    private List<String> errorMessages;

    @Singular
    @NonNull
    private List<RuleResponseItem> items;

    public static AnalysisResponse empty() {
        return AnalysisResponse.builder().build();
    }
}
