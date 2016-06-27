/**
 * @包名 ucux.app.widgets
 * @文件名 DrawableCenterTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @包名 ucux.app.widgets
 * @文件名 DrawableCenterTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 * DrawableLeft和文本 居中显示 的文本控件
 */
public class LeftDrawableCenterTextView extends TextView {

    public LeftDrawableCenterTextView(Context context) {
        super(context);
    }

    public LeftDrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeftDrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawLeftDrawableCenter(this, canvas);
        super.onDraw(canvas);
    }

    public static void drawLeftDrawableCenter(TextView view, Canvas canvas) {
        try {
            Drawable[] drawables = view.getCompoundDrawables();
            if (drawables != null) {
                Drawable drawableLeft = drawables[0];
                if (drawableLeft != null) {
                    //获取文字宽度
                    float textWidth = view.getPaint().measureText(view.getText().toString());
                    //获取设置的图片间隔  android:drawablePadding所设置的值
                    int drawablePadding = view.getCompoundDrawablePadding();
                    int drawableWidth = 0;
                    //获取图像缩放后的宽度，如果是纯色，则返回-1
                    drawableWidth = drawableLeft.getIntrinsicWidth();
                    //内容的总宽度  ： 文字宽度 + 文字与图片的间隙宽度 + 绘制的图片宽度
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    //平移画布
                    canvas.translate((view.getWidth() - bodyWidth) / 2, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}