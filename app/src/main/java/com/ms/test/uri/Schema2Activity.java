package com.ms.test.uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ms.R;

public class Schema2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schema2);
        Log.d("SchemaTest",this.getLocalClassName());
    }
}
