/*
 * Copyright 2019-2020 Vinay Lodha (https://github.com/vinay-lodha)
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
package greenbot.provider.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Vinay Lodha
 */
public class OptionalUtils {
	public static <T> List<T> buildList(List<Optional<T>> optionals) {
		List<T> retval = new ArrayList<T>();
		for (Optional<T> obj : optionals) {
			obj.ifPresent(retval::add);
		}
		return retval;
	}
}
