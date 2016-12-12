package ms.tool;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by SupLuo on 2016/2/25.
 * SharedPreference简易帮助类
 */
public class SharedPreferencesUtil {

    public static boolean getBoolean(Context context, String fileName, String key, boolean defVal) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defVal);
    }

    public static void setBoolean(Context context, String fileName, String key, boolean val) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static int getInt(Context context, String fileName, String key, int defVal) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, defVal);
    }

    public static void setInt(Context context, String fileName, String key, int val) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static long getLong(Context context, String fileName, String key, long defVal) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getLong(key, defVal);
    }

    public static void setLong(Context context, String fileName, String key, long val) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, val);
        editor.apply();
    }

    public static String getString(Context context, String fileName, String key, String defVal) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key, defVal);
    }

    public static void setString(Context context, String fileName, String key, String val) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static float getFloat(Context context, String fileName, String key, float defVal) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defVal);
    }

    public static void setFloat(Context context, String fileName, String key, float val) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    /**
     * 删除SharedPreferences文件
     *
     * @param context
     * @param fileName 文件名
     */
    public static void deleteFile(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getApplicationContext().getFilesDir().getParent())
                .append("/shared_prefs/")
                .append(fileName)
                .append(".xml");
        File file = new File(sb.toString());
        file.delete();
    }


}
