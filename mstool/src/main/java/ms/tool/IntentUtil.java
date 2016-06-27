package ms.tool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 常用内置Intent操作
 * Created by SupLuo on 2016/2/24.
 */
public class IntentUtil {

    /**
     * 打开网页
     *
     * @param context
     * @param url     链接地址，必须以http开头
     */
    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        openUri(context, uri);
    }

    /**
     * 打开Uri
     *
     * @param context
     * @param uri     指定Uri
     */
    public static void openUri(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 打电话(调用拨号界面)
     *
     * @param context
     * @param tel     电话号码 （可不传）
     */
    public static void callPhone(Context context, String tel) {
        if (tel == null)
            tel = "";
        Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + tel));
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    /**
     * 发短信(调用发短信界面)
     *
     * @param context
     * @param tel     电话号码
     */
    public static void sendSMS(Context context, String tel) {
        sendSMS(context, tel, "");
    }

    /**
     * 发短信(调用发短信界面)
     *
     * @param context
     * @param tel          电话号码
     * @param extraContent 预设的短信内容
     */
    public static void sendSMS(Context context, String tel,
                               String extraContent) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", tel);
        intent.putExtra("sms_body", extraContent);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

    /**
     * 发邮件
     *
     * @param context
     * @param emailAddrs   邮箱地址数组
     * @param chooserTitle chooserTitle选择用于发送邮件的程序时显示的标题
     * @param subject      邮件主题（即邮件标题，可不传）
     * @param extraContent 预设的邮件内容（可不传）
     */
    public static void sendMail(Context context, String[] emailAddrs,
                                String chooserTitle, String subject, String extraContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        // 设置对方邮件地址
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddrs);

        // 设置标题内容
        if (!TextUtils.isEmpty(subject))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // 设置邮件文本内容
        if (!TextUtils.isEmpty(extraContent))
            intent.putExtra(Intent.EXTRA_TEXT, extraContent);

        context.startActivity(Intent.createChooser(intent, chooserTitle));
    }

    /**
     * 从相册获取图片
     * 通过{@link Activity#onActivityResult}方法接收返回的Intent参数，通过{@link Intent#getData()}得到选择的数据Uri
     * @param activity
     * @param requestCode
     */
    public static void getPhotoByAlbum(Activity activity, int requestCode) {
        Intent it = new Intent(Intent.ACTION_PICK);
        it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(it, requestCode);
    }

    /**
     * 拍照获取图片（不返回数据，通过URI获取）
     * 在{@link Activity#onActivityResult}方法中操作此方法的Uri参数，即可处理数据
     * @param activity
     * @param uri         用于存储拍照之后的图片
     * @param requestCode
     */
    public static void getPhotoByCapture(Activity activity, Uri uri, int requestCode) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // it.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, false);
        // it.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        // it.putExtra("return-data", false);// 不返回数据
        activity.startActivityForResult(it, requestCode);
    }

    /**
     * 获取文件
     * 通过{@link Activity#onActivityResult}方法接收返回的Intent参数，通过{@link Intent#getData()}得到选择的数据Uri
     * @param activity
     * @param requestCode
     */
    public static void getFileByDocument(Activity activity, int requestCode) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 复制内容到剪切板
     *
     * @param context
     * @param content
     *            复制的内容
     */
    public static void copyToClipboard(Context context, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            copyToClipboardHigh(context, content);
        } else {
            copyToClipboardLow(context, content);
        }
    }

    /**
     * 复制内容到剪切板 版本号 >= 11
     *
     * @param context
     * @param content
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void copyToClipboardHigh(Context context, String content) {
		// android.content.ClipboardManager cManager =
		// (android.content.ClipboardManager) context
		// .getApplicationContext().getSystemService(
		// Context.CLIPBOARD_SERVICE);
		// cManager.setText(content);
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE); 
        ClipData clip = ClipData.newPlainText("label", content); 
        clipboard.setPrimaryClip(clip); 

    }

    /**
     * 复制内容到剪切板 版本号 < 11
     *
     * @param context
     * @param content
     * @return
     */
    private static void copyToClipboardLow(Context context, String content) {
        android.text.ClipboardManager cManager = (android.text.ClipboardManager) context
                .getApplicationContext().getSystemService(
                        Context.CLIPBOARD_SERVICE);
        cManager.setText(content);
    }
}
