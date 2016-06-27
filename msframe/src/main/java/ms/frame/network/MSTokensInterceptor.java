package ms.frame.network;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SupLuo on 2016/5/17.
 * token interceptor：put tokens into headers of request
 */
public abstract class MSTokensInterceptor implements Interceptor {

    public static final String DEFAULT_TOKEN_KEY = "Authorization";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        willRequestUrl(chain.request().url().toString());
        Map<String, String> tokenMap = getTokenMap();
        //no header values
        if (tokenMap == null || tokenMap.size() == 0)
            return chain.proceed(originalRequest);

        Headers headers = originalRequest.headers();


        Set<String> keys = tokenMap.keySet();
        for (String key : keys) {
            if (TextUtils.isEmpty(headers.get(key))) {//if head not exits the token of key，put it into header
                String val = headers.get(key);
                if(!TextUtils.isEmpty(val)){
                    originalRequest.newBuilder().addHeader(key, headers.get(key));
                }
            }
        }
        //get the request
        Request authorised = originalRequest.newBuilder().build();
        return chain.proceed(authorised);
    }

    //the key of token
    public abstract Map<String, String> getTokenMap();

    /**
     * execute before request the url
     * @param url
     */
    public void willRequestUrl(String url) {

    }
}
