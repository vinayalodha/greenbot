package greenbot.provider.service;

import java.util.List;

import greenbot.rule.model.InstanceStorage;
import greenbot.rule.model.Tag;

public interface InstanceStorageService {
	List<InstanceStorage> orphans(Tag includedTag, Tag excludedTag);
}
