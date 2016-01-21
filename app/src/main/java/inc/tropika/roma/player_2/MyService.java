package inc.tropika.roma.player_2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service{
    MyBinder binder = new MyBinder();
    private MediaPlayer player;
    Utils utils;
    public static BassBoost bass;
    AudioManager audioManager;
    AFListener afListenerMusic;
    private Handler durationHandler = new Handler();


    public MyService() {
        Log.d("State", "MyService constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("State", "MyService: onCreate()");
        utils=new Utils(getApplicationContext());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            initMusicPlayer();
    }




    public void initMusicPlayer(){
        Log.d("State", "MyService: initMusicPlayer()");
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MainActivity.SESSION=player.getAudioSessionId();
        Log.d("State","Session: "+player.getAudioSessionId());
        bass=new BassBoost(0,MainActivity.SESSION);
        bass.setEnabled(true);

        MainActivity.settings.moveToFirst();
        if(MainActivity.settings.getInt(4)==1) {
            bass.setStrength((short)1000);
        }else{
            bass.setStrength((short)0);
        }
        if(MainActivity.IS_FIRST_TIME_BY_SESSION_INIT) {
            afListenerMusic = new AFListener();
            int requestResult = audioManager.requestAudioFocus(afListenerMusic, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            Log.d("State", "Music request focus, result: " + requestResult);
            MainActivity.IS_FIRST_TIME_BY_SESSION_INIT=false;
        }else{
            Player.setupEqualizer();
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicCompleted();
            }
        });
    }





public void musicCompleted(){
    Log.d("State","MyService: MusicCompleted()");
    Log.d("State","New List: "+MainActivity.IS_NEW_LIST+"; Position: "+MainActivity.pos+";");
    if(MainActivity.IS_REPEAT){
        playSong();
    }
    int rand;
MainActivity.settings.moveToFirst();
    if(MainActivity.cursor_numb==1){
        if(MainActivity.IS_SHUFFLE){
            rand=utils.randInt(0,MainActivity.all.getCount()-1);
            MainActivity.pos=rand;
            playSong();
            return;
        }else{
            if(MainActivity.pos==MainActivity.all.getCount()-1){
                MainActivity.pos=0;
                playSong();
                return;
            }else{
                MainActivity.pos++;
                playSong();
                return;
            }
        }
    }
if(MainActivity.IS_NEW_LIST){
    if(MainActivity.cursor.getCount()==0){
        un_init();
        stopSong();
        return;
    }
        if(MainActivity.IS_SHUFFLE){
            if(MainActivity.cursor.getCount()==0){
                un_init();
                stopSong();
                return;
            }else{
                rand=utils.randInt(0,MainActivity.cursor.getCount()-1);
                MainActivity.pos=rand;
                playSong();
                return;
            }
        }else {
            MainActivity.pos=0;
            playSong();
            return;
        }


}
if(MainActivity.IS_SHUFFLE){
    rand=utils.randInt(0,MainActivity.cursor.getCount()-1);
    MainActivity.pos=rand;
    playSong();
    return;
}
   if(MainActivity.pos==MainActivity.cursor.getCount()-1){
           MainActivity.pos=0;
           playSong();
           return;
   }
    MainActivity.pos++;
    playSong();
}

public void setLooping(){
    Log.d("State","MyService: setLooping()");
    player.setLooping(MainActivity.IS_REPEAT);
}


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("State","MyService: onDestroy()");
        Log.d("State","Losing focus");
        audioManager.abandonAudioFocus(afListenerMusic);
    }

    public boolean onUnbind(Intent intent) {
        Log.d("State", "MyService: onUnbind()");
        return true;
    }

    public void stopSong(){
        Log.d("State", "MyService: stopSong()");
            Player.video.pause();
            MainActivity.IS_INIT=false;
            MainActivity.IS_PAUSED_BY_SENSOR=false;
            player.stop();
            player.release();

    }


public void pauseSong(){
    Log.d("State","MyService: pauseSong()");
    if(player.isPlaying()&&MainActivity.IS_PLAYING){
        player.pause();
        Log.d("State", "MyService song paused");
        Player.video.pause();
        MainActivity.IS_PLAYING=false;
        Player.play.setImageDrawable(getResources().getDrawable(R.drawable.play));
        Player.play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
        if(MainActivity.IS_HIDDEN){
            MainActivity.customNotification(MainActivity.ctx);
        }
    }
}

