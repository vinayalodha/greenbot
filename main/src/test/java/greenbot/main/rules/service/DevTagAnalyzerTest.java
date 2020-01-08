package greenbot.main.rules.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import greenbot.main.model.Tag;

public class DevTagAnalyzerTest {
	
	private DevTagAnalyzer devTagAnalyzer = new DevTagAnalyzer();
	
	
	//@ParameterizedTest
	//@CsvFileSource(resources = "/dev-tags.csv.csv")
	void withCsvSource(String key, String value, Boolean outcome ) {
		Tag tag = Tag.builder().key(key).value(value).build();
		assertEquals(outcome, Arrays.asList(tag));
	}


}
