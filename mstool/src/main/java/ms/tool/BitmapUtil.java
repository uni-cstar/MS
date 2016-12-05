package ms.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SupLuo on 2016/2/25.
 */
public class BitmapUtil {

    /**
     * 缩放图片
     *
     * @param src       用于缩放的bitmap对象
     * @param desWidth  所需要的宽
     * @param desHeight 所需要的高
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap src, int desWidth, int desHeight) {
        if (src == null)
            return null;
        int w = src.getWidth();
        int h = src.getHeight();

        float scaleWidth = ((float) desWidth) / w;
        float scaleHeight = ((float) desHeight) / h;

        Matrix mMatrix = new Matrix();
        mMatrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, mMatrix,
                true);
        return resizedBitmap;
    }

    /**
     * 缩放图片
     *
     * @param src         用于缩放的bitmap对象
     * @param scaleWidth  宽的缩放比例
     * @param scaleHeight 高的缩放比列
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap src, float scaleWidth,
                                    float scaleHeight) {
        if (src == null)
            return null;

        Matrix mMatrix = new Matrix();
        mMatrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0,
                (int) (src.getWidth() * scaleWidth),
                (int) (src.getHeight() * scaleHeight), mMatrix, true);
        return resizedBitmap;
    }

    /**
     * 裁剪图片
     *
     * @param src    图片源
     * @param rect   用于裁剪的rect
     * @param config 裁剪之后的图片设置
     * @return
     */
    public static Bitmap cropBitmap(Bitmap src, Rect rect, Bitmap.Config config) {
        int width = rect.width();
        int height = rect.height();

        Bitmap cropBitmap = Bitmap.createBitmap(width, height, config);

        Canvas cvs = new Canvas(cropBitmap);
        Rect newRect = new Rect(0, 0, width, height);
        cvs.drawBitmap(src, rect, newRect, null);
        return cropBitmap;
    }

    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degree      旋转角度
     * @param needRecycle  是否需要在转换之后回收bitmap
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree, boolean needRecycle) {
        Matrix m = new Matrix();
        m.postRotate(degree);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        return bm;
    }

    /**
     * 混合图片（大图在前，小图在后）
     * (居中混合，小图放在大图中间)
     * 使用场景：二维码中间贴上logo
     *
     * @param srcBmp  大图
     * @param markBmp 小图
     * @param config     建议{@link Bitmap.Config#RGB_565},占用的内存更少
     * @return 混合之后的图片
     */
    public static Bitmap mixtureBitmap(Bitmap srcBmp, Bitmap markBmp,
                                       Bitmap.Config config) {
        if (srcBmp == null)
            return null;
        int sW = srcBmp.getWidth();
        int sH = srcBmp.getHeight();
        int mW = markBmp.getWidth();
        int mH = markBmp.getHeight();
        Bitmap newB = Bitmap.createBitmap(sW, sH, config);
        Canvas cv = new Canvas(newB);
        cv.drawBitmap(srcBmp, 0, 0, null);
        cv.drawBitmap(markBmp, sW / 2 - mW / 2, sH / 2 - mH / 2, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newB;
    }

    /**
     * 转换成字节数组
     * 默认格式为JPEG，占用的空间更小,裁剪质量80，即百分之八十（取值：0-100）
     *
     * @param bmp         bitmap对象
     * @param needRecycle 是否需要在转换之后回收bitmap
     * @return
     */
    public static byte[] toByteArray(final Bitmap bmp,
                                     boolean needRecycle) {
        return toByteArray(bmp, Bitmap.CompressFormat.JPEG, 80, needRecycle);
    }

    /**
     * 转换成字节数组
     *
     * @param format      转换格式
     * @param bmp         bitmap对象
     * @param quality     转换质量（0-100取值）
     * @param needRecycle 是否需要在转换之后回收bitmap
     * @return
     */
    public static byte[] toByteArray(final Bitmap bmp, Bitmap.CompressFormat format, int quality,
                                     boolean needRecycle) {
        ByteArrayOutputStream output = null;
        byte[] result = null;
        try {
            output = new ByteArrayOutputStream();
            bmp.compress(format, quality, output);
            if (needRecycle) {
                bmp.recycle();
            }
            result = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 把bitmap转换成Base64编码String
     *
     * @param bitmap
     * @return
     */
    public static String toBase64String(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        return Base64.encodeToString(toByteArray(bitmap, format, quality, false), Base64.DEFAULT);
    }

    /**
     * Drawable 转换成 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * Bitmap 转换成 Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable toDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * 字节数组转换成Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap toBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }


    /**
     * 获取bitmap的InputStream
     *
     * @param bm
     * @return
     */
    public static InputStream toInputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return isBm;
    }

    /**
     * 圆bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toCircleBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }



    /**
     * 保存bitmap为文件
     * 默认数据格式为jpeg，质量为100
     *
     * @param bitmap
     * @param path                    文件路径
     * @param isCreateDirWhenNotExist 当路径对应的目录不存在时是否创建目录
     * @return
     * @throws IOException
     */
    public static boolean saveToFile(Bitmap bitmap, String path, boolean isCreateDirWhenNotExist)
            throws IOException {
        return saveToFile(bitmap, path, Bitmap.CompressFormat.JPEG, 100, isCreateDirWhenNotExist);
    }

    /**
     * 保存为文件
     *
     * @param bitmap
     * @param path                    文件路径
     * @param format                  压缩格式
     * @param quality                 压缩质量
     * @param isCreateDirWhenNotExist 当路径对应的目录不存在时是否创建目录
     * @return
     * @throws IOException
     */
    public static boolean saveToFile(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality, boolean isCreateDirWhenNotExist)
            throws IOException {
        File file = new File(path);

        File pFile = file.getParentFile();
        if (pFile != null && !pFile.exists()) {
            if (isCreateDirWhenNotExist) {
                pFile.mkdirs();
            } else {
                throw new IOException("File directory is not exist");
            }
        }

        //删除原有文件
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(format, quality, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            if (file.exists())
                file.deleteOnExit();
            return false;
        }
    }
}
