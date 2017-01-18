package com.ms.test.uri;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ms.R;

import java.util.List;

public class SchemaActivity extends AppCompatActivity {

    EditText et;
    Button btn;

    /*
    * 测试得出结论
    * 如果寻找 ms://test/path
    * 则能够匹配到 定义的data满足以下的界面 ms://test/path   ms://test   ms://
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SchemaTest", this.getLocalClassName());
        setContentView(R.layout.activity_schema);
        et = (EditText) this.findViewById(R.id.et);
        btn = (Button) this.findViewById(R.id.btn4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = et.getText().toString();
                if (TextUtils.isEmpty(uri))
                    return;
                Intent it = new Intent("ms.intent.action.VIEW", Uri.parse(uri));
                isUxIntentExisting(v.getContext(),it);
                startActivity(it);
            }
        });
    }

    public static boolean isUxIntentExisting(Context context, Intent it) {

        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(it,
                PackageManager.MATCH_DEFAULT_ONLY);
        Log.d("SchemaTest", it.toString() + "\n" + resolveInfo.size());
        if (resolveInfo.size() > 0) {
            return true;
        }

        return false;
    }

    public void onBtnClick(View v) {
        Button btn = (Button) v;
        String action = btn.getText().toString();
        Intent it = new Intent("ms.intent.action.VIEW", Uri.parse(action));
        isUxIntentExisting(this,it);
        startActivity(it);
    }
}
