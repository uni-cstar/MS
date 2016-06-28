package ms.frame.network;

/**
 * Created by SupLuo on 2016/6/28.
 * the interface for download process listener
 */
public interface MSProgressResponseListener {
    /**
     * update process ,run in sub thread
     * @param progress the current process
     * @param total the total
     * @param finish isFinish
     */
    void onMsResponseProgress(long progress, long total, boolean finish);

}
