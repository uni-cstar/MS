package ms.frame.network;

/**
 * Created by SupLuo on 2016/6/28.
 * the interface for upload process listener
 */
public interface MSProgressRequestListener {
    /**
     * update process ,run in sub thread
     * @param progress the current process
     * @param total the total
     * @param finish isFinish
     */
    void onMsRequestProgress(long progress, long total, boolean finish);

}
