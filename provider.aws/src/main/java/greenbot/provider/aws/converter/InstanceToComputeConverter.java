package greenbot.provider.aws.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.provider.model.Compute;
import greenbot.provider.model.Tag;
import software.amazon.awssdk.services.ec2.model.Instance;

@Component
public class InstanceToComputeConverter implements Converter<Instance, Compute> {

	@Override
	public Compute convert(Instance instance) {
		List<Tag> tags = instance.tags()
				.stream()
				.map(this::toTag)
				.collect(toList());
		
		return Compute.builder()
				.id(instance.instanceId())
				.tags(tags)
				.build();
	}

	private Tag toTag(software.amazon.awssdk.services.ec2.model.Tag tag) {
		return Tag.builder()
				.key(tag.key())
				.value(tag.value())
				.build();
	}
}
