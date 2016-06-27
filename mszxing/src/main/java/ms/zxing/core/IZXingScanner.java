package ms.zxing.core;

import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

public interface IZXingScanner {

	Handler getHandler();
	
	Activity getActivity();
	
	void handleDecode(Result result, Bitmap barcode);
	
	void reStart();
	
	ResultPointCallback getResultCallBack();
}

