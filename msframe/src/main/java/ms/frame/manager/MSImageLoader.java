package ms.frame.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by supluo on 2016/4/15.
 */
public class MSImageLoader {

    private static final String TAG = "MS_IMAGE_LOADER";
   

    /**
     * Clears disk cache.
     *
     * <p>
     *     This method should always be called on a background thread, since it is a blocking call.
     * </p>
     */
    public static void clearDiskCache(Context context){
        Glide.get(context).clearDiskCache();
    }

    /**
     * Clears as much memory as possible.
     * <p>
     *  call this method must on Main Thead
     * </p>
     * @see android.content.ComponentCallbacks#onLowMemory()
     * @see android.content.ComponentCallbacks2#onLowMemory()
     *
     */
    public static void clearMemoryCache(Context context){
        Glide.get(context).clearMemory();
    }

    /**
     * Cancel any pending loads Glide may have for the view and free any resources that may have been loaded for the
     * view.
     *
     * <p>
     *     Note that this will only work if {@link View#setTag(Object)} is not called on this view outside of Glide.
     * </p>

     * @param view The view to cancel loads and free resources for.
     * @throws IllegalArgumentException if an object other than Glide's metadata is set as the view's tag.
     */
    public static void  cancel(View view){
        Glide.clear(view);
    }

    /**
     * 仅下载，后续可以用url直接获取到图片
     * @param context
     * @param url 图片地址
     * @param width 宽度
     * @param height 高度
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static File loadFile(Context context, String url, int width,
                                int height) throws ExecutionException, InterruptedException {
        FutureTarget<File> future = Glide.with(context).load(url)
                .downloadOnly(width, height);
        return future.get();
    }

    /**
     * 加载gif
     *
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     */
    public static void loadGif(String url, ImageView imageView,
                               int placeHolder, int errorPlaceHolder) {
        Glide.with(imageView.getContext()).load(url).asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder)
                .into(imageView);
    }

    /**
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void loadGif(String url, ImageView imageView,
                               int placeHolder, int errorPlaceHolder, int anim) {
        Glide.with(imageView.getContext()).load(url).asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     */
    public static void loadLocal(String url, ImageView imageView,
                                 int placeHolder, int errorPlaceHolder) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        // 加载本地的，只需要缓存result
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder)
                .into(imageView);
    }

    /**
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void loadLocal(String url, ImageView imageView,
                                 int placeHolder, int errorPlaceHolder, int anim) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        // 加载本地的，只需要缓存result
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     */
    public static void load(String url, ImageView imageView, int placeHolder,
                            int errorPlaceHolder) {
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder)
                .dontAnimate()
                .into(imageView);
    }

    public static void load(android.app.Fragment fragment, String url,
                            ImageView imageView, int placeHolder, int errorPlaceHolder, int anim) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    public static void load(android.support.v4.app.Fragment fragment,
                            String url, ImageView imageView, int placeHolder,
                            int errorPlaceHolder, int anim) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 从文件中加载图片
     *
     * @param file             文件
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     */
    public static void load(File file, ImageView imageView, int placeHolder,
                            int errorPlaceHolder) {
        Glide.with(imageView.getContext()).load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder)
                .into(imageView);
    }

    /**
     * @param uri
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     */
    public static void load(Uri uri, ImageView imageView, int placeHolder,
                            int errorPlaceHolder) {
        Glide.with(imageView.getContext()).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder)
                .into(imageView);
    }

    /**
     * @param url
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void load(String url, ImageView imageView, int placeHolder,
                            int errorPlaceHolder, int anim) {
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 从文件中加载图片
     *
     * @param file             文件
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void load(File file, ImageView imageView, int placeHolder,
                            int errorPlaceHolder, int anim) {
        Glide.with(imageView.getContext()).load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 从文件中加载图片
     *
     * @param uri
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void load(Uri uri, ImageView imageView, int placeHolder,
                            int errorPlaceHolder, int anim) {
        Glide.with(imageView.getContext()).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // 更改缓存策略，避免不同大小的ImageView显示同张图片时下载多次。
                .placeholder(placeHolder).error(errorPlaceHolder).animate(anim)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param thumbnailUrl     小图地址
     * @param url              大图地址
     * @param imageView
     * @param placeHolder      占位符
     * @param errorPlaceHolder 加载失败占位符
     * @param anim             加载动画
     */
    public static void load(String thumbnailUrl, String url,
                            ImageView imageView, int placeHolder, int errorPlaceHolder, int anim) {
        DrawableRequestBuilder<String> thumbBuilder = Glide
                .with(imageView.getContext()).load(thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder).error(errorPlaceHolder);
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(thumbBuilder).placeholder(placeHolder)
                .error(errorPlaceHolder).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param thumbnailUrl 小图地址
     * @param url          大图地址
     * @param imageView
     * @param listener     加载监听
     */
    public static void load(String thumbnailUrl, final String url,
                            final ImageView imageView, int placeHolder, int errorPlaceHolder,
                            int anim,  final MSImageLoaderListener listener) {
        // create thumbnail request builder
        DrawableRequestBuilder<String> thumbBuilder = Glide
                .with(imageView.getContext()).load(thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder).error(errorPlaceHolder).fitCenter();

        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(thumbBuilder).error(errorPlaceHolder)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        Log.d(TAG, "Glide failed loading image " + url);
                        if (listener != null) {
                            listener.onMSImageLoadFinish();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);

                        if (listener != null) {
                            listener.onMSImageLoadFinish();
                        }
                        return false;
                    }

                }).into(imageView);
    }
}
