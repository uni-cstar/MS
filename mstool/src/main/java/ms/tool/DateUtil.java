package ms.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SuoLuo on 2016/3/1.
 */
public class DateUtil {

    /**
     * 获取UTC时间
     *
     * @return
     */
    public static Date getUtcDate() {
        // 1、取得本地时间：
        final Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        final int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * UTC时间转换成本地时间
     *
     * @param utcDt
     * @return
     */
    public static Date utcToLocalDate(Date utcDt) {
        // 1、取得本地时间：
        final Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        final int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.setTime(utcDt);
        cal.add(Calendar.MILLISECOND, (zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * 转换成 格式 "yyyy-MM-dd HH:mm:ss"字符串
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取字符串
     * @param date
     * @param format 转换格式
     * @return
     */
    public static String toString(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
     * 时间转换成带时区的字符串
     *
     * @param date
     * @return
     */
    public static String toTZoneString(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formater = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formater.format(date);
    }

    /**
     * 自定义最小时间
     */
    public static final Date MIN_DATE = new GregorianCalendar(1, 1, 1).getTime();
    public static final long ONE_DAY = 86400000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long TEN_MINUTE = 600000L;
    private static final String TODAY = "今天";
    private static final String YESTERDAY = "昨天";
    private static final String BEFORE_YESTERDAY = "前天";
    private static final String JUST_NOW = "刚刚";// 十秒内

    /**
     * 转换成友好阅读的时间格式
     *
     * @param date
     * @return 例如：今天 3:50 3小时前
     */
    public static String toFriendlyString(Date date) {
        if (date == null || date.before(MIN_DATE))
            return "-";
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar userDt = Calendar.getInstance();
        userDt.setTime(date);

        long delta = now.getTimeInMillis() - userDt.getTimeInMillis();

        if (delta < TEN_MINUTE) { // 十分钟内
            return JUST_NOW;
        } else {
            long days = delta / ONE_DAY;
            if (now.get(Calendar.YEAR) == userDt.get(Calendar.YEAR)){
                days = now.get(Calendar.DAY_OF_YEAR) - userDt.get(Calendar.DAY_OF_YEAR);
            }
            else{
                days = days + 1;
            }

            // 注：格式化字符串存在区分大小写
            // 对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
            // yyyy代表年份，如“2010”；dd代表天，如“25”
            // date.getDay()
            if (days == 0) {
                return String.format("%s %s", TODAY, new SimpleDateFormat(
                        "HH:mm").format(date));
            } else if (days == 1) {
                return String.format("%s %s", YESTERDAY, new SimpleDateFormat(
                        "HH:mm").format(date));
            } else if (days == 2) {
                return String.format("%s %s", BEFORE_YESTERDAY,
                        new SimpleDateFormat("HH:mm").format(date));
            } else {
                if (now.get(Calendar.YEAR) != userDt.get(Calendar.YEAR)) {
                    return toString(date);
                } else {
                    return toString(date, "MM-dd HH:mm:ss");
                }
            }
        }
    }

}
