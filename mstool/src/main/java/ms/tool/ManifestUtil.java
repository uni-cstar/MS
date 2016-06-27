package ms.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 程序清单文件帮助类
 * Created by SupLuo on 2016/2/24.
 */
public class ManifestUtil {

    /**
     * 获取清单文件中指定的meta-data
     *
     * @param context
     * @param key     MetaData对应的Key
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getMetaData(Context context, String key) {

        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null)
                return null;

            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null)
                return null;

            if (applicationInfo.metaData != null) {
                resultData = applicationInfo.metaData.getString(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获得渠道号
     *
     * @param context
     * @param channelKey 指定渠道Key
     * @return
     */
    public static String getChannelNo(Context context, String channelKey) {
        return getMetaData(context, channelKey);
    }

    /**
     * 获取程序版本名称
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
