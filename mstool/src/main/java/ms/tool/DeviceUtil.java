package ms.tool;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by SupLuo on 2016/2/24.
 */
public class DeviceUtil {

	/**
	 * 网络类型-无网络
	 */
	public static final int NETWORK_TYPE_NONE = 0;

	/**
	 * 网络类型-未知网络
	 */
	public static final int NETWORK_TYPE_UNKNOWN = 1;

	/**
	 * 网络类型-GPRS
	 */
	public static final int NETWORK_TYPE_MOBILE = 2;

	/**
	 * 网络类型-wifi
	 */
	public static final int NETWORK_TYPE_WIFI = 3;

	/**
	 * 判断设备是否接入网络
	 * 
	 * @param context
	 * @return true：连接了网络
	 */
	public static boolean isOnLine(Context context) {
		ConnectivityManager conn_Manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conn_Manager.getActiveNetworkInfo();
		return netInfo != null && netInfo.isAvailable();
	}

	/**
	 * 获取当前设备网络连接类型
	 * 
	 * @param context
	 * @return {@link DeviceUtil#NETWORK_TYPE_NONE},
	 *         {@link DeviceUtil#NETWORK_TYPE_UNKNOWN},
	 *         {@link DeviceUtil#NETWORK_TYPE_MOBILE},
	 *         {@link DeviceUtil#NETWORK_TYPE_WIFI}
	 */
	public static int getNetWorkType(Context context) {
		int type = NETWORK_TYPE_NONE;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			switch (networkInfo.getType()) {
			// 移动网络
			case ConnectivityManager.TYPE_MOBILE:
				type = NETWORK_TYPE_MOBILE;
				break;
			// wifi网络
			case ConnectivityManager.TYPE_WIFI:
				type = NETWORK_TYPE_WIFI;
				break;
			default:
				type = NETWORK_TYPE_UNKNOWN;
				break;
			}
		}
		return type;
	}

	/**
	 * Gps是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * wifi是否打开
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断屏幕是黑的还是亮的
	 * 
	 * @param context
	 * @return true:屏幕亮着
	 */
	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context.getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	/**
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
	 * 
	 * @param context
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 判断是否为平板 通过计算设备尺寸大小的方法来判断是手机还是平板 大于 6 inch 则视为pad
	 * 
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isTablet2(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		float screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		return (screenInches >= 6.0);
	}

	/**
	 * 手机运行版本号如：4.2.1 更多信息{@link Build}
	 */
	public static String getOSVerionCode() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取系统名称 品牌(android系统定制商)-型号：Xiaomi-MI 3
	 */
	public static String getDeviceName() {
		return Build.PRODUCT;
	}

	/**
	 * 获取系统名称 品牌(android系统定制商)-型号：Xiaomi-MI 3
	 */
	public static String getDeviceModel() {
		return Build.BRAND + "-" + Build.MODEL;
	}

	/**
	 * 获取标志手机设备的唯一UUID(自定义方法，并不是具有权威性) 权限：android.permission.READ_PHONE_STATE
	 * DeviceId + androidId + sim卡序列号 + SubscriberId + Build.SERIAL
	 * 
	 * @param context
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getUUID(Context context)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		TelephonyManager tm = (TelephonyManager) context
				.getApplicationContext().getSystemService(
						Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();

		// 设备ID
		String deviceId = tm.getDeviceId();
		if (!TextUtils.isEmpty(deviceId))
			sb.append(deviceId);

		// androidId
		String androidId = android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		if (!TextUtils.isEmpty(androidId))
			sb.append(androidId);

		// sim卡序列号
		String simSerialNumber = tm.getSimSerialNumber();
		if (!TextUtils.isEmpty(simSerialNumber))
			sb.append(simSerialNumber);

		String subScriberID = tm.getSubscriberId();
		if (!TextUtils.isEmpty(subScriberID))
			sb.append(subScriberID);

		String serialNo = Build.SERIAL;
		if (!TextUtils.isEmpty(serialNo))
			sb.append(serialNo);
		return sb.toString();
	}

	/**
	 * 判断是否有软控制键（手机底部几个按钮）
	 * 
	 * @param activity
	 * @return
	 */
	public boolean isSoftKeyAvail(Activity activity) {
		final boolean[] isSoftkey = { false };
		final View activityRootView = (activity).getWindow().getDecorView()
				.findViewById(android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int rootViewHeight = activityRootView.getRootView()
								.getHeight();
						int viewHeight = activityRootView.getHeight();
						int heightDiff = rootViewHeight - viewHeight;
						if (heightDiff > 100) { // 99% of the time the height
												// diff will be due to a
												// keyboard.
							isSoftkey[0] = true;
						}
					}
				});
		return isSoftkey[0];
	}

	/**
	 * 隐藏键盘 ：强制隐藏
	 * 
	 * @param context
	 */
	public static void hideSoftInput(Context context) {
		try {
			if (!(context instanceof Activity))
				return;

			View view = ((Activity) context).getCurrentFocus();
			if (view == null)
				return;

			InputMethodManager inputmanger = (InputMethodManager) context
					.getApplicationContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示输入法
	 * 
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view) {
		try {
			view.requestFocus();
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切换软键盘
	 * 
	 * @param context
	 */
	public static void toggleSoftInput(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 软键盘是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSoftInputShow(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();

	}

}
