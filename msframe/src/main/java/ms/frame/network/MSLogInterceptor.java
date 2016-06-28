package ms.frame.network;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SupLuo on 2016/5/17.
 * 日志拦截器
 */

public class MSLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Response originalResponse = chain.proceed(chain.request());
//        return originalResponse.newBuilder().body(
//                new ProgressResponseBody(originalResponse.body(), progressListener))
//                .build();
        try {
            Request request = chain.request();

            long start = System.currentTimeMillis();
            String startLog = String.format("URL:%s \nTimeAt:%s", request.url(), dateToString(new Date(), "mm:ss.SSS"));
            Logger.i(startLog);

            Response response = chain.proceed(request);

            long end = System.currentTimeMillis();
            String endLog = String.format("URL:%s \nTimeAt:%s\nTotalTime(mils):%d", request.url(), dateToString(new Date(), "mm:ss.SSS"), end - start);
            Logger.i(endLog);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Request request = chain.request();
            return chain.proceed(request);
        }
    }

    private static String dateToString(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }
}

