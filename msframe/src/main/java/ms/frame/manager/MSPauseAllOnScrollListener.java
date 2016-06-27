package ms.frame.manager;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.bumptech.glide.RequestManager;

/**
 * 用于在列表快速滑动过程中暂停图片请求
 * @ClassName: MSPauseOnScrollListener 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 罗超
 * @date 2016-4-20 上午11:33:16
 */
public class MSPauseAllOnScrollListener implements OnScrollListener {

	private RequestManager requestManager;
	private final boolean pauseOnScroll;
	private final boolean pauseOnFling;
	private final OnScrollListener externalListener;

	/**
	 * Constructor
	 * 
	 * @param requestManager
	 *            {@linkplain RequestManager} instance for controlling
	 * @param pauseOnScroll
	 *            Whether {@linkplain RequestManager#pauseRequests() pause RequestManager}
	 *            during touch scrolling
	 *            在滑动过程中是否暂停请求
	 * @param pauseOnFling
	 *            Whether {@linkplain RequestManager#pauseRequests() pause RequestManager}
	 *            during fling
	 *            在快速滑动过程中是否暂停请求
	 */
	public MSPauseAllOnScrollListener(RequestManager requestManager,
			boolean pauseOnScroll, boolean pauseOnFling) {
		this(requestManager, pauseOnScroll, pauseOnFling, null);
	}

	/**
	 * Constructor
	 * 
	 * @param requestManager
	 *            {@linkplain RequestManager} instance for controlling
	 * @param pauseOnScroll
	 *            Whether {@linkplain RequestManager#pauseRequests() pause RequestManager}
	 *            during touch scrolling
	 *            在滑动过程中是否暂停请求
	 * @param pauseOnFling
	 *            Whether {@linkplain RequestManager#pauseRequests() pause RequestManager}
	 *            during fling
	 *            在快速滑动过程中是否暂停请求
	 * @param customListener
	 *            Your custom {@link OnScrollListener} for
	 *            {@linkplain AbsListView list view} which also will be get
	 *            scroll events
	 *            自定义滑动监听
	 */
	public MSPauseAllOnScrollListener(RequestManager requestManager,
			boolean pauseOnScroll, boolean pauseOnFling,
			OnScrollListener customListener) {
		this.requestManager = requestManager;
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
		externalListener = customListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			requestManager.resumeRequestsRecursive();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			if (pauseOnScroll) {
				requestManager.pauseRequestsRecursive();
			}
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			if (pauseOnFling) {
				requestManager.pauseRequestsRecursive();
			}
			break;
		}
		if (externalListener != null) {
			externalListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (externalListener != null) {
			externalListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}
}
