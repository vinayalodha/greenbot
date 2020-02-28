package greenbot.provider.aws.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.Tag;

@Component
public class Ec2TagToTagConverter implements Converter<software.amazon.awssdk.services.ec2.model.Tag, Tag> {

	@Override
	public Tag convert(software.amazon.awssdk.services.ec2.model.Tag tag) {
		return Tag.builder()
				.key(tag.key())
				.value(tag.value())
				.build();
	}

}
