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
package greenbot.main.rules.instance;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.predicates.InstanceFamilyPredicate;
import greenbot.provider.predicates.InstanceTypePredicate;
import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static greenbot.provider.aws.utils.InstanceTypeUtils.CPU_INTENSIVE_CPU_FAMILY_LIST;
import static greenbot.provider.aws.utils.InstanceTypeUtils.GENERAL_PURPOSE_FAMILY_LIST;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author Vinay Lodha
 */
@Component
public class UnderUtilizedInstanceRule extends AbstractGreenbotRule implements InitializingBean {

    private static final Collection<String> GP_CPU_FAMILIES = Stream.of(
            GENERAL_PURPOSE_FAMILY_LIST, CPU_INTENSIVE_CPU_FAMILY_LIST)
            .flatMap(Collection::stream)
            .collect(toList());

    private static final String INSTANCE_CSV = join(GP_CPU_FAMILIES);

    @Value("${rules.UnderutilizedInstanceCpuRule.instance_types_to_ignore}")
    private String instanceTypesToIgnore;

    @Autowired
    private ComputeService computeService;

    @Autowired
    private ConversionService conversionService;

    private InstanceFamilyPredicate instanceFamilyPredicate;
    private InstanceTypePredicate instanceTypePredicate;

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        TagPredicate predicate = conversionService.convert(ruleRequest, TagPredicate.class);
        List<Predicate<Compute>> predicates = Arrays.asList(predicate::test, instanceFamilyPredicate, instanceTypePredicate);

        List<Compute> computes = computeService.list(predicates);
        List<PossibleUpgradeInfo> underUtilized = computeService.findUnderUtilized(computes, ruleRequest.getCloudwatchTimeframeDuration(), ruleRequest.getCpuThresholdInstance());
        List<RuleResponseItem> items = underUtilized
                .stream()
                .map(info -> ConversionUtils.toRuleResponseItem(info, buildRuleId()))
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances", "cloudwatch:GetMetricStatistics"));
    }

    @Override
    public void afterPropertiesSet() {
        instanceFamilyPredicate = InstanceFamilyPredicate.builder()
                .allowedFamilies(GP_CPU_FAMILIES)
                .build();

        String[] split = StringUtils.split(instanceTypesToIgnore, ",");
        instanceTypePredicate = InstanceTypePredicate.builder()
                .instanceTypesToIgnore(Arrays.asList(split))
                .build();
    }

}
