package ms.frame.app;

import android.view.View;

/**
 * Created by SupLuo on 2016/6/2.
 */
interface MSTheme {

    /**
     * 是否设置android:fitsSystemWindows="true"
     * @return
     */
    boolean isFitsSystemWindows();

    /**
     * 修正上层状态栏
     * @param rootGroup
     */
    void setFitsSystemWindows(View rootGroup);

    /**
     * 设置白天主题
     */
    void setLightTheme();

    /**
     * 设置夜晚主题
     */
    void setDarkTheme();

    /**
     * 是否是夜间模式
     * @return
     */
    boolean isLightTheme();

    /**
     * 主题切换之后的处理
     * 比如重启activity，或者重新添加fragment
     */
    void onThemeSwitch();

}