public void resumeSong(){
    Log.d("State","MyService: resumeSong()");
    if(!MainActivity.IS_PLAYING&&MainActivity.IS_INIT){
        player.start();
        Log.d("State","MyService song resumed");
        MainActivity.IS_PLAYING=true;
        if(!MainActivity.IS_HIDDEN&&Player.video!=null){
            Player.video.start();
        }
        MainActivity.IS_PAUSED_BY_SENSOR=false;
        durationHandler.postDelayed(updateSeekBarTime, 1000);
        Player.play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        Player.play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
        if(MainActivity.IS_HIDDEN){
            MainActivity.customNotification(MainActivity.ctx);
        }
    }
}


    public void playSong(){
        Log.d("State","MyService: playSong()");
        player.reset();
        try{
            if(MainActivity.cursor_numb==2) {
                MainActivity.cursor.moveToPosition(MainActivity.pos);
                player.setDataSource(MainActivity.cursor.getString(MainActivity.PATHS_INT));
                Log.d("State", String.format("NO Error setting data source %s", MainActivity.cursor.getString(MainActivity.PATHS_INT)));
            }
            if(MainActivity.cursor_numb==1){
                MainActivity.all.moveToPosition(MainActivity.pos);
                player.setDataSource(MainActivity.all.getString(MainActivity.PATHS_INT));
                Log.d("State", String.format("NO Error setting data source %s", MainActivity.all.getString(MainActivity.PATHS_INT)));
            }
        }
        catch(Exception e){
            Log.d("State", String.format("Error setting data source %s", e.toString()));
        }
        try {
            MainActivity.STOPPED_BY_AUDIO_FOCUS=false;
            player.prepare();
            player.start();
            player.setVolume(MainActivity.VOLUME/100,MainActivity.VOLUME/100);
            if(MainActivity.cursor_numb==2) {
                MainActivity.cursor.moveToPosition(MainActivity.pos);
                MainActivity.id = MainActivity.cursor.getInt(MainActivity.IDS_INT);
            }
            if(MainActivity.cursor_numb==1){
                MainActivity.all.moveToPosition(MainActivity.pos);
                MainActivity.id = MainActivity.all.getInt(MainActivity.IDS_INT);
            }
            MainActivity.IS_PLAYING = true;
            MainActivity.IS_INIT=true;
            MainActivity.IS_PAUSED_BY_SENSOR=false;
            MainActivity.IS_NEW_LIST=false;
            MainActivity.IS_FIRST_TIME_BY_SESSION_PLAY=false;
            if(!MainActivity.IS_HIDDEN&&Player.video!=null){
                Player.video.start();
            }
            if(MainActivity.IS_HIDDEN){
                    MainActivity.customNotification(MainActivity.ctx);
            }
            playerLayout();
            Log.d("State", "Start playing");
        } catch (IOException e) {
            Log.d("State", "Error prepare "+e.toString());
        }

    }

    public void setVolume(){
        Log.d("State","MyService: setVolume()");
        Log.d("State","VOLUME set "+MainActivity.VOLUME);
        float log1=(float)(Math.log(10-MainActivity.VOLUME/10)/Math.log(10));
        player.setVolume(1-log1,1-log1);
    }

    public int getDuration(){
       return player.getDuration();
    }
