/**
 * @包名 ucux.app.widgets
 * @文件名 PMEditText.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @包名 ucux.app.widgets
 * @文件名 PMEditText.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 * 用于私信界面的输入控件  使用场景：在点击控件的时候，关闭底部操作面板，弹出软键盘
 */

public class TapEditText extends EditText {
    OnEditTextTapListener onTapListener;

    public TapEditText(Context context) {
        super(context);
    }

    public TapEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TapEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && onTapListener != null) {
            onTapListener.onEditTextTaped();
        }
        return super.onTouchEvent(event);
    }

    public void setOnEditTapListener(OnEditTextTapListener onEditTapListener) {
        this.onTapListener = onEditTapListener;
    }

    public interface OnEditTextTapListener {
        void onEditTextTaped();
    }
}
