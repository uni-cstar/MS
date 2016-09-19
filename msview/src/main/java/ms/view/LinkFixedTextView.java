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
import android.widget.TextView;

/**
 * @包名 ucux.app.widgets
 * @文件名 LinkFixedTextView.java
 * @作者 luochao
 * @创建日期 2015-6-8
 * @版本 V 1.0
 *
 *     修正ListView 的 Item中如果TextView包含超链接，只响应超链接点击事件而不响应ListView item的点击事件
 *     并提供接口可以供外部以内置浏览器的方式打开超链接(如果整个app点击超链接的操作都一致的话,可以考虑实现一个子类,并实现接口,在构造函数中设置接口)
 */

public class LinkFixedTextView extends TextView {

    private OnLinkFixedTextViewListener listener;

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
            if (this.listener == null) {
                return super.onTouchEvent(event);
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_UP) {
                CharSequence text = this.getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(
                        text);

                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= this.getTotalPaddingLeft();
                y -= this.getTotalPaddingTop();

                x += this.getScrollX();
                y += this.getScrollY();

                Layout layout = this.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] links = stext.getSpans(off, off,
                        ClickableSpan.class);

                if (links.length != 0) {
                    // 如果手指弹起
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        // 只需else 部分的处理即可解决listview
                        // 中textview包含超链接的问题，但是由于工作需求单独对url的点击做了额外的处理用自定义浏览器打开

                        // 如果是超链接 则以内置浏览器打开
                        if (links[0] instanceof URLSpan) {
                            URLSpan span = (URLSpan) links[0];

                            this.listener.onLinkFixedURLSpanClick(this, span);
                            // // 如果是合法的URL 则使用内置浏览器加载
                            // if (ValidUtil.isUrl(span.getURL())) {
                            // PBIntentUtl.runInnerBrowser(this.getContext(),
                            // span.getURL());
                            // } else {
                            // ((ClickableSpan) links[0]).onClick(this);
                            // }
                        } else {
                            ((ClickableSpan) links[0]).onClick(this);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(stext,
                                stext.getSpanStart(links[0]),
                                stext.getSpanEnd(links[0]));
                    }
                    return true;
                } else {
                    Selection.removeSelection(stext);
                    return super.onTouchEvent(event);
                }
            }
        } catch (Exception ex) {
            return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);

    }

    public void setOnLinkFixedTextViewListener(
            OnLinkFixedTextViewListener listener) {
        this.listener = listener;
    }

    public interface OnLinkFixedTextViewListener {
        /**
         * 点击了文本中的超链接
         * @param sender
         * @param clickedSpan 点击的URLSpan对象
         */
        void onLinkFixedURLSpanClick(LinkFixedTextView sender,
                                     URLSpan clickedSpan);
    }

}
