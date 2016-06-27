package ms.frame.countdown;

/**
 * Created by luochao on 2015/10/19.
 */
public interface MSCountDownTimerListener {
	void onMSCDTimerCreate();

	void onMSCDTimerTick(long millisUntilFinished, long total, long interval);

	void onMSCDTimerFinish();
}