public  void playerLayout(){
    Log.d("State","MyService: playerLayout()");
    if(MainActivity.cursor_numb==2){
        MainActivity.cursor.moveToPosition(MainActivity.pos);
        Player.title.setText(MainActivity.cursor.getString(MainActivity.TITLES_INT));
    }
    if(MainActivity.cursor_numb==1){
        MainActivity.all.moveToPosition(MainActivity.pos);
        Player.title.setText(MainActivity.all.getString(MainActivity.TITLES_INT));
    }
    Player.play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    Player.play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    MainActivity.duration=player.getDuration();
    Player.duration.setMax(MainActivity.duration);
    Player.allTime.setText(utils.milliSecondsToTimer(player.getDuration()));
    durationHandler.postDelayed(updateSeekBarTime, 1000);
    int progress=0;
    if(MainActivity.cursor_numb==2){
        MainActivity.cursor.moveToPosition(MainActivity.pos);
    progress=MainActivity.cursor.getInt(MainActivity.MOOD_INT);
    }
    if(MainActivity.cursor_numb==1){
        MainActivity.all.moveToPosition(MainActivity.pos);
    progress=MainActivity.all.getInt(MainActivity.MOOD_INT);
    }


    if(progress==101){
        Player.mood.setProgress(0);
        Player.mood_t.setText("Настрій  не задано");
    }else{
        Player.mood_t.setText("Настрій: "+progress);
        progress++;
        Player.mood.setProgress(progress);

    }

}


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("State", "MyService: onBind()");
        return binder;
    }




    class MyBinder extends Binder{
        MyService getService(){
            Log.d("State", "MyBinder: getService()");
            return MyService.this;
        }
    }


    public void un_init(){
        Log.d("State", "MyService: un_init()");
        if(MainActivity.notification!=null){
            MainActivity.mNotificationManager.cancel(MainActivity.NOTIF_ID);
            MainActivity.notification=null;
        }
       Player.title.setText("");
        Player.currTime.setText("00:00");
        Player.allTime.setText("00:00");
        Player.mood_t.setText("Настрій не задано");
        Player.mood.setProgress(0);
        Player.duration.setProgress(0);
        MainActivity.IS_INIT=false;
        Player.play.setImageDrawable(getResources().getDrawable(R.drawable.play));
        Player.play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    }




    public void prev(){
        Log.d("State","MyService: prev()");
        Log.d("State","New List: "+MainActivity.IS_NEW_LIST+"; Position: "+MainActivity.pos+";");
        if(MainActivity.IS_REPEAT){
            playSong();
        }
        int rand;
        MainActivity.settings.moveToFirst();
        if(MainActivity.cursor_numb==1){
            if(MainActivity.IS_SHUFFLE){
                rand=utils.randInt(0,MainActivity.all.getCount()-1);
                MainActivity.pos=rand;
                playSong();
                return;
            }else{
                if(MainActivity.pos==0){
                    MainActivity.pos=MainActivity.all.getCount()-1;
                    playSong();
                    return;
                }else{
                    MainActivity.pos--;
                    playSong();
                    return;
                }
            }
        }
        if(MainActivity.IS_NEW_LIST){
            if(MainActivity.cursor.getCount()==0){
                stopSong();
                un_init();
                return;
            }
            if(MainActivity.IS_SHUFFLE){
                if(MainActivity.cursor.getCount()==0){
                    un_init();
                    stopSong();
                    return;
                }else{
                    rand=utils.randInt(0,MainActivity.cursor.getCount()-1);
                    MainActivity.pos=rand;
                    playSong();
                    return;
                }
            }else {
                MainActivity.pos=0;
                playSong();
                return;
            }


        }
        if(MainActivity.IS_SHUFFLE){
            rand=utils.randInt(0,MainActivity.cursor.getCount()-1);
            MainActivity.pos=rand;
            playSong();
            return;
        }
        if(MainActivity.pos==0){
                MainActivity.pos=MainActivity.cursor.getCount()-1;
                playSong();
                return;
        }
        MainActivity.pos--;
        playSong();
    }




    class AFListener implements AudioManager.OnAudioFocusChangeListener {

        public AFListener() {
            Log.d("State","AFListener constructor");
        }

        public void onAudioFocusChange(int focusChange) {
            Log.d("State","AFListener: onAudioFocusChange()");
            String event;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    event = "AUDIOFOCUS_LOSS";
                    pauseSong();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    event = "AUDIOFOCUS_LOSS_TRANSIENT";
                    MainActivity.STOPPED_BY_AUDIO_FOCUS=true;
                    pauseSong();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    player.setVolume(MainActivity.VOLUME/400,MainActivity.VOLUME/400);
                    MainActivity.STOPPED_BY_AUDIO_FOCUS=true;
                    event = "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK";
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    event = "AUDIOFOCUS_GAIN";
                    if(MainActivity.STOPPED_BY_AUDIO_FOCUS){
                        resumeSong();
                        MainActivity.STOPPED_BY_AUDIO_FOCUS=false;
                    }
                    break;
                default:event = "NOT EXIST";
            }
            Log.d("State","FOCUS: " + event);
        }
    }
    public void seekTo(int time){
        Log.d("State","MyService: seekTo("+time+")");
        player.seekTo(time);
    }


    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            try {
                //Log.d("State","updateSeekBarTime()");//very often
                Player.duration.setProgress(player.getCurrentPosition());
                if(MainActivity.IS_PLAYING&&!MainActivity.IS_DESTROY) {
                    durationHandler.postDelayed(this, 1000);
                }
            } catch (Exception e) {
                Log.d("State","Error in DESTROY "+e.toString());
            }
        }
    };
}
