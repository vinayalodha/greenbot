package greenbot.provider.service;

import greenbot.rule.model.cloud.Tag;

public interface InstanceImageService {
	boolean isGreaterThanThreshold(int threshold, Tag includedTag, Tag excludedTag);

}
