package ms.tool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by SupLuo on 2016/2/25.
 */
public class Encoder {

    /**
     * 标准MD5加密
     *
     * @param content
     * @return
     * @throws NoSuchAlgorithmException 加密异常
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    public static String toMD5String(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = content.getBytes("UTF-8");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        bytes = md5.digest(bytes);

        StringBuilder result = new StringBuilder();
        for (byte item : bytes) {
            String hexStr = Integer.toHexString(0xFF & item);

            if (hexStr.length() < 2) {
                result.append("0");
            }
            result.append(hexStr);
        }

        return result.toString();
    }
}
