/**
 * @包名   ucux.app.widgets
 * @文件名 RoundImageView.java
 * @作者   luochao
 * @创建日期 2015-6-9
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * @包名 ucux.app.widgets
 * @文件名 RoundImageView.java
 * @作者 luochao
 * @创建日期 2015-6-9
 * @版本 V 1.0
 * 提供圆形or圆角形状的图片控件
 * 见属性RoundImageView定义
 */
public class RoundImageView extends ImageView {
	/**
	 * 图片的类型，圆形or圆角
	 */
	private int type;

	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;

	/**
	 * 圆角的大小
	 */
	private int mBorderRadius;

	/**
	 * 圆角大小的默认值 6dp
	 */
	private static final int BODER_RADIUS_DEFAULT = 6;

	/**
	 * 绘图的Paint
	 */
	private Paint mBitmapPaint,borderPaint;

	/**
	 * 圆行的半径
	 */
	private int mRadius;

	/**
	 * 3x3 矩阵，主要用于缩小放大
	 */
	private Matrix mMatrix;
	/**
	 * 渲染图像，使用图像为绘制图形着色
	 */
	private BitmapShader mBitmapShader;
	/**
	 * view的宽度
	 */
	private int mWidth;
	private RectF mRoundRect;

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);// 抗锯齿

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RoundImageView);

		mBorderRadius = a.getDimensionPixelSize(
				R.styleable.RoundImageView_borderRadius, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
								BODER_RADIUS_DEFAULT, context.getResources()
										.getDisplayMetrics()));// 默认为6dp
		type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle

		a.recycle();
	}

	public RoundImageView(Context context) {
		super(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
		 */
		if (type == TYPE_CIRCLE) {
			mWidth = Math
					.min(this.getMeasuredWidth(), this.getMeasuredHeight());
			mRadius = mWidth / 2;
			setMeasuredDimension(mWidth, mWidth);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			if (this.getDrawable() == null)
				return;

			setUpShader();

			// 绘制圆角
			if (type == TYPE_ROUND) {
				canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
						mBitmapPaint);
			}
			// 绘制圆形
			else {
	/*	        paint.setColor(bColor); // 边框颜色
		        paint.setAntiAlias(true);// 设置抗锯齿
		        c.drawCircle(d / 2, d / 2, d / 2, paint); */
		        
				canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
			}
		} catch (Exception e) {
			super.onDraw(canvas);
		}

	}

	/**
	 * 初始化BitmapShader
	 */
	private void setUpShader() {
		Drawable drawable = this.getDrawable();

		Bitmap bmp = drawableToBitamp(drawable);
		if (bmp == null)
			return;

		// 将bmp作为着色器，就是在指定区域内绘制bmp
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		float scale = 1.0f;
		if (type == TYPE_CIRCLE) {
			// 拿到bitmap宽或高的小值
			int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
			scale = mWidth * 1.0f / bSize;

		} else if (type == TYPE_ROUND) {
			// 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
			if (!(bmp.getWidth() == this.getWidth() && bmp.getHeight() == this
					.getHeight())) {
				scale = Math.max(this.getWidth() * 1.0f / bmp.getWidth(),
						this.getHeight() * 1.0f / bmp.getHeight());
			}

		}
		// shader的变换矩阵，我们这里主要用于放大或者缩小
		mMatrix.setScale(scale, scale);
		// 设置变换矩阵
		mBitmapShader.setLocalMatrix(mMatrix);
		// 设置shader
		mBitmapPaint.setShader(mBitmapShader);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		// 圆角图片的范围
		if (type == TYPE_ROUND)
			mRoundRect = new RectF(0, 0, w, h);
	}

	/**
	 * drawable转bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}

		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	
	private static final String STATE_INSTANCE = "state_instance";
	private static final String STATE_TYPE = "state_type";
	private static final String STATE_BORDER_RADIUS = "state_border_radius";

	/**
	 * 保存状态
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		try {
			Bundle bundle = new Bundle();
			bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
			bundle.putInt(STATE_TYPE, type);
			bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
			return bundle;
		} catch (Exception ex) {
			return super.onSaveInstanceState();
		}
	}

	/**
	 * 恢复保存的状态
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		try {
			if (state instanceof Bundle) {
				Bundle bundle = (Bundle) state;
				super.onRestoreInstanceState((Parcelable) bundle
						.getParcelable(STATE_INSTANCE));
				this.type = bundle.getInt(STATE_TYPE);
				this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
			} else {
				super.onRestoreInstanceState(state);
			}
		} catch (Exception ex) {
			super.onRestoreInstanceState(state);
		}

	}

	/**
	 * 设置圆角半径
	 * @param borderRadius
	 */
	public void setBorderRadius(int borderRadius) {
		int pxVal = dp2px(borderRadius);
		//如果当前是圆角，并且圆角半径与之前的圆角半径不相等，重绘
		if (this.mBorderRadius != pxVal) {
			this.mBorderRadius = pxVal;
			if( this.type == TYPE_ROUND){
				this.invalidate();
			}
		}
	}

	/**
	 * 设置图片形状
	 * 参考值：TYPE_ROUND & TYPE_CIRCLE
	 * 如果传递的值不在上述两者之内，则默认为TYPE_CIRCLE
	 */
	public void setType(int type) {
		if (this.type != type) {
			this.type = type;
			if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
				this.type = TYPE_CIRCLE;
			}
			requestLayout();
		}
	}

	private int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,this.getResources().getDisplayMetrics());
	}

}
