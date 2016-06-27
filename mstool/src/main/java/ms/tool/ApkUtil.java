package ms.tool;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SupLuo on 2016/2/24.
 */
public class ApkUtil {

    /**
     * 安装APK
     *
     * @param context
     * @param uri
     */
    public static void install(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载一个app
     *
     * @param context
     * @param packageName 包名
     */
    public static void uninstall(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        // 获取PackageManager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断用户当前使用的界面是否是指定界面
     *
     * @param context
     * @param className activity所在的classname 例如“your package.LoginActivity”
     *                  可以使用Class.getName获取
     * @return
     */
    public static boolean isActivityRunningTop(Context context, String className) {
        ActivityManager manager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = manager
                .getRunningTasks(1);
        if (taskInfos == null)
            return false;

        String cmpNameTemp = (taskInfos.get(0).topActivity)
                .getClassName();
        return !TextUtils.isEmpty(cmpNameTemp)
                && cmpNameTemp.equalsIgnoreCase(className);
    }

    /**
     * 判断指定className的service是否正在后台运行
     *
     * @param mContext
     * @param className 服务的class name，可以使用Class.getName获取
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        ActivityManager activityManager = (ActivityManager) mContext
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (serviceList == null || !serviceList.isEmpty())
            return false;

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equalsIgnoreCase(
                    className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户当前使用的程序是否是指定程序
     *
     * @param context
     * @return
     */
    public static boolean isAppRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = am
                .getRunningTasks(1);

        if (runningTaskInfos == null)
            return false;

        ComponentName cn = runningTaskInfos.get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(context.getPackageName());

    }


}
