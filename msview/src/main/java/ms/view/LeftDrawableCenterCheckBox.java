/**
 * @包名 ucux.app.widgets
 * @文件名 LeftDrawableCenterCheckBox.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * @包名 ucux.app.widgets
 * @文件名 LeftDrawableCenterCheckBox.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 * DrawableLeft和文本 居中显示 的CheckBox控件
 */

public class LeftDrawableCenterCheckBox extends CheckBox {

    public LeftDrawableCenterCheckBox(Context context) {
        super(context);
    }

    public LeftDrawableCenterCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeftDrawableCenterCheckBox(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        try {
//            Drawable[] drawables = this.getCompoundDrawables();
//            if (drawables != null) {
//                Drawable drawableLeft = drawables[0];
//                if (drawableLeft != null) {
//                    float textWidth = this.getPaint().measureText(
//                            this.getText().toString());
//                    int drawablePadding = this.getCompoundDrawablePadding();
//                    int drawableWidth = 0;
//                    drawableWidth = drawableLeft.getIntrinsicWidth();
//                    float bodyWidth = textWidth + drawableWidth
//                            + drawablePadding;
//                    canvas.translate((this.getWidth() - bodyWidth) / 2, 0);
//                }
//            }
//        } catch (Exception e) {
//        }
        LeftDrawableCenterTextView.drawLeftDrawableCenter(this, canvas);
        super.onDraw(canvas);
    }

}
