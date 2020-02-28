/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
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
package greenbot.provider.aws.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;

@Service
@Profile("dev")
public class DevRegionService implements RegionService {

	@Override
	public List<Region> regions() {
		return Collections.singletonList(Region.US_EAST_2);
	}
}
