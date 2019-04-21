package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MeuBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    private boolean mAllowUserDragging = true;

    public MeuBottomSheetBehavior() {
        super();
    }

    public MeuBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setmAllowUserDragging(boolean mAllowUserDragging) {
        this.mAllowUserDragging = mAllowUserDragging;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (!mAllowUserDragging) {
            return false;
        }
        return super.onInterceptTouchEvent(parent, child, event);
    }
}
