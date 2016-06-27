package ms.frame.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SupLuo on 2016/3/9.
 * 使用方法：创建一个子类，并且最好单例
 */
public abstract class MSApiClient {

    private Retrofit mRetrofit;

    /**
     * 获取基地址
     *
     * @return 基地址
     */
    protected abstract String getBaseUrl();

    /**
     * 超时时间
     *
     * @return 超时时间
     */
    protected abstract long getTimeOut();

    /**
     * 超时时间单位
     *
     * @return 超时时间单位
     */
    protected abstract TimeUnit getTimeOutUnit();

//    protected abstract Context getAppContext();

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
//            Context context = App.sharedInstance();
//
//            //设置缓存目录
//            File cacheDirectory = new File(context.getCacheDir()
//                    .getAbsolutePath(), "kt_http_cache");
//            Cache cache = new Cache(cacheDirectory, 20 * 1024 * 1024);
            OkHttpClient mOkHttpClient = createOkHttpClient();

            //构建Retrofit
            mRetrofit = createRetrofit(mOkHttpClient, getBaseUrl());
        }
        return mRetrofit;
    }

    /**
     * 设置 retrofit
     *
     * @param retrofit retrofit实例
     */
    public void setRetrofit(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }

    public Retrofit createRetrofit(OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                //配置服务器路径
                .baseUrl(baseUrl)
                        //设置日期解析格式，这样可以直接解析Date类型
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        //配置转化库，默认是Gson
                .addConverterFactory(createJsonConverter())
//                            配置回调库，采用RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        //设置OKHttpClient为网络客户端
                .client(client)
                .build();
    }

    /**
     * 解析器（可重写）
     *
     * @return 序列化解析器converter
     */
    protected Converter.Factory createJsonConverter() {
        return GsonConverterFactory.create();
    }

    /**
     * okhttp(可重写)
     *
     * @return okhttp实例
     */
    protected OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = createOkhttpClientBuilder();
        return builder.build();
    }

    protected OkHttpClient.Builder createOkhttpClientBuilder() {
        //自定义拦截器，查看请求url，请求时间等
        Interceptor logInterceptor = new MSLogInterceptor();

        //logging 拦截器，okhttp.logging提供，主要是用于输出网络请求和结果的Log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//配置输出级别

//        //网络请求拦截器，可以作为在所有网络请求之前增加token参数的作用
        return new OkHttpClient.Builder()
                .connectTimeout(getTimeOut(), getTimeOutUnit()) //配置请求超时时间
                .addInterceptor(logInterceptor)        //配置拦截器
                .addInterceptor(httpLoggingInterceptor)//配置拦截器
//                .addNetworkInterceptor(tokenInterceptor)//配置网络请求拦截器
                .retryOnConnectionFailure(true)       //设置出现错误进行重新连接
                ;
    }


//    static class MSTokenInterceptor implements Interceptor {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request originalRequest = chain.request();
//            if (Your.sToken == null || alreadyHasAuthorizationHeader(originalRequest)) {
//                return chain.proceed(originalRequest);
//            }
//            Request authorised = originalRequest.newBuilder()
//                    .header("Authorization", Your.sToken)
//                    .build();
//            return chain.proceed(authorised);
//        }
//    }


}