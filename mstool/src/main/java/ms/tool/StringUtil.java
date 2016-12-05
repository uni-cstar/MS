package ms.tool;

import android.text.TextUtils;

/**
 * Created by SupLuo on 2016/2/25.
 */
public class StringUtil {

    /**
     * 是否为空
     *
     * @param content
     * @return
     */
    public static boolean isNullOrEmpty(String content) {
        return content == null || "".equals(content);
    }

    /**
     * 左填充
     *
     * @param source 源字符串
     * @param fill   填充字符串
     * @param total  字符串总长度
     * @return
     */
    public static String padLeft(String source, String fill, int total) {
        if (source.length() >= total) return source;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < total - source.length(); i++) {
            sb.append(fill);
        }
        sb.append(source);
        return sb.toString();
    }

    /**
     * 首字符大写
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     *
     * @param str
     * @return
     */
    public static String upperFirstLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        //如果第一个字符不是字母或者已经是大写字母，则返回原字符串
        if (!Character.isLetter(c) || Character.isUpperCase(c)) {
            return str;
        }

        return new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * 全拼输入转半拼输入
     *
     * @param s
     * @return
     */
    //     * fullWidthToHalfWidth(null) = null;
//     * fullWidthToHalfWidth("") = "";
//     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
//     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
    public static String fullWidthToHalfWidth(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }


    /**
     * 半拼转全拼
     * <p>
     *
     * @param s
     * @return
     */
//    * halfWidthToFullWidth(null) = null;
//    * halfWidthToFullWidth("") = "";
//    * halfWidthToFullWidth(" ") = new String(new char[] {12288});
//    * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
    public static String halfWidthToFullWidth(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
}
