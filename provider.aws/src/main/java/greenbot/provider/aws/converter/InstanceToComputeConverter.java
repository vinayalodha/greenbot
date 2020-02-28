package greenbot.provider.aws.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.model.Instance;

@Component
@AllArgsConstructor
public class InstanceToComputeConverter implements Converter<Instance, Compute> {

	private Ec2TagToTagConverter ec2TagToTagConverter;

	@Override
	public Compute convert(Instance instance) {
		List<Tag> tags = instance.tags()
				.stream()
				.map(ec2TagToTagConverter::convert)
				.collect(toList());

		return Compute.builder()
				.id(instance.instanceId())
				.tags(tags)
				.build();
	}
}
