package ms.frame.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by SupLuo on 2016/6/28.
 * the helper for progress
 */
public class MSProgressHelper {
    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param client           待包装的OkHttpClient
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient，使用clone方法返回
     */
    public static void addProgressResponseListener(final OkHttpClient client, final MSProgressResponseListener progressListener) {

//        //由于okhttp3 去掉了clone的实现，因此如下代码无法实现
//        //克隆
//        OkHttpClient clone = (OkHttpClient)client.clone();
//        //增加拦截器
//        clone.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                //拦截
//                Response originalResponse = chain.proceed(chain.request());
//                //包装响应体并返回
//                return originalResponse.newBuilder()
//                        .body(new MSProgressResponseBody(originalResponse.body(), progressListener))
//                        .build();
//            }
//        });
//        return clone;

        //增加拦截器
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体
                Response response = originalResponse.newBuilder()
                        .body(new MSProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
                client.networkInterceptors().remove(this);
                //返回响应体
                return response;
            }
        });
    }

    /**
     * 包装请求体用于上传文件的回调
     *
     * @param requestBody             请求体RequestBody
     * @param progressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static MSProgressRequestBody addProgressRequestListener(RequestBody requestBody, MSProgressRequestListener progressRequestListener) {
        //包装请求体
        return new MSProgressRequestBody(requestBody, progressRequestListener);
    }
}
