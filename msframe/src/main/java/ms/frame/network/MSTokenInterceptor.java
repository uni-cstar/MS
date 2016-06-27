package ms.frame.network;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SupLuo on 2016/5/17.
 */
public abstract class MSTokenInterceptor implements Interceptor {

    public static final String DEFAULT_TOKEN_KEY = "Authorization";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        willRequestUrl(chain.request().url().toString());
        String token = getTokenValue();
        String name = getTokenKey();
        //如果token值为空，则不添加到header
        if (TextUtils.isEmpty(token))
            return chain.proceed(originalRequest);

        Headers headers = originalRequest.headers();

        //如果请求中已经包含了token，则直接处理请求
        if (!TextUtils.isEmpty(headers.get(name)))
            return chain.proceed(originalRequest);
        //添加token
        Request authorised = originalRequest.newBuilder()
                .addHeader(name, token)
                .build();
        return chain.proceed(authorised);
    }

    //token key
    public abstract String getTokenKey();

    //token 值
    public abstract String getTokenValue();

    public void willRequestUrl(String url){

    }
}
