package greenbot.provider.aws.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.InstanceStorage;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.model.Volume;

@Component
@AllArgsConstructor
public class VolumeToInstanceStorageConverter implements Converter<Volume, InstanceStorage> {

	private Ec2TagToTagConverter ec2TagToTagConverter;

	@Override
	public InstanceStorage convert(Volume source) {
		List<Tag> tags = source.tags()
				.stream()
				.map(ec2TagToTagConverter::convert)
				.collect(toList());

		return InstanceStorage.builder()
				.id(source.volumeId())
				.tags(tags)
				.build();
	}

}
