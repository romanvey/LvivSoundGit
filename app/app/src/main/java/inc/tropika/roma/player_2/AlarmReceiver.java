package inc.tropika.roma.player_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends android.content.BroadcastReceiver {

    public void onReceive(Context ctx, Intent intent) {
        Log.d("State","Action: "+intent.getAction());
        if (intent.getAction().equals(MainActivity.ACTION_ALARM)) {
           if(MainActivity.IS_INIT){
               MainActivity.server.stopSong();
               MainActivity.server.un_init();
               MainActivity.radioTmp.stopRadio();
           }
            MainActivity.IS_ALARM_CREATED=false;
        }
    }
}