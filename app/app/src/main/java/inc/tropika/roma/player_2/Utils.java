package inc.tropika.roma.player_2;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.Random;

public class Utils {
    static int w,h;

    public Utils(Context ctx) {
        Log.d("State", "Utils constructor");
        WindowManager wm=(WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        w=size.x;
        h=size.y;
    }



    public String milliSecondsToTimer(long milliseconds){
        //Log.d("State", "Utils: milliSecondsToTimer()");//very often
        String finalTimerString = "";
        String secondsString;

        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);

        if(hours > 0){
            finalTimerString = hours + ":";
        }

        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;


        return finalTimerString;
    }
    public int randInt(int min, int max) {


        Random rand = new Random();


        return rand.nextInt((max - min) + 1) + min;
    }
}
