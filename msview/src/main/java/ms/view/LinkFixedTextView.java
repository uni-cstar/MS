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
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * @包名 ucux.app.widgets
 * @文件名 LinkFixedTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 * 修正ListView 的 Item中如果TextView包含超链接，只响应超链接点击事件而不响应ListView item的点击事件
 * 并提供接口可以供外部以内置浏览器的方式打开超链接(如果整个app点击超链接的操作都一致的话,可以考虑实现一个子类,并实现接口,在构造函数中设置接口)
 * @版本 V 2.0
 * @日期 2016-12-27
 * @描述: 修正长按超链接不响应控件长按事件
 * @######备注 如果要让控件能够支持listview的itemclick等事件,则{@link #setAttachToAdapterView}
 */

public class LinkFixedTextView extends TextView {

    private OnLinkFixedTextViewListener listener;

    //解决问题-重写touchevent之后,长按超链接并不会响应控件的长按时间,因此在up和down的时候,校验是否主动触发longclick事件
    View.OnLongClickListener mLongClickListenerCache = null;
    CheckForLongPress mPendingCheckForLongPress;//长按延迟执行
    private boolean mHasPerformedLongPress = false;//是否已触发长按操作

    //
    View.OnClickListener mClickListenerCache = null;

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
    public void setOnClickListener(OnClickListener l) {
        mClickListenerCache = l;
        super.setOnClickListener(l);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mLongClickListenerCache = l;
        super.setOnLongClickListener(l);
    }

    /**
     * 在adapterview中使用这个控件,会与adapterview的itemclick等事件冲突.如果要让adapterview的事件生效,则需要调用此方法,
     * 否则可以在控件的click等相应事件中实现类似adapterview的事件效果
     */
    public void setAttachToAdapterView() {
        this.setFocusable(false);
        this.setClickable(false);
        this.setLongClickable(false);
    }

    /**
     * 是否可以响应点击
     *
     * @return
     */
    private boolean isEnableClickPerform() {
        return this.isClickable() && mClickListenerCache != null;
    }

    /**
     * 是否可以响应长按
     *
     * @return
     */
    private boolean isEnableLongClickPerform() {
        return this.isLongClickable() && mLongClickListenerCache != null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            //如果没有设置超链接监听,则不处理
            if (this.listener == null) {
                return super.onTouchEvent(event);
            }

            int action = event.getAction();
            //如果控件被按下或者手指离开控件
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
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


                //如果点击区域不包含链接
                if (links == null || links.length == 0) {
                    //如果控件本身要响应点击或者长按操作,则返回super.onTouchEvent(event)
                    if (isEnableClickPerform() || isEnableLongClickPerform()) {
                        return super.onTouchEvent(event);
                    } else {
                        //此方法可以从控件本身思考出发去触发被adapterview包涵的情况,但是这样没有必要,还的考虑本身的click与adapterview的itemviewclick的兼容情况
//                        try {
//                            //解决ListView中包涵链接的情况
//                            View child = this;
//                            ViewParent parent = this.getParent();
//                            while (parent != null) {
//                                if (parent instanceof AdapterView) {//
//                                    AdapterView adapterView = (AdapterView) parent;
//                                    int position = adapterView.getPositionForView(child);
//                                    long id = adapterView.getItemIdAtPosition(position);
//                                    AdapterView.OnItemClickListener itemClickListener = adapterView.getOnItemClickListener();
//                                    if (itemClickListener != null) {
//                                        itemClickListener.onItemClick(adapterView, child, position, id);
//                                    }
//                                    break;
//                                } else {
//                                    child = (View) parent;
//                                    parent = child.getParent();
//                                }
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        return false;
                    }
                }


                //如果包含了链接 : 链接的处理并不会影响控件本身的处理,两者事件处理相对独立,因此只专注 链接的处理
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
