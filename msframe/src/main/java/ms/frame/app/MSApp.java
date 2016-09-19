package ms.frame.app;

import android.app.Application;

import ms.frame.R;

/**
 * Created by SupLuo on 2016/6/2.
 */
public abstract class MSApp extends Application {

    static MSApp instance;

    /**
     * 默认白天主题
     */
    public static final int DEFAULT_THEME_DAY = R.style.MSTheme_Light;

    /**
     * 默认夜间主题
     */
    public static final int DEFAULT_THEME_NIGHT = R.style.MSTheme_Dark;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    /**
     * 获取application实例
     *
     * @return
     */
    public static MSApp instance() {
        return instance;
    }

    /**
     * 获取白天主题
     *
     * @return
     * @see #DEFAULT_THEME_DAY 默认白天主题
     */
    public int getDayTheme() {
        return DEFAULT_THEME_DAY;
    }

    /**
     * 获取夜间主题
     *
     * @return
     * @see #DEFAULT_THEME_NIGHT 默认夜晚主题
     */
    public int getNightTheme() {
        return DEFAULT_THEME_NIGHT;
    }

}
