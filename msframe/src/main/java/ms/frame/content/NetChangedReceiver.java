package ms.frame.content;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;

import ms.tool.DeviceUtil;

/**
 * 网络改变广播监听器
 */
public class NetChangedReceiver extends BroadcastReceiver {

    public static final int NetLost = 404;
    public static final int NetConnect = 100;

    Handler mHandler;

    public NetChangedReceiver(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 不是网络状态变化的不做处理
        if (!(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)))
            return;

        if (this.mHandler == null)
            return;

        int netType = DeviceUtil.getNetWorkType(context);
        Message msg = null;
        if (netType == DeviceUtil.NETWORK_TYPE_NONE) {
            msg = this.mHandler.obtainMessage(NetLost, netType);

        } else {
            msg = this.mHandler.obtainMessage(NetConnect, netType);
        }
        //移除原有的消息
        this.mHandler.removeMessages(NetConnect);
        this.mHandler.removeMessages(NetLost);
        //延迟发送消息
        this.mHandler.sendMessageDelayed(msg, 500);
    }


    /**
     * 注册广播
     *
     * @param activity
     * @param handler
     * @return
     */
    public static NetChangedReceiver registerReceiver(Activity activity, Handler handler) {
        NetChangedReceiver netReceiver = new NetChangedReceiver(handler);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(netReceiver, mFilter);
        return netReceiver;
    }

    /**
     * 解除广播
     *
     * @param activity
     * @param receiver
     */
    public static void unregisterReceiver(Activity activity, BroadcastReceiver receiver) {
        activity.unregisterReceiver(receiver);
    }
}
