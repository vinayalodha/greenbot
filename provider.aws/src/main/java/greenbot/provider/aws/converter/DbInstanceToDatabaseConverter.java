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
package greenbot.provider.aws.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import greenbot.rule.model.cloud.Database;
import software.amazon.awssdk.services.rds.model.DBInstance;

/**
 * @author Vinay Lodha
 */
@Component
public class DbInstanceToDatabaseConverter implements Converter<DBInstance, Database> {
	public Database convert(DBInstance instance) {
		// Optional[db.t2.micro]
		return Database.builder()
				.id(instance.dbInstanceIdentifier())
				.name(instance.dbName())
				.instanceClass(instance.dbInstanceClass())
				.engine(instance.engine())
				.build();
	}
}
