package greenbot.provider.aws.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.InstanceStorage;
import greenbot.rule.model.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVolumesResponse;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.ec2.paginators.DescribeVolumesIterable;

@Service
@AllArgsConstructor
public class AwsInstanceStorageService implements InstanceStorageService {

	private RegionService regionService;
	private ConversionService conversionService;

	@Override
	@Cacheable("AwsInstanceStorageService")
	public List<InstanceStorage> orphans(Tag includedTag, Tag excludedTag) {

		return regionService.regions()
				.parallelStream()
				.map(region -> Ec2Client.builder().region(region).build())
				.map(Ec2Client::describeVolumesPaginator)
				.flatMap(DescribeVolumesIterable::stream)
				.map(DescribeVolumesResponse::volumes)
				.flatMap(Collection::stream)
				.filter(volume -> StringUtils.equalsAnyIgnoreCase("available", volume.stateAsString()))
				.map(this::convert)
				.filter(compute -> {
					return includedTag == null || compute.getTags().contains(includedTag);
				})
				.filter(compute -> {
					return excludedTag == null || !compute.getTags().contains(excludedTag);
				})
				.collect(Collectors.toList());
	}

	private InstanceStorage convert(Volume volume) {
		return conversionService.convert(volume, InstanceStorage.class);
	}
}
