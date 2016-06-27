package ms.tool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by SupLuo on 2016/3/1.
 */
public class FileUtil {

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * 如果目录文件可能比较多，也可以尝试放在线程中去执行
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            //递归删除目录中的子目录下
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDirectory(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取哈希文件名
     *
     * @param fileName
     * @return
     */
    public static String getHashedFileName(String fileName) {
        if (fileName == null || fileName.endsWith("/")) {
            return null;
        }

        String suffix = getSuffix(fileName);
        StringBuilder sb = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] dstbytes = digest.digest(fileName.getBytes("UTF-8")); // GMaFroid uses UTF-16LE
            sb = new StringBuilder();
            for (byte b : dstbytes) {
                sb.append(Integer.toHexString(b & 0xff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != sb && null != suffix) {
            return sb.toString() + "." + suffix;
        }
        return null;
    }

    /**
     * 获取文件名后缀
     *
     * @param fileName
     * @return
     */
    private static String getSuffix(String fileName) {
        int dot_point = fileName.lastIndexOf(".");
        int sl_point = fileName.lastIndexOf("/");
        if (dot_point < sl_point) {
            return "";
        }

        if (dot_point != -1) {
            return fileName.substring(dot_point + 1);
        }

        return null;
    }

    /**
     * 获得指定文件的byte数组
     * @param filePath
     * @return
     */
    public static byte[] getBytes(String filePath) {
        File file = new File(filePath);
        return getBytes(file);
    }

    /**
     * 根据文件获取byte数组
     * @param file
     * @return
     */
    public static byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     * @param bfile
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
