package ms.tool;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapUtil2 {

	/**
	 * 获取合适的inSampleSize
	 *
	 * @param outW      图片宽
	 * @param outH      图片高
	 * @param maxW      最大宽
	 * @param maxHeight 最大高
	 * @return
	 */
	public static int getInSampleSize(int outW, int outH, int maxW, int maxHeight) {
		float var4 = (float) outW / (float) outH;
		float var5 = (float) maxW / (float) maxHeight;
		return var4 > var5 ? outW / maxW : outH / maxHeight;
	}

	/**
	 * 读取图片的旋转角度
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static int readPictureDegree(String path) throws IOException {
		int degree;
		ExifInterface exifInterface = new ExifInterface(path);
		int orientation = exifInterface.getAttributeInt(
				ExifInterface.TAG_ORIENTATION, 1);
		switch (orientation) {
		case 5:
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;

		case ExifInterface.ORIENTATION_ROTATE_180:
		case 4:
			degree = 180;
			break;

		case 7:
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;

		default:
			degree = 0;
			break;
		}
		return degree;
	}

	/**
	 * 解析Bitmap的公用方法. 数据源只需提供一种
	 * 
	 * @param path
	 *            图片路径字符串
	 * @param data
	 *            图片的字节数组
	 * @param context
	 * @param uri
	 *            图片的Uri
	 * @param options
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap decode(String path, byte[] data, Context context,
			Uri uri, BitmapFactory.Options options)
			throws FileNotFoundException {
		Bitmap result = null;

		if (path != null) {
			result = BitmapFactory.decodeFile(path, options);
		} else if (data != null) {
			result = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} else if (uri != null) {
			ContentResolver cr = context.getContentResolver();
			result = BitmapFactory.decodeStream(cr.openInputStream(uri), null,
					options);
		}

		return result;
	}

	/**
	 * 获取图片的BitmapFactory.Options
	 * 
	 * @param path
	 * @param data
	 * @param context
	 * @param imaUri
	 * @return
	 * @throws FileNotFoundException
	 */
	private static BitmapFactory.Options getImageInfoOptions(String path,
			byte[] data, Context context, Uri imaUri)
			throws FileNotFoundException {
		BitmapFactory.Options optionsInfo = new BitmapFactory.Options();
		// 这里设置true的时候，decode时候Bitmap返回的为空，
		// 将图片宽高读取放在Options里.
		optionsInfo.inJustDecodeBounds = true;
		decode(path, data, context, imaUri, optionsInfo);
		return optionsInfo;
	}

	/**
	 * 计算图片压缩比
	 * 
	 * @param imgWidth
	 * @param imgHeight
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static float getImageScaleRadio(int imgWidth, int imgHeight,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		int height = imgHeight;
		int width = imgWidth;
		float inSampleSize = 1;
		if (width > reqWidth || height > reqHeight) {
			float temp = (height * reqWidth) / reqHeight;
			if (width >= temp) {
				inSampleSize = (width * 1.0f) / reqWidth;
			} else {
				inSampleSize = (height * 1.0f) / reqHeight;
			}
		}
		return Math.max(inSampleSize, 0.01f);
	}

	/**
	 * 通过需求的宽和高来获取适当的InSampleSize
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSizeWithWidthAndHeight(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth || height > reqHeight) {
			int temp = (height * reqWidth) / reqHeight;
			if (width >= temp) {
				inSampleSize = (int) Math.round((1.0 * width) / reqWidth);
			} else {
				inSampleSize = (int) Math.round((height * 1.0) / reqHeight);
			}
		}
		return inSampleSize;
	}

	/**
	 * 创建一个合适的用于获取图片的BitmapFactory.Options
	 * 
	 * @param context
	 * @param path
	 * @param width
	 * @param height
	 * @param jpegQuality
	 * @return
	 * @throws FileNotFoundException 
	 */
	private static BitmapFactory.Options createBitmapOptions(Context context,
			String path, int width, int height, int jpegQuality) throws FileNotFoundException {
		BitmapFactory.Options imgInfoOptions = getImageInfoOptions(path, null,
				context, null);
		// 计算压缩比
		float radio = getImageScaleRadio(imgInfoOptions.outWidth,
				imgInfoOptions.outHeight, width, height);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSizeWithWidthAndHeight(
				imgInfoOptions,
				(int) Math.ceil((imgInfoOptions.outWidth * 1.0 / radio + 0.1)),
				(int) Math.ceil((imgInfoOptions.outHeight * 1.0 / radio + 0.1)));
		return options;
	}

	/**
	 * 创建一个合适的用于获取图片的BitmapFactory.Options
	 * @param context
	 * @param imgUri
	 * @param width
	 * @param height
	 * @param jpegQuality
	 * @return
	 * @throws FileNotFoundException
	 */
	private static BitmapFactory.Options createBitmapOptions(Context context,
			Uri imgUri, int width, int height, int jpegQuality) throws FileNotFoundException {
		BitmapFactory.Options imgInfoOptions = getImageInfoOptions(null, null, context, imgUri);

		float radio = getImageScaleRadio(imgInfoOptions.outWidth,
				imgInfoOptions.outHeight, width, height);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = (int) radio;
		return options;
	}

    public static Bitmap getCompressBitmap(Context context,String path, int width, int height, int jpegQuality) throws FileNotFoundException
    {
    	BitmapFactory.Options options = createBitmapOptions(context, path, width, height, jpegQuality);
    	return decode(path, null, context, null, options);
    }
    
	/**
	 * 获取上传的图片对象
	 * @param context
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static Bitmap getCorrectedCompressImageStream(Context context, String path,int maxWidth,int maxHeight,int jpegQuality) throws IOException
    {
        //获取偏转角度
        int degree = readPictureDegree(path);

        if (degree == 0)
        {
        	return getCompressBitmap(context, path, maxWidth, maxHeight, jpegQuality);
        }
        else
        {
        	Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            
            Bitmap bitmap = getCompressBitmap(context, path, maxWidth, maxHeight, jpegQuality);
            if(bitmap == null) return null;
            
            Bitmap bitmapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bitmapRotate;
           
        }
    }
}
