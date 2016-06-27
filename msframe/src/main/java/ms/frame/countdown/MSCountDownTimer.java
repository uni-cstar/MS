package ms.frame.countdown;

import android.os.CountDownTimer;

/**
 * Created by luochao on 2015/10/19.
 * 自定义倒计时类
 */
public class MSCountDownTimer extends CountDownTimer {

    MSCountDownTimerListener mListener;
    MSCountDownFinishListener finishListener;

    long mTotal;
    long mInteval;
    String key;

    private boolean isFinish = false;

    /**
     * @param total   总时间
     * @param inteval 间隔执行时间
     */
    public MSCountDownTimer(long total, long inteval, String key, MSCountDownFinishListener finishListener) {
        super(total, inteval);
        this.key = key;
        this.mTotal = total;
        this.mInteval = inteval;
        this.finishListener = finishListener;
        isFinish = false;

    }

    public void setOnCountDownTimerListener(MSCountDownTimerListener listener) {
        this.mListener = listener;
    }

    public void removeOnCountDownTimerListener(MSCountDownTimerListener listener) {
        if(this.mListener == listener){
            this.mListener = null ;
        }
    }

    public void removeOnCountDownTimerListener() {
        this.mListener = null;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (this.mListener != null)
            this.mListener.onMSCDTimerTick(millisUntilFinished, mTotal,mInteval);
    }

    @Override
    public void onFinish() {
        isFinish = true;

        if (this.mListener != null)
            this.mListener.onMSCDTimerFinish();

        if (this.finishListener != null)
            this.finishListener.onMSCDTimerFinish(key, this);
    }

    public boolean isFinish() {
        return isFinish;
    }
}

