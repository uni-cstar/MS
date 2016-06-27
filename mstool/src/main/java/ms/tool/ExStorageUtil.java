package ms.tool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 存储设备相关帮助函数
 * Created by SupLuo on 2016/2/25.
 */
public class ExStorageUtil {

    /**
     * SD卡路径
     * @return
     */
    public static String getSDCardPath() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return "";
    }

    /**
     * 外置存储是否可用
     *
     * @return true ：可用.
     */
    public static boolean isAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 通知系统扫描指定路径的文件。
     * 通常用于对文件增删之后，比如：用于拍照之后，让新拍照的图片立即显示出来等
     *
     * @param context
     * @param path
     */
    public static void scan(Context context, String path) {
        Uri uri = Uri.fromFile(new File(path));
        scan(context, uri);
    }

    /**
     * 通知系统扫描指定Uri。
     * 通常用于对文件增删之后，比如：用于拍照之后，让新拍照的图片立即显示出来等
     *
     * @param context
     * @param uri
     */
    public static void scan(Context context, Uri uri) {
        Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        it.setData(uri);
        context.sendBroadcast(it);
    }

    /**
     * 扫描整个SD卡文件
     * 通常用于对文件增删之后，比如：用于拍照之后，让新拍照的图片立即显示出来等
     *
     * @param context
     */
    public static void scanExternalStorage(Context context) {
        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
        Intent it = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        it.setData(uri);
        context.sendBroadcast(it);
    }

}
