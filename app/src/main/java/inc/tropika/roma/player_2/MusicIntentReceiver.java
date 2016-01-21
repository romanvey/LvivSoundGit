package inc.tropika.roma.player_2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

public class MusicIntentReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {

        if (intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            Log.d("State", "MusicIntentReceiver: onReceive()");
            Log.d("State", "Music paused(ACTION_AUDIO_BECOMING_NOISY)");
            if (MainActivity.IS_INIT) {
                MainActivity.server.pauseSong();
            }
        }
    }








}
