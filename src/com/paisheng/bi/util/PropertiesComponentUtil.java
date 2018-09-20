package com.paisheng.bi.util;

import com.intellij.ide.util.PropertiesComponent;

public class PropertiesComponentUtil {
    private static final String BI_CONFIG_DATA = "com.paisheng.bi.util.propertiescomponentutil.bi.config.data";
    private static final String NAME_REGEX = "^Bi[a-zA-Z]+Note.(Local|Um|Sensors).[a-zA-Z0-9]+$";


    public static String getBiConfigData() {
        return PropertiesComponent.getInstance().getValue(BI_CONFIG_DATA, NAME_REGEX);
    }

    public static void setBiConfigData(String data) {
        PropertiesComponent.getInstance().setValue(BI_CONFIG_DATA, data);
    }
}
