package greenbot.provider.aws;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpgradeMapUtils {

    public static Map<String, String> inf1InstanceUpgradeMap() {
        Map<String, String> retval = new LinkedHashMap<>();

        // Milk
        retval.put("g2", "inf1");
        retval.put("g3", "inf1");
        retval.put("p2", "inf1");
        retval.put("p3", "inf1");
        return retval;

    }

    public static Map<String, String> armInstanceUpgradeMap() {
        Map<String, String> retval = new LinkedHashMap<>();

        // Milk
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

        return retval;
    }

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
