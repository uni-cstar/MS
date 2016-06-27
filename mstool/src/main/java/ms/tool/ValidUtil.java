package ms.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SupLuo on 2016/3/1.
 * 合法性校验
 */
public class ValidUtil {

    /**
     * 是否是网络地址
     *
     * @return
     */
    public static boolean isUrl(String url) {
        //TODO 此正则估计不是很完善，有待解决
        String check = "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$";
        Pattern p = Pattern.compile(check, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        return matcher.matches();
    }

    /**
     * 是否是电话号码
     *
     * @param tel
     * @return
     */
    public static boolean isMobileNo(String tel) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(tel);
        b = m.matches();
        return b;
    }
}
