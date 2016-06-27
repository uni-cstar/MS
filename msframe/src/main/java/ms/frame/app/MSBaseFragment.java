package ms.frame.app;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ms.frame.R;
import ms.frame.util.MSPBCache;

/**
 * Created by admin on 2016/6/2.
 */
public class MSBaseFragment extends Fragment implements MSTheme {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setFitsSystemWindows(view);
        return view;
    }

    @Override
    public boolean isFitsSystemWindows() {
        return true;
    }

    @Override
    public void setFitsSystemWindows(View view) {
        if (!isFitsSystemWindows()) return;

        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && !view.getFitsSystemWindows()) {
            view.setFitsSystemWindows(true);
        }
    }

    @Override
    public void setLightTheme() {
        if (this.getActivity() == null)
            return;
        this.getActivity().setTheme(R.style.MSTheme_Light);
    }

    @Override
    public void setDarkTheme() {
        if (this.getActivity() == null)
            return;
        this.getActivity().setTheme(R.style.MSTheme_Dark);
    }

    @Override
    public boolean isLightTheme() {
        return MSPBCache.isNight(this.getActivity());
    }

    @Override
    public void onThemeSwitch() {

    }
}
