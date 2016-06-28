package ms.frame.network;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by SupLuo on 2016/5/25.
 */
public class MSApi {

    /**
     * Created by SupLuo on 2016/5/25.
     * Api线程调度器 ： Http请求所使用的线程调度器（observable执行在io线程，subscriber运行在主UI线程）
     */
    public static class ApiSchedulers<T> implements Observable.Transformer<T, T> {

        @Override
        public Observable<T> call(Observable<T> tObservable) {

            return tObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * 使用Api调度器
     *
     * @param <T>
     * @return 线程调度器
     */
    public static <T> Observable.Transformer<T, T> applyApiSchedulers(Class<T> tClass) {
        return new ApiSchedulers<T>();
    }

    /**
     * 使用Api调度器
     *
     * @param <T>
     * @return 线程调度器
     */
    public static <T> Observable<T> applyApiSchedulers(Observable<T> observable) {
        return observable.compose(new ApiSchedulers<T>());
    }

    /**
     * Api结果检查函数，用于校验api的返回结果
     *
     * @param <T>
     */
    public static class ApiCheckResultFunc<T> implements Func1<MSApiResult<T>, Observable<T>> {

        TokenInvalidateListener mListener;

        public ApiCheckResultFunc() {

        }

        public ApiCheckResultFunc(TokenInvalidateListener listener) {
            this.mListener = listener;
        }
//
//        public Object getObj(){
//            return new Object();
//        }
//
//        public void t(){
//            Observable.create(new Observable.OnSubscribe<Object>() {
//                @Override
//                public void call(Subscriber<? super Object> subscriber) {
//                    Object object = getObj();
//                    subscriber.onNext(object);
//                    subscriber.onCompleted();
//                }
//            })
//            Observable.from
//        }

        @Override
        public Observable<T> call(MSApiResult<T> apiResult) {
            if (apiResult.getRet() == 0) {//成功返回结果
                return Observable.just(apiResult.getData());
            } else if (apiResult.getRet() == 3) {//Token失效
                if (this.mListener != null) {
                    this.mListener.onTokenInvalidate();
                }
                return Observable.empty();
            } else {
                return Observable.error(new ApiException(apiResult));
            }
        }
    }

    /**
     * Token不可用回调
     */
    public interface TokenInvalidateListener {
        void onTokenInvalidate();
    }

    /**
     * Created by SupLuo on 2016/5/25.
     * Api请求失败抛出的异常
     */
    public static class ApiException extends RuntimeException {

        public MSApiResult Result;

        public ApiException(MSApiResult result) {
            super(result.getMsg());
            this.Result = result;
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }

        public ApiException(Throwable throwable) {
            super(throwable);
        }
    }

}
