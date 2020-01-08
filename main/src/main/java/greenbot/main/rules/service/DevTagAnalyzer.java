package greenbot.main.rules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import greenbot.main.model.Tag;

// TODO
@Component
public class DevTagAnalyzer {
	public boolean isDevTagPresent(List<Tag> tags) {
		if (tags == null)
			return false;
		Optional<Boolean> searchResult = tags.stream()
				.map(this::isDevTagPresent)
				.filter(o -> o)
				.findAny();
		return searchResult.isPresent();

	}

	private boolean isDevTagPresent(Tag tag) {
		
		return false;
	}
}
