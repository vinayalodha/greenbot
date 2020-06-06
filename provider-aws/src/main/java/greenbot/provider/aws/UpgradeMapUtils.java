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
package greenbot.provider.aws;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Last checked June 6 2020
 * 
 * @author Vinay Lodha
 *
 */
public class UpgradeMapUtils {

    public static Optional<String> inf1InstanceUpgradeMap(String key) {
        Map<String, String> retval = new LinkedHashMap<>();

        retval.put("g2", "inf1");
        retval.put("g3", "inf1");
        retval.put("p2", "inf1");
        retval.put("p3", "inf1");
        return Optional.ofNullable(retval.get(key));

    }

    public static Optional<String> elasticCacheUpgradeMap(String key) {
        Map<String, String> retval = new LinkedHashMap<>();
        retval.put("cache.t2", "cache.t3");
        retval.put("cache.m3", "cache.m5");
        retval.put("cache.m4", "cache.m5");
        retval.put("cache.r3", "cache.r5");
        retval.put("cache.r4", "cache.r5");
        return Optional.ofNullable(retval.get(key));
    }

    public static Optional<String> armInstanceUpgradeMap(String key) {
        Map<String, String> retval = new LinkedHashMap<>();

        retval.put("t1", "a1");
        retval.put("t2", "a1");
        retval.put("t3", "a1");
        retval.put("t3a", "a1");

        retval.put("r3", "r6g");
        retval.put("r4", "r6g");
        retval.put("r5", "r6g");
        retval.put("r5d", "r6g");
        retval.put("r5a", "r6g");
        retval.put("r5ad", "r6g");

        retval.put("m1", "m6g");
        retval.put("m3", "m6g");
        retval.put("m4", "m6g");
        retval.put("m5", "m6g");
        retval.put("m5d", "m6g");
        retval.put("ma", "m6g");
        retval.put("m5ad", "m6g");

        retval.put("c1", "c6g");
        retval.put("c3", "c6g");
        retval.put("c4", "c6g");
        retval.put("cc1", "c6g");
        retval.put("cc2", "c6g");
        retval.put("c5", "c6g");
        retval.put("c5d", "c6g");
        retval.put("c5n", "c6g");
        retval.put("c5a", "c6g");

        return Optional.ofNullable(retval.get(key));
    }

    public static Optional<String> instanceUpgradeMap(String key) {

        Map<String, String> retval = new LinkedHashMap<>();

        retval.put("c1", "c5a");
        retval.put("c3", "c5a");
        retval.put("c4", "c5a");
        retval.put("cc1", "c5a");
        retval.put("cc2", "c5a");
        retval.put("c5", "c5a");

        retval.put("g2", "g4dn");
        retval.put("g3", "g4dn");
        retval.put("g3s", "g4dn");

        retval.put("i2", "i3");

        retval.put("m1", "m5a");
        retval.put("m3", "m5a");
        retval.put("m4", "m5a");
        retval.put("m5", "m5a");
        retval.put("m5d", "m5ad");

        retval.put("p2", "p3");

        retval.put("r3", "r5a");
        retval.put("r4", "r5a");
        retval.put("r5", "r5a");
        retval.put("r5d", "r5ad");

        retval.put("t1", "t3a");
        retval.put("t2", "t3a");
        retval.put("t3", "t3a");

        return Optional.ofNullable(retval.get(key));
    }

    public static Optional<String> databaseUpgradeMap(String key) {
        Map<String, String> retVal = new HashMap<>();
        retVal.put("db.t2", "db.t3");
        retVal.put("db.m3", "db.m5");
        retVal.put("db.m4", "db.m5");
        retVal.put("db.r3", "db.r5");
        retVal.put("db.r4", "db.r5");
        return Optional.ofNullable(retVal.get(key));
    }
}
