package com.paisheng.bi;

public class constant {
    public static final String ASPECT =
            "package bi;\n" +
            "import com.alibaba.android.arouter.launcher.ARouter;\n" +
            "import com.paisheng.commonbiz.arouter.BiARouterConstant;\n" +
            "import com.paisheng.commonbiz.bi.local.ILocalRecordIProvider;\n" +
            "import com.paisheng.commonbiz.bi.sensors.ISensorsRecordIProvider;\n" +
            "import com.paisheng.commonbiz.bi.um.IUMRecordIProvider;\n" +
            "import org.aspectj.lang.annotation.Aspect;\n" +
            "import org.aspectj.lang.annotation.Before;\n" +
            "\n" +
            "import java.util.Map;\n" +
            "\n" +
            "public class %s {\n" +
            "\n" +
            "}";

    public static final String NOTE =
            "package bi;\n" +
            "import java.lang.annotation.ElementType;\n" +
            "import java.lang.annotation.Retention;\n" +
            "import java.lang.annotation.RetentionPolicy;\n" +
            "import java.lang.annotation.Target;\n" +
            "\n" +
            "\n" +
            "@Target({ElementType.METHOD})\n" +
            "@Retention(RetentionPolicy.CLASS)\n" +
            "public @interface %s {\n" +
            "   \n" +
            "}";

    public static final String SENSORS_ASPECT = "@Aspect\n" +
            "    public static class Sensors {\n" +
            "        private ISensorsRecordIProvider getSensorsRecordIProvider() {\n" +
            "            return (ISensorsRecordIProvider) ARouter.getInstance().build(BiARouterConstant.BI_SENSROS).navigation();\n" +
            "        }\n" +
            "\n" +
            "        private void toRecordTrack(String actionKey, String recordKey, Map<String, Object> value) {\n" +
            "            ISensorsRecordIProvider mSensorsRecordIProvider = getSensorsRecordIProvider();\n" +
            "            if (mSensorsRecordIProvider != null) {\n" +
            "                mSensorsRecordIProvider.actionRecord(actionKey, recordKey, value);\n" +
            "            }\n" +
            "        }\n" +
            "    }";

    public static final String UM_ASPECT = "@Aspect\n" +
            "    public static class Um {\n" +
            "        private IUMRecordIProvider getUMRecordIProvider() {\n" +
            "            return (IUMRecordIProvider) ARouter.getInstance().build(BiARouterConstant.BI_UM).navigation();\n" +
            "        }\n" +
            "    }";

    public static final String LOCAL_ASPECT = "@Aspect\n" +
            "    public static class Local {\n" +
            "        private ILocalRecordIProvider getLocalRecordIProvider() {\n" +
            "            return (ILocalRecordIProvider) ARouter.getInstance().build(BiARouterConstant.BI_LOCAL).navigation();\n" +
            "        }\n" +
            "    }";

    public static final String ASPECT_SENSORS_METHOD = "@Before(\"execution(@bi.%s * *(..))\")\n" +
            "        public void sensors%s(JoinPoint joinPoint) {\n" +
            "            boolean checked = true;\n%s" +
            "            if (checked) {\n%s" +
            "            }\n" +
            "        }";

    public static final String ASPECT_UM_METHOD = "@Before(\"execution(@bi.%s * *(..))\")\n" +
            "        public void um%s(JoinPoint joinPoint) {\n" +
            "            IUMRecordIProvider mUMRecordIProvider = getUMRecordIProvider();\n" +
            "            boolean checked = mUMRecordIProvider != null;\n%s" +
            "            if (checked) {\n%s" +
            "            }\n" +
            "        }";

    public static final String ASPECT_LOCAL_METHOD = "@Before(\"execution(@bi.%s * *(..))\")\n" +
            "        public void local%s(JoinPoint joinPoint) {\n" +
            "            ILocalRecordIProvider mLocalRecordIProvider = getLocalRecordIProvider();\n" +
            "            boolean checked = mLocalRecordIProvider != null;\n%s" +
            "            if (checked) {\n%s" +
            "            }\n" +
            "        }";

    public static final String ASPECT_ANOTE = "@Target({ElementType.METHOD})\n" +
            "        @Retention(RetentionPolicy.CLASS)\n" +
            "        @interface %s {\n" +
            "        }";
}
