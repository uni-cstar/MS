package ms.zxing;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

import ms.zxing.core.CameraManager;
import ms.zxing.core.CaptureActivityHandler;
import ms.zxing.core.IZXingScanner;
import ms.zxing.core.InactivityTimer;

/**
 * 滚动扫描线扫描方式解析二维码（类似微信效果） 可以继承此界面实现特性处理
 * 
 * @see #createViews
 * @see #createNavigationLayout
 * @see #handleResult
 * @author SupLuo
 * 
 */
public class ZXingCustomerScannerActivity extends Activity implements Callback,
		ResultPointCallback, IZXingScanner {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private FrameLayout scanBgFlot;
	private FrameLayout navLayout;
	private View animView;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createViews();
		possibleResultPoints = new HashSet<ResultPoint>(5);
		CameraManager.init(getApplication());
	}

	/**
	 * 重写此方法创建不同的界面
	 * 
	 * @see #createNavigationLayout
	 */
	protected void createViews() {
		setContentView(R.layout.activity_qrcode_scan_customer);
		initViews();
	}

	TranslateAnimation scanAnim;

	private void initViews() {
		navLayout = (FrameLayout) this.findViewById(R.id.fl_nav_container);
		scanBgFlot = (FrameLayout) this.findViewById(R.id.scanBgFlot);
		int width = this.getResources().getDisplayMetrics().widthPixels;
		width = width / 9 * 5;
		LayoutParams lp = (LayoutParams) scanBgFlot
				.getLayoutParams();
		lp.width = width;
		lp.height = width;
		scanBgFlot.setLayoutParams(lp);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		animView = this.findViewById(R.id.animView);
		initAnim(width);

		createNavigationLayout(navLayout);

	}

	/**
	 * if you do not override {@link #createViews} method,you can override this
	 * method to create navigation bar
	 * 
	 * @return
	 */
	protected void createNavigationLayout(ViewGroup navGroup) {
		Button btnCancel = (Button) this.findViewById(R.id.btn_cancel_scan);
		btnCancel.setVisibility(View.VISIBLE);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ZXingCustomerScannerActivity.this.finish();
			}
		});
	}

	/**
	 * override this method to handle the qrcode result
	 * 
	 * @param result
	 */
	protected void handleResult(String result) {
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(ZXingManager.EXTRA_ZXING_RESULT, result);
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);
		ZXingCustomerScannerActivity.this.finish();
	}

	/**
	 * 扫描线动画
	 * 
	 * @param height
	 */
	private void initAnim(int height) {
		if (scanAnim == null) {
			scanAnim = new TranslateAnimation(0, 0, 0, height);
			scanAnim.setDuration(2500);
			scanAnim.setInterpolator(AnimationUtils.loadInterpolator(
					this.getApplicationContext(),
					android.R.anim.accelerate_decelerate_interpolator));
			scanAnim.setRepeatMode(Animation.RESTART);
			scanAnim.setRepeatCount(Animation.INFINITE);
			animView.setAnimation(scanAnim);
		} else {
			scanAnim.cancel();
			scanAnim.reset();
		}
		scanAnim.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();

		animView.clearAnimation();
		if (scanAnim != null)
			scanAnim.cancel();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.zxing_beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		handleResult(resultString);
	}

	private Collection<ResultPoint> possibleResultPoints;

	@Override
	public void foundPossibleResultPoint(ResultPoint arg0) {
		possibleResultPoints.add(arg0);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler getHandler() {
		return handler;
	}

	@Override
	public void reStart() {

	}

	@Override
	public ResultPointCallback getResultCallBack() {
		return this;
	}

}