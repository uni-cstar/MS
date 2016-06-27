/**
 * @包名   ucux.app.widgets
 * @文件名 InnerGridView.java
 * @作者   luochao
 * @创建日期 2015-6-9
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @包名 ucux.app.widgets
 * @文件名 InnerGridView.java
 * @作者 luochao
 * @创建日期 2015-6-9
 * @版本 V 1.0 用于解决ScrollView/listview/gridview等组件之间的一些嵌套问题
 *     让控件的内容平铺，如果控件的内容设置为wrap_content，则只会显示一行数据
 */
public class ExpandedGridView extends GridView {

	public ExpandedGridView(Context context) {
		super(context);
	}

	public ExpandedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mTouchInvalidPosListener == null) {
			return super.onTouchEvent(event);
		}
		if (!isEnabled()) {
			// A disabled view that is clickable still consumes the touch
			// events, it just doesn't respond to them.
			return isClickable() || isLongClickable();
		}
		final int motionPosition = pointToPosition((int) event.getX(),
				(int) event.getY());
		if (motionPosition == INVALID_POSITION) {
			super.onTouchEvent(event);
			return mTouchInvalidPosListener.onTouchInvalidPosition(event
					.getActionMasked());
		}
		return super.onTouchEvent(event);
	}

	// 覆写此方法主要是为了让控件的内容能够平铺，通常解决ScrollView/listview/gridview等组件之间的一些嵌套问题
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 三种模式：
		// ①、UNSPECIFIED(未指定)，父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小；
		// ②、EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
		// ③、AT_MOST(至多)，子元素至多达到指定大小的值。
		// 根据提供的大小值和模式，创建一个测量值(格式)

		// 右移两位的原因是因为前两位会用于保存模式值
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	/**
	 * 当点击GridView空白区域时调用的监听
	 * 场景：gridview内嵌到listview中，但并没有铺满内容，这个时候点击gridview的空白区域想触发listview的item点击事件，即可使用此方法
	 * @author admin
	 *
	 */
	public interface OnTouchInvalidPositionListener {
		/**
		 * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者
		 * MotionEvent.ACTION_UP等来按需要进行判断
		 * 
		 * @return 是否要终止事件的路由
		 */
		boolean onTouchInvalidPosition(int motionEvent);
	}

	OnTouchInvalidPositionListener mTouchInvalidPosListener;
	
	/**
	 * 点击空白区域时的响应和处理接口
	 */
	public void setOnTouchInvalidPositionListener(
			OnTouchInvalidPositionListener listener) {
		mTouchInvalidPosListener = listener;
	}
}
