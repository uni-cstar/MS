package ms.frame.app;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2016/9/19.
 * 提供rx 请求取消方法
 */
public class MSRxBaseActivity extends MSBaseActivity {

    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s) {
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
}
