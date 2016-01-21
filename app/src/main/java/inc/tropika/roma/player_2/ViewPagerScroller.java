package inc.tropika.roma.player_2;

import android.content.Context;
import android.util.Log;
import android.widget.Scroller;

public class ViewPagerScroller extends Scroller {



    public static int mScrollDuration = 300;
    public ViewPagerScroller(Context context) {
        super(context);
        Log.d("State", "ViewPageScroller constructor");
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
        Log.d("State", "ViewPageScroller: startScroll(1)");
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
        Log.d("State", "ViewPageScroller: startScroll(2)");
    }


}
