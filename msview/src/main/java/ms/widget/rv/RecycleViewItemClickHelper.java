package ms.widget.rv;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Lucio on 16/12/14.
 * recycleview item click listener
 * simple: recycleview.addOnItemTouchListener(this)
 */
public abstract class RecycleViewItemClickHelper extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;

    public RecycleViewItemClickHelper(final RecyclerView recyclerView) {
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
//                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                        if (childView != null && mItemClickListener != null) {
//                            mItemClickListener.onRecycleViewItemClick(childView, recyclerView.getChildAdapterPosition(childView));
//                        }
//                        return true;


                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null) {
//                            int position = recyclerView.getChildAdapterPosition(child);//recyclerView.indexOfChild(child);
                            onRecycleViewItemClick(recyclerView.getChildViewHolder(child));
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
//                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                        if (childView != null && mItemClickListener != null) {
//                            self.onRecycleViewItemLongClick(recyclerView.getChildViewHolder(childView), recyclerView.getChildAdapterPosition(childView));
//                        }

                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
//                            int position = recyclerView.getChildAdapterPosition(childView);// recyclerView.indexOfChild(child);
                            onRecycleViewItemLongClick(recyclerView.getChildViewHolder(childView));
                        }
                    }
                });
    }

    public abstract void onRecycleViewItemClick(RecyclerView.ViewHolder viewHolder);

    public abstract void onRecycleViewItemLongClick(RecyclerView.ViewHolder view);


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }
}
