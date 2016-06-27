package ms.frame.util;

import android.content.Context;

import ms.frame.app.MSApp;
import ms.tool.SharedPreferencesUtil;

/**
 * Created by SupLuo on 2016/6/2.
 */
public class MSPBCache {

    /**
     * App公用的 SharedPreferences 文件名
     */
    private static final String FILE_NAME = "ms_pb_cache";

    /**
     * 主题
     */
    private static final String KEY_THEME_NIGHT = "theme_night";

    /**
     * 设置夜间主题
     */
    public static void setNight(Context context) {
        SharedPreferencesUtil.setBoolean(context.getApplicationContext(), FILE_NAME, KEY_THEME_NIGHT, true);
    }

    /**
     * 设置白天主题
     */
    public static void setDay(Context context) {
        SharedPreferencesUtil.setBoolean(context.getApplicationContext(), FILE_NAME, KEY_THEME_NIGHT, false);
    }

    /**
     * 是否是夜间模式
     *
     * @return 主题资源ID
     */
    public static boolean isNight(Context context) {
        return SharedPreferencesUtil.getBoolean(context.getApplicationContext(), FILE_NAME, KEY_THEME_NIGHT, false);
    }

    /**
     * 获取主题资源
     * @return 主题资源ID
     */
    public static int getThemeRes(Context context) {
        if (!isNight(context.getApplicationContext())) {
            return MSApp.instance().getDayTheme();
        } else {
            return MSApp.instance().getNightTheme();
        }
    }
}
