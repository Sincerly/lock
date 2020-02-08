package com.ysxsoft.lock.config;

import android.os.Environment;

/**
 * 应用配置信息
 */
public class AppConfig {
    public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/ysxsoft/lock";
    public static final String PHOTO_CACHE_PATH = BASE_PATH + "/image/cache";
    public static final String PHOTO_PATH = BASE_PATH + "/image/";
    public static final String APK_URL = BASE_PATH + "/apk/";
    public static final String BASE_URL = "http://linlilinwaiv2.oss-cn-shanghai.aliyuncs.com/";
    public static final boolean IS_DEBUG_ENABLED = true;
    public static final boolean IS_TEST_LOGIN = true;
    public static AppConfig INSTANCE;

    public static synchronized AppConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
        }
        return INSTANCE;
    }
}
