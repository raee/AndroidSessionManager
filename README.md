
[![](https://jitpack.io/v/raee/AndroidSessionManager.svg)](https://jitpack.io/#raee/AndroidSessionManager)

相信大家都知道，每一个App都需要登录，登录信息都保存在本地文件中，然后我们就写一堆的操作SharedPreferences的代码了。

现在，你可以完全抛弃这种方法，一句代码搞定登录信息，用户信息管理。实现全局操作。为你的程序解耦。

```java
 public void sessionDemo() {

        // 获取登录信息
        TokenInfo token = SessionManager.getDefault().getUserToken();
        // 获取用户信息
        UserInfo userInfo = SessionManager.getDefault().getUser();
        // 是否登录
        boolean isLogin = SessionManager.getDefault().isLogin();
        // 退出登录
        SessionManager.getDefault().clear();

        // 登录成功后，设置用户信息
        SessionManager.getDefault().setUser(new UserInfo());
        // 登录成功后，设置登录信息
        SessionManager.getDefault().setUserToken(new TokenInfo());

        // 这句请在Application onCreate 的时候调用初始化配置信息
        SessionManager.initWithConfig(
                new SessionManager.ConfigBuilder()
                        .context(this)
                        .tokenClass(TokenInfo.class)
                        .userClass(UserInfo.class)
                        .build());

        Log.i("rae", "登录信息：" + token);
        Log.i("rae", "用户信息：" + userInfo);
        Log.i("rae", "是否登录：" + isLogin);
    }
```

# 使用

1、Add it in your root `build.gradle` at the end of repositories:

```groovy
allprojects {
		repositories {
			 maven { url 'https://jitpack.io' }
		}
}
```
2、Add the dependency

```groovy
compile 'com.github.raee:AndroidSessionManager:1.0.0'
```
# 一、设计

抽象工厂模式，整体UML如下图，整体思路为：

抽象出SessionManager类，实际还是PreferencesSessionManager实现了这个抽象类，进行本地SharePreferences保存。

![](http://upload-images.jianshu.io/upload_images/2706530-bec6545a8fe94d0a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**Config：**负责维护实体类的class，用来做JSON序列化保存到本地的。这个类起到泛型解析的作用，getUser()返回的对象就由这个类去维护了。从而达到了解耦的作用。

**ConfigBuild：**构建者模式，提供方便的会话配置。

SessionManager.getDefault()； 默认返回的是PreferencesSessionManager的实例。

# 二、自定义SessionManger

如果不想用SharePreferences来保存用户信息，也可以继承SessionManger类实现抽象方法实现自定义的Session管理。

如果你的自定义也想全局访问，那么可以新增一个AppSessionManger的类，来实现一个单例方法，返回你自定义的实现。

参考SessionManger.getDefault()的实现就可以了。

# 三、 SessionManger 代码实现

```java
/**
 * 会话管理
 * Created by ChenRui on 2017/4/28 0028 17:27.
 */
public abstract class SessionManager {

    public static class Config {
        Class<?> userTokenClass;
        Class<?> userClass;
        Context context;
    }

    public static class ConfigBuilder {
        private final Config mConfig;

        public ConfigBuilder() {
            mConfig = new Config();
        }

        public ConfigBuilder tokenClass(Class<?> cls) {
            mConfig.userTokenClass = cls;
            return this;
        }

        public ConfigBuilder userClass(Class<?> cls) {
            mConfig.userClass = cls;
            return this;
        }

        public ConfigBuilder context(Context applicationContext) {
            mConfig.context = applicationContext;
            return this;
        }

        public Config build() {
            return mConfig;
        }
    }

    private static Config sConfig;
    private static WeakReference<SessionManager> managerWeakReference;

    /**
     * 获取默认的会话管理器，默认的为cookie 管理器。
     * 使用之前请使用{@link #initWithConfig(Config)} 来进行初始化配置。
     */
    public static SessionManager getDefault() {
        if (sConfig == null) {
            Log.w("SessionManager", "session config from default");
            sConfig = new ConfigBuilder().tokenClass(SessionToken.class).userClass(SessionUserInfo.class).build();
        }

        if (managerWeakReference == null || managerWeakReference.get() == null) {
            synchronized (SessionManager.class) {
                if (managerWeakReference == null || managerWeakReference.get() == null) {
                    managerWeakReference = new WeakReference<SessionManager>(new PreferencesSessionManager(sConfig));
                }
            }
        }

        return managerWeakReference.get();
    }

    /**
     * 初始化会话管理器
     */
    public static void initWithConfig(Config config) {
        if (sConfig != null) {
            sConfig = null;
            System.gc();
        }

        sConfig = config;
    }


    SessionManager() {
    }

    /**
     * 是否登录
     */
    public abstract boolean isLogin();


    /**
     * 清除会话信息，即退出登录。
     */
    public abstract void clear();

    /**
     * 获取当前登录的用户信息，在调用该方法之前请先调用{@link #isLogin()}来判断是否登录
     */
    public abstract <T> T getUser();

    /**
     * 设置当前用户信息
     */
    public abstract <T> void setUser(T user);

    /**
     * 设置用户授权信息
     *
     * @param token 授权信息
     */
    public abstract <T> void setUserToken(T token);

    /**
     * 获取用户授权信息
     */
    public abstract <T> T getUserToken();


}
```

非常简单的实现，但是却非常实用！
