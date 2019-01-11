package com.rae.session;

import android.app.Application;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Session配置信息
 * Created by ChenRui on 2018/3/26 0026 14:54.
 */
public class SessionConfig {
    private Class<?> userTokenClass;
    private Class<?> userClass;
    private WeakReference<Application> mApplication;
    private String configName;

    public Class<?> getUserTokenClass() {
        return userTokenClass;
    }

    public void setUserTokenClass(Class<?> userTokenClass) {
        this.userTokenClass = userTokenClass;
    }

    public Class<?> getUserClass() {
        return userClass;
    }

    public void setUserClass(Class<?> userClass) {
        this.userClass = userClass;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @Nullable
    public Application getApplication() {
        if (mApplication == null) return null;
        return mApplication.get();
    }

    public void setApplication(Application application) {
        mApplication = new WeakReference<>(application);
    }
}
