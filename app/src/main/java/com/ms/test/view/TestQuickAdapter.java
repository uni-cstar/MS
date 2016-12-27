package com.ms.test.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ms.R;

import java.util.List;

import ms.widget.QuickAdapter;

/**
 * Created by Lucio on 16/12/5.
 */
public class TestQuickAdapter extends QuickAdapter<String, TestViewHolder> {

    public TestQuickAdapter(Context context) {
        super(context);
    }

    public TestQuickAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new TestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_test_quick_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.bindValue(getItem(position));
    }
}
