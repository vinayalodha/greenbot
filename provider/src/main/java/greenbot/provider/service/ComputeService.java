package greenbot.provider.service;

import java.util.List;

import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;

public interface ComputeService {
	List<Compute> list(Tag includedTag, Tag excludedTag);
}
