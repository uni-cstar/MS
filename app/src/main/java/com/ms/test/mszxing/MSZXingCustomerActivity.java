package com.ms.test.mszxing;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.ms.R;

import ms.zxing.ZXingCustomerScannerActivity;

public class MSZXingCustomerActivity extends ZXingCustomerScannerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	@Override
	protected void createNavigationLayout(ViewGroup navGroup) {
		View view = this.getLayoutInflater().inflate(R.layout.layout_test_zxing_nav, null);
		navGroup.addView(view);
	}

	@Override
	protected void handleResult(String result) {
		super.handleResult(result);
	}

}
