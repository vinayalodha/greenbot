package greenbot.provider.aws;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpgradeMapUtils {
    public static Map<String, String> instanceUpgradeMap() {

        Map<String, String> retval = new LinkedHashMap<>();

        // Milk
        retval.put("c1", "c5");
        retval.put("c3", "c5");
        retval.put("c4", "c5");
        retval.put("cc1", "c5");
        retval.put("cc2", "c5");

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

        return retval;
    }

    public static Map<String, String> databaseUpgradeMap() {
        Map<String, String> retVal = new HashMap<>();
        // Milk
        retVal.put("db.t2", "db.t3");
        retVal.put("db.m3", "db.m5");
        retVal.put("db.m4", "db.m5");
        retVal.put("db.r3", "db.r5");
        retVal.put("db.r4", "db.r5");
        return retVal;
    }
}
