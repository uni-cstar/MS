package ms.frame.manager;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by SupLuo on 2016/5/26.
 */
public class MSLogger {

    /**
     * Logger初始化
     *
     * @param defTag 默认全局tag
     */
    public static void init(boolean debugable,String defTag) {

        LogLevel logLevel = LogLevel.NONE;
        if (debugable) {
            logLevel = LogLevel.FULL;
        }
        Logger.init(defTag).logLevel(logLevel);
    }

    /**
     * 输出json
     * @param data 数据
     */
    public static void json(String data){
        Logger.json(data);
    }

    /**
     * 输出调试信息
     *
     * @param msg 内容
     */
    public static void d(String msg) {
        Logger.d(msg);
    }

    /**
     * 输出调试信息
     *
     * @param tag 标记
     * @param msg 内容
     */
    public static void d(String tag, String msg) {
        Logger.t(tag).d(msg);
    }

    /**
     * 输出info信息
     * @param msg 内容
     */
    public static void i(String msg) {
        Logger.i(msg);
    }

    /**
     * 输出info信息
     * @param tag 标记
     * @param msg 内容
     */
    public static void i(String tag, String msg) {
        Logger.t(tag).i(msg);
    }

    /**
     * 输出错误信息
     * @param msg 内容
     */
    public static void e(String msg) {
        Logger.e(msg);
    }

    /**
     * 输出错误信息
     * @param tag 标记
     * @param msg 内容
     */
    public static void e(String tag, String msg) {
        Logger.t(tag).e(msg);
    }

    /**
     * 输出错误信息
     * @param error 异常
     * @param msg 额带内容
     */
    public static void e(Throwable error, String msg) {
        Logger.e(error, msg);
    }

    /**
     * 输出错误信息
     * @param tag tag标记
     * @param error 异常
     * @param msg 额带内容
     */
    public static void e(String tag, Throwable error, String msg) {
        Logger.t(tag).e(error, msg);
    }
}
