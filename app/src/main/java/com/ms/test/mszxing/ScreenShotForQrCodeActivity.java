package com.ms.test.mszxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.ms.R;

import java.util.Hashtable;

import ms.zxing.ZXingManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ScreenShotForQrCodeActivity extends AppCompatActivity {

    WebView mWebView;
    ImageView mImageView;
    TextView mTextView;
    Activity mActivity = ScreenShotForQrCodeActivity.this;
    float downX, downY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot_for_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTextView = (TextView) this.findViewById(R.id.tv);
        mImageView = (ImageView) this.findViewById(R.id.iv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });
        mWebView = (WebView) this.findViewById(R.id.wv);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebView.setWebViewClient(wvc);
        mWebView.loadUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTI0NjQ0MA==&mid=2655812030&idx=1&sn=7f4bd1a0edae4aa8ce25d2682a4113bd&scene=23&srcid=0630LrnOWtDYSfAJ6O4KSF4b#rd");
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mActivity.getWindow().getDecorView().setDrawingCacheEnabled(true);
                Bitmap bmp = getWindow().getDecorView().getDrawingCache();

                Observable.just(bmp)
                        .flatMap(new Func1<Bitmap, Observable<Bitmap>>() {
                            @Override
                            public Observable<Bitmap> call(Bitmap bitmap) {
                                int screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
                                int screenHeight = mActivity.getResources().getDisplayMetrics().heightPixels;
                                boolean isPortrait = screenWidth < screenHeight;
                                //截取截屏期望宽度
                                int width = Math.min(screenWidth, screenHeight);

                                int startY;
                                int endY;

                                if (isPortrait) {
                                    startY = (int) (downY - width / 2.0);
                                    endY = (int) (downY + width / 2.0);
                                    if (endY > screenHeight) { //如果需求的Y坐标超过了屏幕的高度，则以屏幕高度为Y轴基准
                                        startY = Math.max(0, screenHeight - width);
                                        endY = screenHeight;
                                    } else {
                                        if (startY < 0) {
                                            startY = 0;
                                        }
                                    }
                                } else {//如果是横屏，则直接截取屏幕高度
                                    startY = 0;
                                    endY = width;
                                }

                                //裁剪的图片，x轴方向永远跟手机保持一致
                                Bitmap result = Bitmap.createBitmap(bitmap, 0, startY, screenWidth, endY - startY);
                                return Observable.just(result);
                            }
                        }).flatMap(new Func1<Bitmap, Observable<TestResult>>() {
                    @Override
                    public Observable<TestResult> call(Bitmap bitmap) {
                        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
                        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码
                        Result scanResult = ZXingManager.scanBitmap(bitmap, hints, false);
                        TestResult result = new TestResult();
                        result.bmp = bitmap;
                        result.qrString = scanResult == null ? "没有二维码" : "二维码结果：" + scanResult.getText();
                        return Observable.just(result);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<TestResult>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(mTextView, e.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onNext(TestResult testResult) {
                                mImageView.setVisibility(View.VISIBLE);
                                mWebView.setVisibility(View.GONE);
                                mImageView.setImageBitmap(testResult.bmp);
                                mTextView.setText(testResult.qrString);
                            }
                        });


                return true;
            }
        });

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                downX = event.getX();
                downY = event.getY();

                return false;
            }
        });
    }

    private static class TestResult {
        public Bitmap bmp;
        public String qrString;
    }

    private WebViewClient wvc = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
}
