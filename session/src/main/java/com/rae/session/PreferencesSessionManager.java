/*
 * Copyright (c) 2017.
 */

package com.rae.session;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * 本地保存的状态
 * Created by ChenRui on 2017/5/2 0002 18:37.
 */
public class PreferencesSessionManager extends SessionManager {

    private final SessionConfig mConfig;
    private final Gson mGson = new Gson();
    private SharedPreferences mSharedPreferences;

    // 用户信息获取比较频繁，作为一个字段去维护
    private Object mUserInfo;

    public PreferencesSessionManager(SessionConfig config) {
        mConfig = config;
        Application context = config.getApplication();
        if (context == null)
            throw new NullPointerException("默认使用的SessionManger为偏好，请初始化Context");
        mSharedPreferences = context.getSharedPreferences(context.getPackageName() + ".session", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLogin() {
        return getUser() != null;
    }

    @Override
    public void clear() {
        super.clear();
        mUserInfo = null; // 清除本地缓存字段
        mSharedPreferences.edit().clear().apply();
    }

    @Override
    @Nullable
    public <T> T getUser() {
        if (mConfig.getUserClass() == null) return null;
        try {
            if (mUserInfo != null) {
                return (T) mUserInfo;
            }
            String json = mSharedPreferences.getString("users", null);
            if (TextUtils.isEmpty(json)) return null;
            return (T) mGson.fromJson(json, mConfig.getUserClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> void setUser(T user) {
        if (user == null) return;
        String json = mGson.toJson(user);
        mSharedPreferences.edit().putString("users", json).apply();
        mUserInfo = user;
        notifyUserInfoChanged();
    }

    @Override
    public <T> void setUserToken(T token) {
        if (token == null) return;
        String json = mGson.toJson(token);
        mSharedPreferences.edit().putString("token", json).apply();
        notifyTokenChanged();
    }

    @Override
    @Nullable
    public <T> T getUserToken() {
        if (mConfig.getUserTokenClass() == null) return null;
        try {
            String json = mSharedPreferences.getString("token", null);
            if (TextUtils.isEmpty(json)) return null;
            return (T) mGson.fromJson(json, mConfig.getUserTokenClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
