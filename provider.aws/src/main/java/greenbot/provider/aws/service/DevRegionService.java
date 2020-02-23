package greenbot.provider.aws.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;

@Service
@Profile("dev")
public class DevRegionService implements RegionService{
	
	@Override
	public List<Region> regions() {
		return Collections.singletonList(Region.US_EAST_2);
	}
}
