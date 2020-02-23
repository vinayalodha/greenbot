package greenbot.provider.aws.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;

@Service
@Profile("default")
public class ProdRegionService implements RegionService {
	
	@Override
	@Cacheable("ProdRegionService")
	public List<Region> regions() {
		Ec2Client client = Ec2Client.builder().build();

		DescribeRegionsRequest request = DescribeRegionsRequest.builder().allRegions(true).build();
		DescribeRegionsResponse response = client.describeRegions(request);
		return response.regions()
				.stream()
				.filter(re -> !"not-opted-in".equalsIgnoreCase(re.optInStatus()))
				.map(re -> Region.of(re.regionName()))
				.collect(toList());
	}
}
