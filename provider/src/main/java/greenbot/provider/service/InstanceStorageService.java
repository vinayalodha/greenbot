package greenbot.provider.service;

import java.util.List;

import greenbot.rule.model.cloud.InstanceStorage;
import greenbot.rule.model.cloud.Tag;

public interface InstanceStorageService {
	List<InstanceStorage> orphans(Tag includedTag, Tag excludedTag);
}
