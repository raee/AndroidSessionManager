package com.rae.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rae.session.SessionManager;
import com.rae.session.SessionStateChangedListener;
import com.rae.session.SessionUserInfo;

/**
 * 演示
 * Created by ChenRui on 2018/3/26 0026 15:21.
 */
public class MainActivity extends AppCompatActivity implements SessionStateChangedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SessionManager.init(new SessionManager.Builder().withContext(getApplication()).build());
        SessionManager.getDefault().addSessionStateChangedListener(this);
        findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUserInfo userInfo = new SessionUserInfo();
                userInfo.setName("测试写入" + System.currentTimeMillis());
                SessionManager.getDefault().setUser(userInfo);

                // 读取
                findViewById(R.id.btn_get).performClick();
            }
        });
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUserInfo userInfo = SessionManager.getDefault().getUser();
                Toast.makeText(v.getContext(), userInfo == null ? "没有登录" : userInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getDefault().clear();
                Toast.makeText(v.getContext(), "清除Session", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SessionManager.getDefault().removeSessionStateChangedListener(this);
    }

    @Override
    public void onUserInfoChanged(SessionManager sessionManager) {
        Log.i("rae", "用户信息发生改变！");
    }

    @Override
    public void onTokenInfoChanged(SessionManager sessionManager) {
        Log.i("rae", "凭证发生改变！");

    }
}
