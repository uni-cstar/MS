package com.ms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ms.test.mszxing.MSZXingMainActivity;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    ArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lstV = (ListView) this.findViewById(R.id.lstV);
        lstV.setOnItemClickListener(this);

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getDataArray());
        lstV.setAdapter(mAdapter);
    }

    public String[] getDataArray() {
        return new String[]{
                "二维码"
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String menu = (String) mAdapter.getItem(position);
        if("二维码".equals(menu)){
            Intent it = new Intent(this, MSZXingMainActivity.class);
            startActivity(it);
        }
    }
}
