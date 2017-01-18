package com.ms.test.uri;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ms.R;

public class Uri1Activity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SchemaTest",this.getLocalClassName());
        setContentView(R.layout.activity_uri1);

        textView = (TextView) this.findViewById(R.id.tv);
        Uri uri = this.getIntent().getData();
        StringBuilder sb = new StringBuilder();
        sb.append("Scheme:" + uri.getScheme() + "\n");
        sb.append("Host:" + uri.getHost() + "\n");
        sb.append("Path:" + uri.getPath() + "\n");
        sb.append("Authority:" + uri.getAuthority() + "\n");
        textView.setText(sb.toString());
    }
}
