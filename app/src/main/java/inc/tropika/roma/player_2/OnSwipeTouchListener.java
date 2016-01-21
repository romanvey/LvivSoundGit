package inc.tropika.roma.player_2;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
//Without logs
public class OnSwipeTouchListener implements View.OnTouchListener {
    public int w;
    public int h;
    final GestureDetector gestureDetector;






    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        w = size.x;
        h = size.y;
        Log.d("State", "Width: " + w + "; Height: " + h + ";");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {//тупо треба
        return false;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_VERT = 100;
        private static final int SWIPE_HOR = 200;
        private static final int VEL_VERT = 100;
        private static final int VEL_HOR = 200;
        private static final int MISTAKE = 300;



        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(e.getX()>w/2-MISTAKE&&e.getX()<w/2+MISTAKE&&e.getY()<h/2+MISTAKE&&e.getY()>h/2-MISTAKE) {
                onTouchCenter();
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {//тупо треба
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                Log.d("State","VelocityX: "+Math.abs(velocityX)+";VelocityY: "+Math.abs(velocityY)+";");
                Log.d("State","DifferenceX: "+diffX+";DifferenceY: "+diffY+";");
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_HOR && Math.abs(velocityX) > VEL_HOR) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_VERT && Math.abs(velocityY) > VEL_VERT) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public void onTouchCenter() {
    }

}