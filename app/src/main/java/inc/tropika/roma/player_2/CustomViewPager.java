package inc.tropika.roma.player_2;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        Log.d("State", "CustomViewPager constructor");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("State", "CustomViewPager: OnTouchEvent()");//very often
        return this.enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //Log.d("State", "CustomViewPager: OnInterceptTouchEvent()");//very often
        return this.enabled && super.onInterceptTouchEvent(event);

    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
        Log.d("State", String.format("CustomViewPager: setPagingEnabled(%s)", enabled));
    }

}
