package ms.zxing;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import ms.zxing.core.RGBLuminanceSource;

public class ZXingManager {

    /**
     * 获取二维码扫描结果所使用的key，如果字符串为空，则没扫描到结果
     */
    public static final String EXTRA_ZXING_RESULT = "result";

    /**
     * 获取二维码扫描结果(界面效果是google自带的)
     *
     * @param activity
     * @param requestCode 请求码，通过onActivityResult返回结果. Key --
     *                    {@link ZXingManager#EXTRA_ZXING_RESULT}
     *                    <p>
     *                    <p>
     *                    在application工程的清单文件中申明Activity ：
     *                    ZXingScannerActivity
     *                    </p>
     *                    <p>
     *                    所需权限 <uses-permission android:name="android.permission.CAMERA"
     *                    /> <uses-feature android:name="android.hardware.camera" />
     *                    <uses-feature android:name="android.hardware.camera.autofocus"
     *                    /> <uses-permission
     *                    android:name="android.permission.VIBRATE"/>
     *                    </p>
     */
    public static void getQRCodeResult(Activity activity, int requestCode) {
        Intent it = new Intent(activity, ZXingScannerActivity.class);
        activity.startActivityForResult(it, requestCode);
    }

    /**
     * 获取二维码扫描结果(自定义界面)
     * <p>
     * 可继承重写界面处理扫描结果,默认通过onActivityResult返回结果， Key --
     * {@link ZXingManager#EXTRA_ZXING_RESULT}
     * </p>
     * <p>
     * 在application工程的清单文件中申明Activity ：
     * ZXingCustomerScannerActivity
     * </p>
     * <p>
     * 所需权限 <uses-permission android:name="android.permission.CAMERA" />
     * <uses-feature android:name="android.hardware.camera" /> <uses-feature
     * android:name="android.hardware.camera.autofocus" /> <uses-permission
     * android:name="android.permission.VIBRATE"/>
     * </p>
     *
     * @param activity
     * @param requestCode
     */
    public static void getQRCodeResultCustomer(Activity activity,
                                               int requestCode) {
        Intent it = new Intent(activity, ZXingCustomerScannerActivity.class);
        activity.startActivityForResult(it, requestCode);
    }


    /**
     * 扫描二维码图片（建议异步执行）
     *
     * @param path
     * @return
     */
    public static Result scanImageFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, Object> hints = getDefaultDecodeHint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize1 = (int) (options.outHeight / (float) 600);
        if (sampleSize1 <= 0)
            sampleSize1 = 1;
        int sampleSize2 = (int) (options.outWidth / (float) 600);
        if (sampleSize2 <= 0)
            sampleSize2 = 1;
        options.inSampleSize = Math.max(sampleSize1, sampleSize2);

        scanBitmap = BitmapFactory.decodeFile(path, options);
        return scanBitmap(scanBitmap, hints, true);
    }

    /**
     * 扫描图片
     *
     * @param srcBmp     图片源
     * @param hints      解析参数
     * @param recycleSrc 扫描结束之后是否回收图片
     * @return
     * @see com.google.zxing.DecodeHintType
     */
    public static Result scanBitmap(Bitmap srcBmp, Hashtable<DecodeHintType, Object> hints, boolean recycleSrc) {
        RGBLuminanceSource source = new RGBLuminanceSource(srcBmp);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } finally {
            if (srcBmp != null && recycleSrc && !srcBmp.isRecycled()) {
                srcBmp.recycle();
                srcBmp = null;
            }
        }
        return null;
    }

    /**
     * 创建带有标记的二维码
     *
     * @param content 数据字符串
     * @param markRes 中间小图标资源（图片不能太大）
     * @param width   二维码图片宽度（不能特别大）
     * @return
     * @throws WriterException
     */
    public static Bitmap createQRCodeBitmapWithMark(Context context,
                                                    int markRes, String content, int width) throws WriterException {
        Bitmap bitmap = createQRCodeBitmap(content, width, width);

        Bitmap markBitmap = BitmapFactory.decodeResource(
                context.getResources(), markRes);
        // 获取logo图片(默认为42)
        int logoLength = 42;// width / 5;
        if (markBitmap != null) {
            // 如果图标的宽或者高超过了指定的logo长度，则进行缩放
            if (logoLength < markBitmap.getWidth()
                    || logoLength < markBitmap.getHeight()) {
                markBitmap = zoomBitmap(markBitmap, logoLength, logoLength,
                        true);
            }
            // 获取重叠的图片
            return midMixtureBitmap(bitmap, markBitmap, Bitmap.Config.ARGB_8888);
        } else {
            return bitmap;
        }
    }

    /**
     * 缩放图片
     *
     * @param src        用于缩放的bitmap对象
     * @param desWidth   所需要的宽
     * @param desHeight  所需要的高
     * @param recycleSrc 是否回收源图片数据
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap src, int desWidth, int desHeight,
                                    boolean recycleSrc) {
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

        if (recycleSrc) {
            src.recycle();
        }
        return resizedBitmap;
    }

    /**
     * 居中混合图片（大图在前，小图在后） (居中混合，小图放在大图中间) 使用场景：二维码中间贴上logo
     *
     * @param srcBmp  大图
     * @param markBmp 小图
     * @param config  建议{@link Bitmap.Config#RGB_565},占用的内存更少
     * @return 混合之后的图片
     */
    public static Bitmap midMixtureBitmap(Bitmap srcBmp, Bitmap markBmp,
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
     * 根据字符串生成二维码图片
     *
     * @param str
     * @param codeWidth  指定宽
     * @param codeHeight 指定高
     * @return
     * @throws WriterException
     */
    public static Bitmap createQRCodeBitmap(String str, int codeWidth,
                                            int codeHeight) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);// 容错等级

        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, codeWidth, codeHeight, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 获取默认的二维码解析配置
     * 采用utf-8编码
     *
     * @return
     */
    public static Hashtable<DecodeHintType, Object> getDefaultDecodeHint() {
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码
        return hints;
    }

    /**
     * 截屏检查二维码
     * 最好放在子线程执行
     * @param activity
     * @param downY
     * @return 如果结果返回空字符串，则不存在二维码或二维码解析失败
     */
    public String screenshotForChekQrcode(Activity activity, float downY) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        if (bmp == null) {
            return null;
        }
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        boolean isPortrait = screenWidth < screenHeight;
        //截取截屏期望宽度
        int width = Math.min(screenWidth, screenHeight);

        int startY;
        int endY;
        if (isPortrait) {
            startY = (int) (downY - width / 2.0);
            endY = (int) (downY + width / 2.0);
            if (endY > screenHeight) { //如果需求的Y坐标超过了屏幕的高度，则以屏幕高度为Y轴基准
                startY = Math.max(0, screenHeight - width);
                endY = screenHeight;
            } else {
                if (startY < 0) {
                    startY = 0;
                }
            }
        } else {//如果是横屏，则直接截取屏幕高度
            startY = 0;
            endY = width;
        }

        //裁剪的图片，x轴方向永远跟手机保持一致
        Bitmap result = Bitmap.createBitmap(bmp, 0, startY, screenWidth, endY - startY);
        Hashtable<DecodeHintType, Object> hints = getDefaultDecodeHint();
        Result scanResult = scanBitmap(result, hints, false);
        return scanResult == null ? null : scanResult.getText();
    }
}
