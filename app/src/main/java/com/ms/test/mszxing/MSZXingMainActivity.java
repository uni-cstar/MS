package com.ms.test.mszxing;

import com.google.zxing.Result;
import com.ms.R;
import ms.tool.UriUtil;
import ms.zxing.ZXingManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MSZXingMainActivity extends Activity implements OnClickListener {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * 显示扫描结果
	 */
	private TextView resultFile, result;
	/**
	 * 显示扫描拍的图片
	 */
	private ImageView mImageView, mImageView2;

	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_zxing_main);

		resultFile = (TextView) findViewById(R.id.resultFile);
		result = (TextView) findViewById(R.id.result);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		mImageView2 = (ImageView) findViewById(R.id.qrcode_bitmap2);
		// 点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		// 扫描完了之后调到该界面
		Button mButton = (Button) findViewById(R.id.scanFile);
		mButton.setOnClickListener(this);

		Button mScan = (Button) findViewById(R.id.scan);
		mScan.setOnClickListener(this);
		Button mScan2 = (Button) findViewById(R.id.scan2);
		mScan2.setOnClickListener(this);
		Button mScan3 = (Button) findViewById(R.id.scan3);
		mScan3.setOnClickListener(this);

		Button mCreate = (Button) findViewById(R.id.create);
		mCreate.setOnClickListener(this);

		Button createMark = (Button) findViewById(R.id.createMark);
		createMark.setOnClickListener(this);
	}

	ProgressDialog mProgress;
	String photo_path;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE:
				// 获取选中图片的路径
				photo_path = UriUtil.getPath(this, data.getData());

				mProgress = new ProgressDialog(MSZXingMainActivity.this);
				mProgress.setMessage("正在扫描...");
				mProgress.setCancelable(false);
				mProgress.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						final Result result = ZXingManager.scanImageFile(photo_path);
				
				
						MSZXingMainActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if (result != null) {
									String resultStr = result.getText();
									resultFile.setText(resultStr);
								} else {
									String resultStr = "Scan failed!";
									resultFile.setText(resultStr);
								}
								mProgress.dismiss();
							}
						});
			

					}
				}).start();

				break;

			case 10:
				String resultStr = data
						.getStringExtra(ZXingManager.EXTRA_ZXING_RESULT);
				result.setText(resultStr);
				break;

			}
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.scanFile:
				// 打开手机中的相册
				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
				innerIntent.setType("image/*");
				Intent wrapperIntent = Intent.createChooser(innerIntent,
						"选择二维码图片");
				MSZXingMainActivity.this.startActivityForResult(wrapperIntent,
						REQUEST_CODE);
				break;
			case R.id.scan:
				ZXingManager.getQRCodeResult(MSZXingMainActivity.this, 10);
				break;
			case R.id.scan2:
				ZXingManager.getQRCodeResultCustomer(MSZXingMainActivity.this, 10);
				break;
			case R.id.scan3:
				Intent it = new Intent(this, MSZXingCustomerActivity.class);
				startActivityForResult(it, 10);
				break;
			case R.id.create:
				mImageView.setImageBitmap(ZXingManager.createQRCodeBitmap(
						"www.baidu.com", 400, 400));
				break;

			case R.id.createMark:
				mImageView2.setImageBitmap(ZXingManager
						.createQRCodeBitmapWithMark(this, R.drawable.qr_logo,
								"www.baidu.com", 400));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
