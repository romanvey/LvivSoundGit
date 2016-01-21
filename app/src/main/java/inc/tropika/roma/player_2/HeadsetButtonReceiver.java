package inc.tropika.roma.player_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;

//Need test!
public class HeadsetButtonReceiver extends BroadcastReceiver{
    static int d = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event =  intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_HEADSETHOOK:
                if (action == KeyEvent.ACTION_DOWN) {
                    d++;
                    final Handler handler = new Handler();
                    final Runnable r2 = new Runnable(){

                        @Override
                        public void run() {
                        if(d==2){
                            if(MainActivity.IS_INIT) {
                                MainActivity.server.musicCompleted();
                            }
                        }else{
                            if(MainActivity.IS_INIT) {
                                MainActivity.server.prev();
                            }
                        }
                        d=0;
                        }
                    };
                    final Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            if(d == 1){
                               if(MainActivity.IS_PLAYING&&MainActivity.IS_INIT){
                                   MainActivity.server.pauseSong();
                               }
                               if(!MainActivity.IS_PLAYING&&MainActivity.IS_INIT){
                                   MainActivity.server.resumeSong();
                               }
                                d=0;
                            }
                            if(d ==2){
                                handler.postDelayed(r2,1000);
                            }
                        }
                    };

                    if (d == 1) {
                        handler.postDelayed(r, 1000);
                    }
                }break;
        }
    }
}
