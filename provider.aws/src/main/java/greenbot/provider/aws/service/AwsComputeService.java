package greenbot.provider.aws.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.ComputeService;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.paginators.DescribeInstancesIterable;

@Service
@AllArgsConstructor
public class AwsComputeService implements ComputeService {

	private RegionService regionService;
	private ConversionService conversionService;

	@Override
	@Cacheable("awsComputeService")
	public List<Compute> list(Tag includedTag, Tag excludedTag) {
		return regionService.regions()
				.parallelStream()
				.map(region -> Ec2Client.builder().region(region).build())
				.map(Ec2Client::describeInstancesPaginator)
				.flatMap(DescribeInstancesIterable::stream)
				.map(DescribeInstancesResponse::reservations)
				.flatMap(Collection::stream)
				.map(Reservation::instances)
				.flatMap(Collection::stream)
				.filter(instance -> {
					String name = instance.state().nameAsString();
					if (name.equalsIgnoreCase("stopped") || name.equalsIgnoreCase("running")) {
						return true;
					}
					return false;
				})
				.map(this::convert)
				.filter(compute -> {
					return includedTag == null || compute.getTags().contains(includedTag);
				})
				.filter(compute -> {
					return excludedTag == null || !compute.getTags().contains(excludedTag);
				})
				.collect(Collectors.toList());
	}

	private Compute convert(Instance instance) {
		return conversionService.convert(instance, Compute.class);
	}
}
