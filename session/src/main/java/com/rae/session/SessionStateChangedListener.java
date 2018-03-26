package com.rae.session;

/**
 * 会话状态改变监听器
 * Created by ChenRui on 2018/3/26 0026 15:07.
 */
public interface SessionStateChangedListener {

    /**
     * 用户信息改变
     */
    void onUserInfoChanged(SessionManager sessionManager);

    /**
     * 访问凭证发生改变
     */
    void onTokenInfoChanged(SessionManager sessionManager);

}
