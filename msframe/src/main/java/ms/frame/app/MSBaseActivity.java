package ms.frame.app;


import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import ms.frame.R;
import ms.frame.util.MSPBCache;

/**
 * 基础界面
 */
public class MSBaseActivity extends AppCompatActivity implements MSTheme {

    /**
     * 当前界面是否获取焦点
     */
    protected boolean isFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主题切换
        setTheme(MSPBCache.getThemeRes(this));
        //TODO add activity
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        fitSystemWindows();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        fitSystemWindows();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        fitSystemWindows();
    }

    private void fitSystemWindows() {
        if (!isFitsSystemWindows()) return;
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        setFitsSystemWindows(contentFrameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFocus = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFocus = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO remove activity
    }

    public boolean isFocus() {
        return isFocus;
    }

    @Override
    public boolean isFitsSystemWindows() {
        return true;
    }


    /**
     * 解决透明状态栏效果
     * 设置支持android:fitsSystemWindows="true"
     *
     * @param rootGroup
     */
    @Override
    public void setFitsSystemWindows(View rootGroup) {
        if (rootGroup == null)
            return;

        View parentView = ((ViewGroup) rootGroup).getChildAt(0);
        if (parentView == null)
            return;

        //4.1以上就设置此属性（本身要求是4.4及以上）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            parentView.setFitsSystemWindows(true);
        }

        //如果是4.4 至6.0之间，需要把主界面背景设置为跟标题栏背景颜色一致
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Resources.Theme theme = this.getTheme();
            if (theme != null) {
                TypedArray a = theme.obtainStyledAttributes(new int[R.styleable.Theme_colorPrimary]);
                int colorPrimary = a.getColor(R.styleable.Theme_colorPrimary, 0);
                a.recycle();
                //将整个窗口背景设置为主题色，解决4.4透明状态栏颜色问题
                if (colorPrimary != 0) {
                    //借助工具，解决4.4透明状态栏颜色问题
                    SystemBarTintManager tintManager = new SystemBarTintManager(this);
                    tintManager.setStatusBarTintColor(colorPrimary);
                    tintManager.setStatusBarTintEnabled(true);
//                    parentView.setBackgroundColor(colorPrimary);
                }
            }
        }
        //5.0不需要额外设置
    }

    @Override
    public void setLightTheme() {
        MSPBCache.setDay(this);
        setTheme(R.style.MSTheme_Light);
    }

    @Override
    public void setDarkTheme() {
        MSPBCache.setNight(this);
        setTheme(R.style.MSTheme_Dark);
    }

    @Override
    public boolean isLightTheme() {
        return MSPBCache.isNight(this);
    }

    @Override
    public void onThemeSwitch() {

    }

}
