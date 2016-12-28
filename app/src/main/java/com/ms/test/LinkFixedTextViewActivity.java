package com.ms.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.R;

import ms.view.LinkFixedTextView;

public class LinkFixedTextViewActivity extends AppCompatActivity {

    Activity mActivity = this;
    String TAG = "LinkFixedTextView2Activity";

    TextView tv00,tv01;
    LinkFixedTextView tv1, tv2;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_fixed_text_view);
        tv00 = (TextView) this.findViewById(R.id.tv_0_0);
        tv01 = (TextView) this.findViewById(R.id.tv_0_1);
        tv1 = (LinkFixedTextView) this.findViewById(R.id.tv_1_1);
        tv2 = (LinkFixedTextView) this.findViewById(R.id.tv_1_2);

        tv1.setOnLinkFixedTextViewListener(linkClickListener);
        tv2.setOnLinkFixedTextViewListener(linkClickListener);

        tv00.setOnClickListener(clickListener);
        tv01.setOnClickListener(clickListener);
        tv1.setOnClickListener(clickListener);
        tv2.setOnClickListener(clickListener);
        tv00.setOnLongClickListener(longClickListener);
        tv01.setOnLongClickListener(longClickListener);
        tv1.setOnLongClickListener(longClickListener);
        tv2.setOnLongClickListener(longClickListener);

        mListView = (ListView) this.findViewById(R.id.lv);
        mListView.setAdapter(new MyAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    count++;
                    Toast.makeText(mActivity, view.hashCode() + ":setOnItemClickListener", Toast.LENGTH_LONG)
                            .show();
                    Log.d(TAG, "count:" + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    count++;
                    Toast.makeText(mActivity, view.hashCode() + ":setOnItemLongClickListener", Toast.LENGTH_LONG)
                            .show();
                    Log.d(TAG, "count:" + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    int count;
    private LinkFixedTextView.OnLinkFixedTextViewListener linkClickListener = new LinkFixedTextView.OnLinkFixedTextViewListener() {

        @Override
        public void onLinkFixedURLSpanClick(LinkFixedTextView sender, URLSpan clickedSpan) {
            try {
                count++;
                Toast.makeText(mActivity, sender.hashCode() + ":" + clickedSpan.getURL(), Toast.LENGTH_LONG)
                        .show();
                Log.d(TAG, "count:" + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                count++;
                Toast.makeText(mActivity, v.hashCode() + ":OnClickListener", Toast.LENGTH_LONG).show();
                ;
                Log.d(TAG, "count:" + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            try {
                count++;
                Toast.makeText(mActivity, v.hashCode() + ":OnLongClickListener", Toast.LENGTH_LONG).show();
                ;
                Log.d(TAG, "count:" + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if (convertView == null) {
                String linkText = "这是一串包含链接的文本:测试TextView中包含链接http://www.baidu.com,点击,http://www.tmall.com,点击非链接的控http://www.jd.com,件本身事件";
                String normalText = "这是一串普通文本:测试修应链接的点击点击非链接的时候响应控件本身事件";
                String linkText2 = "http://www.baidu.com";

                if (position == 0) {
                    textView = new LinkFixedTextView(mActivity);
                    ((LinkFixedTextView) textView).setOnLinkFixedTextViewListener(linkClickListener);
//                    ((LinkFixedTextView2) textView).setAttachToAdapterView();
                    textView.setOnClickListener(clickListener);
                    textView.setOnLongClickListener(longClickListener);
                    textView.setAutoLinkMask(Linkify.WEB_URLS);
                    textView.setText("设置了控件自身的click longclick" + linkText);
                    ((LinkFixedTextView) textView).setAttachToAdapterView();
                } else if (position == 1) {
                    textView = new LinkFixedTextView(mActivity);
                    ((LinkFixedTextView) textView).setOnLinkFixedTextViewListener(linkClickListener);
//                    ((LinkFixedTextView2) textView).setAttachToAdapterView();
                    textView.setAutoLinkMask(Linkify.WEB_URLS);
                    textView.setText(linkText);
                    ((LinkFixedTextView) textView).setAttachToAdapterView();
                } else {
                    textView = new LinkFixedTextView(mActivity);
                    ((LinkFixedTextView) textView).setOnLinkFixedTextViewListener(linkClickListener);

                    textView.setAutoLinkMask(Linkify.WEB_URLS);
                    textView.setOnClickListener(clickListener);
                    textView.setOnLongClickListener(longClickListener);
                    textView.setText("http://www.baidu.com");
                    ((LinkFixedTextView) textView).setAttachToAdapterView();
                }

            } else {
                textView = (TextView) convertView;
            }

            return textView;
        }


        //        android:descendantFocusability=""
        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }
}
