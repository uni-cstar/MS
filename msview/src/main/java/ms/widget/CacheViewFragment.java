package ms.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lucio on 16/9/8.
 * 缓存根view的fragment
 */
public abstract class CacheViewFragment extends Fragment {

    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isCacheView()) {
            if (mRootView == null) {
                mRootView = onCreateContentView(inflater, container, savedInstanceState);
                initViews(mRootView);
            }
        } else {
            mRootView = onCreateContentView(inflater, container, savedInstanceState);
            initViews(mRootView);
        }

        return mRootView;
    }

    /**
     * 默认开启缓存View
     * 可以重写此方法,去掉自动缓存view功能
     *
     * @return
     */
    protected boolean isCacheView() {
        return true;
    }

    /**
     * 创建视图布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化视图布局
     *
     * @param view
     */
    protected abstract void initViews(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isCacheView()) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = null;
        }

    }
}
