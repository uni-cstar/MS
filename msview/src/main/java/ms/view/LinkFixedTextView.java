/**
 * @包名 ucux.app.widgets
 * @文件名 LinkFixedTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 */
package ms.view;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * @包名 ucux.app.widgets
 * @文件名 LinkFixedTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 * 修正ListView 的 Item中如果TextView包含超链接，只响应超链接点击事件而不响应ListView item的点击事件
 * 并提供接口可以供外部以内置浏览器的方式打开超链接(如果整个app点击超链接的操作都一致的话,可以考虑实现一个子类,并实现接口,在构造函数中设置接口)
 *
 * @版本 V 2.0
 * @日期 2016-12-27
 * @描述: 修正长按超链接不响应控件长按事件
 */

public class LinkFixedTextView extends TextView {

    private OnLinkFixedTextViewListener listener;

    //解决问题-重写touchevent之后,长按超链接并不会响应控件的长按时间,因此在up和down的时候,校验是否主动触发longclick事件
    CheckForLongPress mPendingCheckForLongPress;//长按延迟执行
    private boolean mHasPerformedLongPress = false;//是否已触发长按操作

    public LinkFixedTextView(Context context) {
        super(context);
    }

    public LinkFixedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkFixedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            //如果没有设置超链接监听,则不处理
            if (this.listener == null) {
                return super.onTouchEvent(event);
            }

            //如果控件被按下或者手指离开控件
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_UP) {
                //获取当前文本
                CharSequence text = this.getText();
                Spannable spanText = Spannable.Factory.getInstance().newSpannable(text);

                //获取当前手指坐标
                int x = (int) event.getX();
                int y = (int) event.getY();

                //计算偏移量
                x -= this.getTotalPaddingLeft();
                y -= this.getTotalPaddingTop();

                x += this.getScrollX();
                y += this.getScrollY();

                Layout layout = this.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                //获取包含的link
                ClickableSpan[] links = spanText.getSpans(off, off, ClickableSpan.class);

                //如果未包含链接,则返回默认处理
                if (links == null || links.length == 0)
                    return super.onTouchEvent(event);

                //如果包含了链接 : 链接的处理并不会影响控件本身的处理,两者事件处理相对独立,因此只专注 链接的处理
                int action = event.getAction();
                checkLongClickPerformForLinkState(action);
                // 如果手指弹起
                if (action == MotionEvent.ACTION_UP) {
                    //如果触发了长按操作,则取消系统的长按触发:手动触发的时间比系统时间更短,所以先于系统触发
                    if (mHasPerformedLongPress) {
                        cancelLongPress();
                    } else {
                        //触发链接点击回调
                        if (links[0] instanceof URLSpan) {
                            URLSpan span = (URLSpan) links[0];
                            this.listener.onLinkFixedURLSpanClick(this, span);
                        } else {
                            (links[0]).onClick(this);
                        }
                    }
                    Selection.removeSelection(spanText);
                    return true;
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(spanText, spanText.getSpanStart(links[0]), spanText.getSpanEnd(links[0]));
                    return true;
                }
            }
        } catch (Exception ex) {
            return super.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    /**
     * 检查长按触发
     *
     * @param motionEvent
     */
    private void checkLongClickPerformForLinkState(int motionEvent) {
        try {
            if (!this.isLongClickable())//不支持长按,则直接返回
                return;

            if (motionEvent == MotionEvent.ACTION_DOWN) {
                checkForLongClick();
            } else if (motionEvent == MotionEvent.ACTION_UP) {
                if (!mHasPerformedLongPress) {
                    removeLongPressCallback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置长按回调
    private void checkForLongClick() {
        if (this.isLongClickable()) {// 如果当前支持长按
            mHasPerformedLongPress = false;
            if (mPendingCheckForLongPress == null) {
                mPendingCheckForLongPress = new CheckForLongPress();
            }
            //-20毫秒,让手动触发的长按事件先于系统触发
            postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - 20);
        }
    }

    /**
     * 移除长按回调
     */
    private void removeLongPressCallback() {
        if (mPendingCheckForLongPress != null) {
            removeCallbacks(mPendingCheckForLongPress);
        }
    }


    private final class CheckForLongPress implements Runnable {
        @Override
        public void run() {
            if (isPressed()) {
                if (performLongClick()) {
                    mHasPerformedLongPress = true;
                }
            }
        }
    }

    public void setOnLinkFixedTextViewListener(
            OnLinkFixedTextViewListener listener) {
        this.listener = listener;
    }

    public interface OnLinkFixedTextViewListener {
        /**
         * 点击了文本中的超链接
         *
         * @param sender
         * @param clickedSpan 点击的URLSpan对象
         */
        void onLinkFixedURLSpanClick(LinkFixedTextView sender,
                                     URLSpan clickedSpan);
    }

}
