package ms.frame.countdown;

import java.util.HashMap;

/**
 * Created by luochao on 2015/10/19.
 * 自定义倒计时管理器类，用于实现倒计时界面（不会因为界面消失而导致倒计时问题，适合用在短信认证地方）
 * 
 */
public class MSCountDownTimerManager implements MSCountDownFinishListener {

    private MSCountDownTimerManager() {
        myCdTimerHashMap = new HashMap<String, MSCountDownTimer>();
    }

    private static class Holder {
        private static MSCountDownTimerManager instance = new MSCountDownTimerManager();
    }

    public static MSCountDownTimerManager getInstance() {
        return Holder.instance;
    }


    private HashMap<String, MSCountDownTimer> myCdTimerHashMap;

    public boolean isContains(String key){
        return myCdTimerHashMap.containsKey(key);
    }

    /**
     * 开始倒计时
     * 如果对应key的计时器已经存在并正在运行，则复用之前的倒计时对象
     * @param total 倒计时时间
     * @param inteval 执行间隔时间
     * @param key 用于唯一指定定时器的key
     * @param listener 回调
     * @return
     */
    public MSCountDownTimer start(long total, long inteval, String key, MSCountDownTimerListener listener) {
    	MSCountDownTimer timer = null;
        if (myCdTimerHashMap.containsKey(key)) {//如果已包含倒计时
            timer = myCdTimerHashMap.get(key);
            if (timer.isFinish()) {//倒计时已结束
                timer.removeOnCountDownTimerListener();//移除监听
                myCdTimerHashMap.remove(key);//从map中移除

                timer = new MSCountDownTimer(total, inteval, key, this);
                listener.onMSCDTimerCreate();
                myCdTimerHashMap.put(key, timer);
                timer.start();
            }
            timer.setOnCountDownTimerListener(listener);
        } else {
            timer = new MSCountDownTimer(total, inteval, key, this);
            timer.setOnCountDownTimerListener(listener);
            listener.onMSCDTimerCreate();

            myCdTimerHashMap.put(key, timer);
            timer.start();
        }
        return timer;
    }
    
    /**
     * update by 2016.07.21
     * @param total
     * @param inteval
     * @param key
     * @param listener
     * @param restartIfExitsAndFinished 如果有相同的定时器，并且定时器已经结束，是否自动重启一个定时器
     * @return restartifExitsAndFinished == false,
     *          包含相同key的定时器，如果定时器未结束，则返回定时器对象并绑定回调，如果已结束则返回null
     *          不包含同key的定时器，则返回null
     */
    public MSCountDownTimer start(long total, long inteval, String key, MSCountDownTimerListener listener,boolean restartIfExitsAndFinished) {
    	MSCountDownTimer timer = null;
        if (myCdTimerHashMap.containsKey(key)) {//如果已包含倒计时
            timer = myCdTimerHashMap.get(key);
            if (timer.isFinish()) {//倒计时已结束
            	
            	if(!restartIfExitsAndFinished)//如果不重启，则
            		return null;
            	
                timer.removeOnCountDownTimerListener();//移除监听
                myCdTimerHashMap.remove(key);//从map中移除

                timer = new MSCountDownTimer(total, inteval, key, this);
                listener.onMSCDTimerCreate();
                myCdTimerHashMap.put(key, timer);
                timer.start();
            }
            timer.setOnCountDownTimerListener(listener);
        } else {
            if(restartIfExitsAndFinished){
                timer = new MSCountDownTimer(total, inteval, key, this);
                timer.setOnCountDownTimerListener(listener);
                listener.onMSCDTimerCreate();

                myCdTimerHashMap.put(key, timer);
                timer.start();
            }
        }
        return timer;
    }

    public void removeListener(String key,MSCountDownTimerListener listener){
        if (myCdTimerHashMap.containsKey(key)) {//如果已包含倒计时
        	MSCountDownTimer timer = myCdTimerHashMap.get(key);
            timer.removeOnCountDownTimerListener(listener);
        }
    }

    @Override
    public void onMSCDTimerFinish(String key, MSCountDownTimer timer) {
        timer.removeOnCountDownTimerListener();

        if (myCdTimerHashMap.containsKey(key)) {//如果已包含倒计时
            myCdTimerHashMap.remove(key);
        }
    }

}

