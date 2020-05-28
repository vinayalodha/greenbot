/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenbot.main.rules.service;

import greenbot.rule.model.cloud.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vinay Lodha
 */
public class DevTagAnalyzerTest {

    private final TagAnalyzer devTagAnalyzer = new TagAnalyzer();

    @ParameterizedTest
    @CsvFileSource(resources = "/dev-tags.csv")
    void withCsvSource(String key, String value, Boolean outcome) {
        Tag tag = Tag.builder().key(key).value(value).build();
        assertEquals(outcome, devTagAnalyzer.isDevTagPresent(Arrays.asList(tag)));
    }
}
