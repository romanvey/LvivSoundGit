package inc.tropika.roma.player_2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class ScreenReceiver extends android.content.BroadcastReceiver {
static int count=0;
    @Override
    public void onReceive(Context ctx, Intent intent) {

        MainActivity.settings.moveToFirst();
        if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON))&&MainActivity.settings.getInt(3)==1) {
            count++;
            final Handler handler = new Handler();
            Runnable r1=new Runnable(){

                @Override
                public void run() {
                    count=0;
                }
            };
            if(count==1) {
                handler.postDelayed(r1, 3000);
            }
            if(count>1){
                count=0;
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    if(MainActivity.IS_PLAYING){
                    MainActivity.server.musicCompleted();
                    }else{
                        MainActivity.server.musicCompleted();
                        MainActivity.server.pauseSong();
                    }
                }


            }
        }
    }
}