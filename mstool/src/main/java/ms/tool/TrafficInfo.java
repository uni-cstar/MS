package ms.tool;

import android.content.Context;
import android.net.TrafficStats;
import android.util.Log;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lucio on 17/2/22.
 */

public class TrafficInfo {

    private static final String TAG = "TrafficInfo";
    //上一次采取数据的大小
    private long preRxBytes = 0, preTxBytes = 0;
    //1秒采取一次数据
    final long scheduleTime = 1000;
    private Timer mRxTimer = null;
    private long uid;//0表示获取所有，>0 表示获取对应应用的流量速度
    TrafficInfoListener1 mRxLister;

    public TrafficInfo() {
        this(0);
    }

    public TrafficInfo(long uid) {
        this.uid = Math.max(0, uid);
    }

    /**
     * 获取context的程序uid
     *
     * @param context
     * @return
     */
    public static long getApplicationUid(Context context) {
        return context.getApplicationInfo().uid;
    }

    private long getRxBytes() {
        return uid <= 0 ? TrafficStats.getTotalRxBytes() : TrafficStats.getUidRxBytes((int) uid);
    }

    private long getTxBytes() {
        return uid <= 0 ? TrafficStats.getTotalTxBytes() : TrafficStats.getUidTxBytes((int) uid);
    }

    /**
     * 获取当前下载网速（b）
     * （0.1b/s）
     *
     * @return
     */
    public double getRxNetSpeed() {
        long result = 0;
        long total = getRxBytes();
        Log.d(TAG, "采取当前数据:total=" + total);
        //如果上一次采取数据的时间超过了3s，则让当前时间
        if (preRxBytes == 0) {
            preRxBytes = total;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            total = getRxBytes();
            Log.d(TAG, "采取当前数据(2次采取):total=" + total);
            result = (total - preRxBytes) * 5;
        } else {
            result = total - preRxBytes;
        }
        preRxBytes = total;
        Log.d(TAG, "当前速度大小：result" + result);
        return result;
//        double kb = (double) result / (double) 1024;
//        BigDecimal bd = new BigDecimal(kb);
//        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getNetSpeedFormatString(double speed) {

        if(speed < 102){
            return String.format("%.1fB/s",speed);
        }else{
            double kb = speed / (double) 1024;
            Log.d(TAG, "KB：" + kb);
            if (kb > 1024) {
                double mb = kb / (double) 1024;
                BigDecimal bd = new BigDecimal(mb);
                kb = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                return String.valueOf(kb) + "M/s";
            } else {
                BigDecimal bd = new BigDecimal(kb);
                kb = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                return String.valueOf(kb) + "K/s";
            }
        }
    }

    public void resetRxPreByte() {
        preRxBytes = 0;
    }

    public void resetTxPreByte() {
        preTxBytes = 0;
    }

    public double getTxNetSpeed() {
        long result = 0;
        long total = getTxBytes();
        Log.d(TAG, "采取当前数据:total=" + total);
        //如果上一次采取数据的时间超过了3s，则让当前时间
        if (preTxBytes == 0) {
            preTxBytes = total;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            total = getTxBytes();
            Log.d(TAG, "采取当前数据(2次采取):total=" + total);
            result = (total - preTxBytes) * 5;
        } else {
            result = total - preTxBytes;
        }
        preTxBytes = total;
        Log.d(TAG, "当前速度大小：result" + result);

        double kb = (double) result / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void startRxNetSpeedTask(TrafficInfoListener1 listener) {
        stopRxNetSpeedTask();
        mRxLister = listener;
        mRxTimer = new Timer();
        mRxTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    double speed = getRxNetSpeed();
                    if (mRxLister != null) {
                        mRxLister.onTrafficRxNetSpeedBack(speed);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500, scheduleTime); // 每秒更新一次
    }

    public void stopRxNetSpeedTask() {
        if (mRxTimer != null) {
            mRxTimer.cancel();
            mRxTimer = null;
        }
    }


    public interface TrafficInfoListener1 {
        void onTrafficRxNetSpeedBack(double speed);
    }

    public interface TrafficInfoListener2 {
        void onTrafficTxNetSpeedBack(double speed);
    }

}
