package greenbot.provider.aws.service;

import java.util.List;

import software.amazon.awssdk.regions.Region;

public interface RegionService {
	public List<Region> regions();
}
