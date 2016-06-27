package ms.tool;

import android.graphics.Paint.FontMetrics;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class TextUtil {
	
	/**
	 * 渲染 字符串
	 * @param source 源字符串
	 * @param tag 需要渲染的部分
	 * @param color 渲染颜色
	 * @return
	 */
	public static SpannableStringBuilder getColorSpan(String source,String tag ,int color){
	    int fstart=source.indexOf(tag);
	    int fend=fstart + tag.length();
	    SpannableStringBuilder style=new SpannableStringBuilder(source);
	    style.setSpan(new ForegroundColorSpan(color),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		return style;
	}

	/**
	 * 获取文本高度
	 * @param paint
	 * @return
	 */
	public static int getTextHeight(TextPaint paint) {
		FontMetrics fm = paint.getFontMetrics();
		// return (int) Math.ceil(fm.descent - fm.top) + 2;
		return (int) Math.ceil(fm.descent - fm.ascent);
	}
	
}
