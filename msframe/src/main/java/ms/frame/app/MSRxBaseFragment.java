package ms.frame.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ms.frame.R;
import ms.frame.util.MSPBCache;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2016/9/19.
 * 提供rx 请求取消方法
 */
public class MSRxBaseFragment extends MSBaseFragment {


    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s) {
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

}
