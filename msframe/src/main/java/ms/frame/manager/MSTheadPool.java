package ms.frame.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SupLuo on 2016/4/25.
 */
public class MSTheadPool {

    private ExecutorService singleExecutor;

    private ExecutorService multyExecutor;

    private MSTheadPool() {
    }

    private static class Holer {
        private static MSTheadPool instance = new MSTheadPool();
    }

    public static MSTheadPool getInstance() {
        return Holer.instance;
    }

    public void startSingle(Runnable runnable) {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }

        singleExecutor.execute(runnable);
    }

    public void start(Runnable runnable) {
        if (multyExecutor == null) {
            multyExecutor = Executors.newCachedThreadPool();
        }
        multyExecutor.execute(runnable);
    }
}
