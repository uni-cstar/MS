package ms.tool;

import android.content.Context;

/**
 * Created by SupLuo on 2016/3/3.
 * SharedPreference 简易帮助类，使用场景：单例的SharedPreference存储
 */
public class SharedPreferenceHelper {

    protected Context mContext;
    protected String mFileName;

    public SharedPreferenceHelper(Context context, String fileName) {
        mContext = context;
        mFileName = fileName;
    }

    public boolean getBoolean(String key, boolean defVal) {
        return SharedPreferencesUtil.getBoolean(mContext, mFileName, key, defVal);
    }

    public void setBoolean(String key, boolean val) {
        SharedPreferencesUtil.setBoolean(mContext, mFileName, key, val);
    }

    public int getInt(String key, int defVal) {
        return SharedPreferencesUtil.getInt(mContext, mFileName, key, defVal);
    }

    public void setInt(String key, int val) {
        SharedPreferencesUtil.setInt(mContext, mFileName, key, val);
    }

    public long getLong(String key, long defVal) {
        return SharedPreferencesUtil.getLong(mContext, mFileName, key, defVal);
    }

    public void setLong(String key, long val) {
        SharedPreferencesUtil.setLong(mContext, mFileName, key, val);
    }

    public String getString(String key, String defVal) {
        return SharedPreferencesUtil.getString(mContext, mFileName, key, defVal);
    }

    public void setString(String key, String val) {
        SharedPreferencesUtil.setString(mContext, mFileName, key, val);
    }

    public float getFloat(String key, float defVal) {
        return SharedPreferencesUtil.getFloat(mContext, mFileName, key, defVal);
    }

    public void setFloat(String key, float val) {
        SharedPreferencesUtil.setFloat(mContext, mFileName, key, val);
    }
}
