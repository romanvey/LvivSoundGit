package inc.tropika.roma.player_2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import inc.tropika.roma.player_2.MyService.MyBinder;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    static final int PAGE_COUNT=4;
    public static Cursor cursor=null;
    public static Cursor all=null;
    public static Cursor settings=null;
    public static int pos = -1;
    public static int id = -1;
    public static int STYLES;
    public static int cursor_numb;
    public static int duration = -1;
    final int CHECKING_TIME=2;
    public static float DISTANCE;
    public static MyService server;
    public static RelativeLayout child;
    public static boolean bound = false;
    public static ArrayList<String> ids = new ArrayList<>();
    public static final int IDS_INT=1;
    public static final int TITLES_INT=2;
    public static final int PATHS_INT=3;
    public static final int MOOD_INT=4;
    public static String last_where="SELECT * FROM "+DBHelper.TABLE;
    public static Context ctx;
    public static  HelpList hl;
    public static Setts st;

    public static final String FILE="back.mp4";

    public static final String ACTION_PREV="inc.tropika.roma.player_2.PREVIOUS";
    public static final String ACTION_PLAY="inc.tropika.roma.player_2.PLAY";
    public static final String ACTION_PAUSE="inc.tropika.roma.player_2.PAUSE";
    public static final String ACTION_CLOSE="inc.tropika.roma.player_2.CLOSE";
    public static final String ACTION_NEXT="inc.tropika.roma.player_2.NEXT";
    public static final String ACTION_ALARM="inc.tropika.roma.player_2.ALARM";

    public static float VOLUME=100f;

    public static final int NOTIF_ID=1;

    public static final String UNCATEGORY="uncategory";
    public static final String SEARCH="search";
    public static final String WHERE="where";
    public static final String IS_FIRST_TIME="firsttime";


    public static boolean IS_FIRST_TIME_BY_SESSION_INIT=true;
    public static boolean IS_FIRST_TIME_BY_SESSION_PLAY=true;
    public static boolean UPDATE_DB=false;
    public static boolean IS_SHUFFLE=false;
    public static boolean IS_REPEAT=false;
    public static boolean IS_PLAYING=false;
    public static boolean IS_INIT=false;
    public static boolean IS_HIDDEN=false;
    public static boolean IS_NEW_LIST=false;
    public static boolean IS_PAUSED_BY_SENSOR=false;
    public static boolean IS_DESTROY=false;
    public static boolean IS_ALARM_CREATED=false;
    public static boolean SENSOR_ALREADY_REGISTER=false;
    public static boolean STOPPED_BY_AUDIO_FOCUS=false;
    public static CustomViewPager pager;
    public static Typeface tf;
    public static SQLiteDatabase db;
    public static NotificationManager mNotificationManager;
    public static Toast toast;
    public static int SESSION;
    public static int PAGE=0;


    public static Notification notification=null;
    public static DBHelper dbHelper;
    public static MenuItem save,gest,hint,done,setts,sleep;
    public static FragmentTransaction fr;

    Handler sensorHandler = new Handler();
    SensorManager mySensorManager;
    Sensor myProximitySensor;
    PowerManager powermanager;
    PendingIntent startAlarmPI;
    AlarmManager alarm;
    PagerAdapter pagerAdapter;
    Intent i;
    int count=0;
    SharedPreferences prefs;
    BroadcastReceiver screenReceiver;
    SharedPreferences.Editor editor;
    ContentValues cv=new ContentValues();
    boolean need_orient;
    Handler or;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            need_orient=true;
        }else{
            need_orient=false;
        }
        ctx=this;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Log.d("State", "MainActivity: OnCreate()");

        toast=new Toast(this);
        ScreenReceiver.count=0;
        if(ids.size()==0) {
            init_db();//+ get audio if checked
        }

        settings.moveToFirst();
        STYLES=settings.getInt(5);
        if(settings.getInt(5)==1){
            tf = Typeface.createFromAsset(this.getAssets(), "DS_Moster.ttf");
        }else{
            tf = Typeface.createFromAsset(this.getAssets(), "AGGalleonC.otf");
        }
        settings.moveToFirst();
        getPager();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        screenReceiver = new ScreenReceiver();
        registerReceiver(screenReceiver, filter);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        child=(RelativeLayout)findViewById(R.id.child);
            or = new Handler();
            or.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(need_orient) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        need_orient=true;
                        or.postDelayed(this,500);
                    }
                }
            }, 500);
        //getDBInLog();//Need for testing
    }





    public static void createToast(String text){
        try {
            toast.cancel();
        } catch (Exception e) {
            Log.d("State","No toasts to cancel");
        }
        Log.d("State", "MainActivity: CreateToast()");
        toast=Toast.makeText(ctx,text,Toast.LENGTH_SHORT);
        toast.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.d("State", "MainActivity: OnCreateOptionsMenu()");
        inflater.inflate(R.menu.menu_main, menu);
        done=menu.findItem(R.id.action_done);
        sleep=menu.findItem(R.id.action_sleep);
        setts=menu.findItem(R.id.action_settings);
        save=menu.findItem(R.id.action_save);
        gest=menu.findItem(R.id.action_touch);
        hint=menu.findItem(R.id.action_hint);
        save.setVisible(false);
        gest.setVisible(false);
        done.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("State", "MainActivity: OnOptionsItemSelected("+item.getTitle()+")");
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_touch:
                viewing();
                return true;
            case R.id.action_save:
                saving();
                return true;
            case R.id.action_sleep:
                sleeping();
                return true;
            case R.id.action_hint:
                listStart();
                return true;
            case R.id.action_done:
                doneSetts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   public void doneSetts(){
       int tmp1,tmp2,tmp3,tmp4;
       if(Setts.f1.isChecked()){tmp1=1;}else{tmp1=0;}
       if(Setts.f2.isChecked()){tmp2=1;}else{tmp2=0;}
       if(Setts.f3.isChecked()){tmp3=1;}else{tmp3=0;}
       if(Setts.f4.isChecked()){tmp4=1;}else{tmp4=0;}
       cv.put(DBHelper.MISTAKE,Setts.mistake.getProgress());
       cv.put(DBHelper.F1,tmp1);
       cv.put(DBHelper.F2,tmp2);
       cv.put(DBHelper.F3,tmp3);
       cv.put(DBHelper.F4,tmp4);
       db.update(DBHelper.SETTINGS, cv, null, null);
       cv.clear();
       settings=db.query(DBHelper.SETTINGS,null,null,null,null,null,null);
       boosting();
       System.gc();
       style();
       if(PAGE==0){
           done.setVisible(false);
           hint.setVisible(true);
           setts.setVisible(true);
           sleep.setVisible(true);
           save.setVisible(false);
           gest.setVisible(false);
       }if(PAGE==1){
           done.setVisible(false);
           hint.setVisible(true);
           setts.setVisible(true);
           sleep.setVisible(true);
           save.setVisible(false);
           gest.setVisible(false);
       }if(PAGE==2){
           done.setVisible(false);
           hint.setVisible(true);
           setts.setVisible(true);
           sleep.setVisible(true);
           gest.setVisible(true);
           save.setVisible(true);
       }if(PAGE==3){
           done.setVisible(false);
           hint.setVisible(false);
           gest.setVisible(false);
           sleep.setVisible(true);
           setts.setVisible(true);
           save.setVisible(false);
       }
       int tmp_1=Search.getMin();
       int tmp_2=Search.getMax();
       Search.positiv.setText("Настрій пісні: "+tmp_1+"-"+tmp_2);


        settings.moveToFirst();
       if(settings.getInt(2)==1&&!SENSOR_ALREADY_REGISTER) {
           mySensorManager.registerListener(proximitySensorEventListener, myProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
           SENSOR_ALREADY_REGISTER=true;
       }if(settings.getInt(2)==0&&SENSOR_ALREADY_REGISTER){
           try {
               mySensorManager.unregisterListener(proximitySensorEventListener,myProximitySensor);
               SENSOR_ALREADY_REGISTER=false;
               Log.d("State","Unregister OK");
           } catch (Exception e) {
               Log.d("State","Error in unregister");
           }
       }


       child.setVisibility(View.VISIBLE);
       fr=getSupportFragmentManager().beginTransaction();
       fr.remove(MainActivity.st);
       fr.commit();
   }
public void style(){
     settings.moveToFirst();
    if(STYLES!=settings.getInt(5)){
        AlertDialog.Builder builder_c = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder_c.setTitle("Зміни");
        builder_c.setMessage("Щоб зміни вступили в силу перезавантажте плеєр");
        builder_c.setNegativeButton("Вийти з плеєра", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
            }
        });
        builder_c.setCancelable(false);
        builder_c.show();
    }
}
public void boosting(){
    if(IS_INIT){
        settings.moveToFirst();
        if(settings.getInt(4)==1) {
            MyService.bass.setStrength((short)1000);
        }else{
            MyService.bass.setStrength((short)0);
        }
    }
}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=newConfig.orientation;
        ViewGroup.MarginLayoutParams params;
        int screenSize;
        switch(orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                screenSize = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;
                switch(screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        params = (ViewGroup.MarginLayoutParams) Search.list.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        Search.list.setLayoutParams(params);
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        params = (ViewGroup.MarginLayoutParams) Search.list.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        Search.list.setLayoutParams(params);
                        Player.gov.setVisibility(View.GONE);
                        Player.gest_txt.setVisibility(View.GONE);
                        Player.rootLayout.setVisibility(View.GONE);
                        MainActivity.pager.setPagingEnabled(true);
                        break;
                    default:
                       break;
                }
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                screenSize = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;
                switch(screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        params = (ViewGroup.MarginLayoutParams) Search.list.getLayoutParams();
                        params.setMargins(0, 150, 0, 0);
                        Search.list.setLayoutParams(params);
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        params = (ViewGroup.MarginLayoutParams) Search.list.getLayoutParams();
                        params.setMargins(0, 150, 0, 0);
                        Search.list.setLayoutParams(params);
                        Player.rootLayout.setVisibility(View.VISIBLE);
                        Player.gov.setVisibility(View.GONE);
                        Player.gest_txt.setVisibility(View.GONE);
                        Player.cardFace.setVisibility(View.VISIBLE);
                        Player.cardBack.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                break;

        }
    }

    public void listStart(){
    save.setVisible(false);
    hint.setVisible(false);
    gest.setVisible(false);
    child.setVisibility(View.GONE);
fr=getSupportFragmentManager().beginTransaction().addToBackStack(null);
    hl=new HelpList();
fr.add(R.id.rooting,hl);
fr.commit();
}


    public void sleeping(){
        if(IS_ALARM_CREATED){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            builder.setTitle("Автозавершення");
            builder.setMessage("Бажаєте вимкнути таймер?");
            builder.setNegativeButton("Скасувати",null);
            builder.setPositiveButton("Вимкнути",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alarm.cancel(startAlarmPI);
                    IS_ALARM_CREATED=false;
                    createToast("Автозавершення вимкнуто");
                }
            });
            builder.setCancelable(false);
            builder.show();
        }else {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            if(settings.getInt(5)==1) {
                tpd.setThemeDark(true);
            }else{
                tpd.setThemeDark(false);
            }
            tpd.setTitle("Автозавершення");
            tpd.show(getFragmentManager(), "TimePicker");
            Log.d("State", "MainActivity: sleeping()");
        }
    }


    public void saving(){
        Log.d("State", "MainActivity: saving()");
        if(!MainActivity.IS_INIT){
            createToast("Пісні не вибрано");
            return;
        }
        String where="SELECT * FROM "+DBHelper.TABLE+" WHERE "+DBHelper.IDS+"="+id;
        Cursor cursor_tmp=db.rawQuery(where,null);
        cursor_tmp.moveToFirst();
      if(((Player.mood.getProgress() - 1) == cursor_tmp.getInt(MOOD_INT)) || ((Player.mood.getProgress() == 0)&&(cursor_tmp.getInt(MOOD_INT)==101))){
          createToast("Настрій не змінено");
          Log.d("State","Pos = "+cursor_tmp.getPosition()+" mood = "+cursor_tmp.getInt(MOOD_INT)+" new_mood = "+(Player.mood.getProgress()-1)+" id = "+cursor_tmp.getInt(IDS_INT)+" =");
      }else{
          createToast("Настрій збережено");
          if(Player.mood.getProgress()==0){
              Log.d("State","Pos = "+cursor_tmp.getPosition()+" mood = "+cursor_tmp.getInt(MOOD_INT)+" new_mood = 101 id = "+cursor_tmp.getInt(IDS_INT));
              cv.put(DBHelper.MOOD,101);
              db.update(DBHelper.TABLE,cv,DBHelper.IDS+" = "+cursor_tmp.getInt(IDS_INT),null);
              cv.clear();
              cursor=db.rawQuery(Search.tmp,null);
          }else{
              Log.d("State","Pos = "+cursor_tmp.getPosition()+" mood = "+cursor_tmp.getInt(MOOD_INT)+" new_mood = "+(Player.mood.getProgress()-1)+" id = "+cursor_tmp.getInt(IDS_INT));
              cv.put(DBHelper.MOOD,(Player.mood.getProgress()-1));
              db.update(DBHelper.TABLE,cv,DBHelper.IDS+" = "+cursor_tmp.getInt(IDS_INT),null);
              cv.clear();
              cursor=db.rawQuery(Search.tmp,null);
          }

      }
        cursor_tmp.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("State", "MainActivity: onStop()");
        IS_HIDDEN=true;
        if(Player.video!=null) {
            Player.video.pause();
        }
        if(IS_INIT) {
            customNotification(this);
        }
    }



    public void openSettings(){
        Log.d("State", "MainActivity: openSettings()");
        save.setVisible(false);
        setts.setVisible(false);
        hint.setVisible(false);
        gest.setVisible(false);
        done.setVisible(false);
        sleep.setVisible(false);
        child.setVisibility(View.GONE);
        fr=getSupportFragmentManager().beginTransaction().addToBackStack(null);
        st=new Setts();
        fr.add(R.id.rooting,st);
        fr.commit();
    }



    public void viewing(){
        Log.d("State", "MainActivity: viewing()");
        if(Player.gov.getVisibility()== View.VISIBLE){
            Player.gov.setVisibility(View.GONE);
            Player.gest_txt.setVisibility(View.GONE);
            MainActivity.pager.setPagingEnabled(true);
            if(Player.card){
                Player.cardFace.setVisibility(View.VISIBLE);
            }else {
                Player.cardBack.setVisibility(View.VISIBLE);
            }

        }else{
            int screenSize = getResources().getConfiguration().screenLayout &Configuration.SCREENLAYOUT_SIZE_MASK;
            if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE&&(screenSize==Configuration.SCREENLAYOUT_SIZE_NORMAL||screenSize==Configuration.SCREENLAYOUT_SIZE_SMALL)) {
            return;
            }
            Player.gov.setVisibility(View.VISIBLE);
                Player.gest_txt.setVisibility(View.VISIBLE);
                settings.moveToFirst();
                if(settings.getInt(5)==1) {
                    Player.gest_txt.setTextColor(getResources().getColor(R.color.text_color));
                }else{
                    Player.gest_txt.setTextColor(getResources().getColor(R.color.text_color));
                }
                Player.gest_txt.setTypeface(MainActivity.tf);

            Player.cardBack.setVisibility(View.GONE);
            Player.cardFace.setVisibility(View.GONE);
            MainActivity.pager.setPagingEnabled(false);
        }
    }

    private ServiceConnection con=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                MyBinder binder = (MyBinder)service;
                Log.d("State", "MainActivity: OnServiceConnected()");
                server=binder.getService();
                bound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("State", "MainActivity: OnServiceDisconnected()");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        settings.moveToFirst();
        if(MainActivity.IS_INIT){
            if(MainActivity.IS_PLAYING){
                Player.play.setImageDrawable(MainActivity.ctx.getResources().getDrawable(R.drawable.pause));
                Player.play.getDrawable().setColorFilter(MainActivity.ctx.getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            }else{
                Player.play.setImageDrawable(MainActivity.ctx.getResources().getDrawable(R.drawable.play));
                Player.play.getDrawable().setColorFilter(MainActivity.ctx.getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            }
        }
        if(!SENSOR_ALREADY_REGISTER&&settings.getInt(2)==1) {
            mySensorManager.registerListener(proximitySensorEventListener, myProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            SENSOR_ALREADY_REGISTER=true;
        }
        Log.d("State","MainActivity: onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("State", "MainActivity: OnStart()");
        IS_HIDDEN=false;
        if(MainActivity.IS_PLAYING&&MainActivity.IS_INIT){
            Player.video.start();
        }
        Log.d("State", "All: " + Runtime.getRuntime().totalMemory() + " Free: " + Runtime.getRuntime().freeMemory());
        try {
            if(i==null){
                i=new Intent(this,MyService.class);
                bindService(i, con, Context.BIND_AUTO_CREATE);
                startService(i);
            }
        } catch (Exception e) {
            Log.d("State","Error in starting service: "+e.toString());
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);

        }
        if(notification!=null){
            mNotificationManager.cancel(NOTIF_ID);notification=null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            IS_DESTROY=true;
        Log.d("State", "MainActivity: OnDestroy()");
        if(MainActivity.IS_PLAYING) {
            server.stopSong();
        }
            try {
                mySensorManager.unregisterListener(proximitySensorEventListener,myProximitySensor);
                unregisterReceiver(screenReceiver);
                Log.d("State","Unregister OK");
            } catch (Exception e) {
                Log.d("State","Error in unregister");
            }
        if(alarm!=null){
            alarm.cancel(startAlarmPI);
            alarm=null;
        }
            if(Player.video!=null){
                Player.video.stop();
                Player.video.release();
                Player.video=null;
            }
        db.close();
        cursor.close();
        all.close();
        settings.close();
        dbHelper.close();
            unbindService(con);
            stopService(i);
            server=null;
        } catch (Exception e) {
            Log.d("State","Error in onDestroy() "+e.toString());
        }
        finally{
            if(notification!=null){
                mNotificationManager.cancel(NOTIF_ID);
                notification=null;
            }
        }
    }



    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        Intent startAlarm=new Intent(ctx,AlarmReceiver.class);
        startAlarm.setAction(ACTION_ALARM);
        startAlarmPI = PendingIntent.getBroadcast(this, 0, startAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        IS_ALARM_CREATED=true;
        Log.d("State", calendar.getTimeInMillis() + " " + System.currentTimeMillis() + " " + ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000));
        if(calendar.getTimeInMillis()-System.currentTimeMillis()<=0){
          calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
            createToast("Музику буде зупинено завтра в "+hourOfDay+":"+minute);
        }else{
            createToast("Музику буде зупинено сьогодні в " + hourOfDay + ":" + minute);
        }

        alarm.set(AlarmManager.RTC, calendar.getTimeInMillis(), startAlarmPI);
    }

    private class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            Log.d("State", String.format("Pager: getItem(%d)", pos));
            switch (pos){
                case 0:return new Search();
                case 1:return new List();
                case 2:return new Player();
                case 3:return new Donate();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            //Log.d("State", "Pager: getCount()"); //very often
            return PAGE_COUNT;
        }
    }



public void init_db(){

    dbHelper=new DBHelper(this);
    db=dbHelper.getWritableDatabase();
    cursor=db.query(DBHelper.TABLE, null, null, null, null, null, null);
    all=db.query(DBHelper.TABLE,null,null,null,null,null,null);
    settings=db.query(DBHelper.SETTINGS,null,null,null,null,null,null);


    prefs=getPreferences(MODE_PRIVATE);
    editor=prefs.edit();
    int isFirstTime=prefs.getInt(IS_FIRST_TIME,1);
    Log.d("State","isFirstTime: "+isFirstTime);
    if(isFirstTime==1) {
        cv.put(DBHelper.FIRST_START, 1);
        db.insert(DBHelper.SETTINGS, null, cv);
        cv.clear();
        editor.putInt(IS_FIRST_TIME,0);
        editor.apply();
    }
}


    public void getAudio(){
        Log.d("State", "MainActivity: getAudio()");
        try {
            Cursor c;
            int idi;
            boolean exist;

            String s= MediaStore.Audio.Media.IS_MUSIC+" != 0";
            String[] p={MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA};
            c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, p, s, null, null);
            int[]idis=new int[c.getCount()];
            Log.d("State", "Start GetAudio()");

            if(c.moveToFirst()){
                do{

                    ids.add(c.getString(1));
                    idi=c.getInt(0);
                    idis[c.getPosition()]=idi;

                    if(cursor.moveToFirst()){
                        exist=false;
                        do{
                            if(idi==cursor.getInt(IDS_INT)){

                                exist=true;
                            }
                        }while(cursor.moveToNext());
                        if(exist){
                            Log.d("State", String.format("Exist, id = %d", idi));
                        }else{
                            Log.d("State", String.format("Added, id = %d", idi));
                            cv.put(DBHelper.IDS,c.getInt(0));
                            cv.put(DBHelper.TITLES,c.getString(1));
                            cv.put(DBHelper.PATHS,c.getString(2));
                            db.insert(DBHelper.TABLE,null,cv);
                            cv.clear();
                        }
                    }else{
                        Log.d("State", String.format("Added, id = %d", idi));
                        cv.put(DBHelper.IDS,c.getInt(0));
                        cv.put(DBHelper.TITLES,c.getString(1));
                        cv.put(DBHelper.PATHS,c.getString(2));

                        db.insert(DBHelper.TABLE,null,cv);
                        cv.clear();
                    }

                }while(c.moveToNext());
            }

            if(cursor.moveToFirst()) {
                do {
                    exist=true;
                    for(int i=0;i<c.getCount();i++){
                        if(cursor.getInt(IDS_INT)==idis[i]){
                            exist=false;
                        }
                    }
                    if(exist){
                        Log.d("State", String.format("Delete, id = %d", cursor.getInt(IDS_INT)));
                        db.delete(DBHelper.TABLE, DBHelper.IDS+" = " + cursor.getInt(IDS_INT), null);

                    }


                } while (cursor.moveToNext());
            }
            c.close();
            cursor=db.query(DBHelper.TABLE,null,null,null,null,null,null);
            all=db.query(DBHelper.TABLE,null,null,null,null,null,null);
            Log.d("State", "Finish GetAudio()");
        } catch (Exception e) {
            Log.d("State", String.format("GetAudio() Error %s", e.toString()));
        }
    }

    @Override
    public void onBackPressed() {
        if(child.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            builder.setTitle("Вихід");
            builder.setMessage("Ви справді бажаєте вийти?");
            builder.setNegativeButton("Скасувати",null);
            builder.setPositiveButton("Вийти",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    System.exit(0);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }else {
            if(UPDATE_DB){
                getAudio();
                UPDATE_DB=false;
                List.ma.notifyDataSetChanged();
                Search.aa.notifyDataSetChanged();
            }
            if (PAGE == 0) {
                done.setVisible(false);
                hint.setVisible(true);
                setts.setVisible(true);
                sleep.setVisible(true);
                save.setVisible(false);
                gest.setVisible(false);
            }
            if (PAGE == 1) {
                done.setVisible(false);
                hint.setVisible(true);
                setts.setVisible(true);
                sleep.setVisible(true);
                save.setVisible(false);
                gest.setVisible(false);
            }
            if (PAGE == 2) {
                done.setVisible(false);
                hint.setVisible(true);
                setts.setVisible(true);
                sleep.setVisible(true);
                gest.setVisible(true);
                save.setVisible(true);
            }
            if (PAGE == 3) {
                done.setVisible(false);
                hint.setVisible(false);
                gest.setVisible(false);
                sleep.setVisible(true);
                setts.setVisible(true);
                save.setVisible(false);
            }
            child.setVisibility(View.VISIBLE);
            super.onBackPressed();
        }
    }

    public void getPager(){
        Log.d("State","MainActivity: getPager()");
        pager=(CustomViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(PAGE_COUNT);
        pagerAdapter = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Потрібний для інтерфейсу
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("State","MainActivity: onPageSelected()");
                PAGE=position;

                    if(PAGE==0){
                        done.setVisible(false);
                        hint.setVisible(true);
                        setts.setVisible(true);
                        sleep.setVisible(true);
                        save.setVisible(false);
                        gest.setVisible(false);
                    }if(PAGE==1){
                        done.setVisible(false);
                        hint.setVisible(true);
                        setts.setVisible(true);
                        sleep.setVisible(true);
                        save.setVisible(false);
                        gest.setVisible(false);
                    }if(PAGE==2){
                        done.setVisible(false);
                        hint.setVisible(true);
                        setts.setVisible(true);
                        sleep.setVisible(true);
                        gest.setVisible(true);
                        save.setVisible(true);
                    }if(PAGE==3){
                        done.setVisible(false);
                        hint.setVisible(false);
                        gest.setVisible(false);
                        sleep.setVisible(true);
                        setts.setVisible(true);
                        save.setVisible(false);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Потрібний для інтерфейсу
            }
        });
        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(pager.getContext());
            mScroller.set(pager, scroller);
            Log.d("State", "MainActivity: Scroller OK");
        } catch (Exception e) {
            Log.d("State", String.format("MainActivity: Error of change scroller: %s", e.toString()));
        }
    }


    //Need for testing
   /*
   public void getDBInLog(){
        if(cursor.moveToFirst()) {
            do {
                Log.d("State", String.format("id = %d, title = %s, mood = %s", cursor.getInt(IDS_INT),cursor.getString(TITLES_INT), cursor.getString(MOOD_INT)));
            } while (cursor.moveToNext());
        }
    }
    */


     SensorEventListener proximitySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            powermanager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            settings.moveToFirst();
            if (settings.getInt(2) == 1){
                    if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                        DISTANCE = event.values[0];
                        if (event.values[0] != 0) {
                            if (IS_PAUSED_BY_SENSOR &&IS_INIT && !IS_PLAYING) {
                                server.resumeSong();
                                count = 0;
                            }
                        }
                        if (event.values[0] == 0 && count == 0 && IS_INIT && !IS_PAUSED_BY_SENSOR&&IS_PLAYING) {
                            if (powermanager.isScreenOn()) //isInteractive() not deprecated but from API 20, so...
                            {
                                sensorHandler.postDelayed(isUnderHand, 500);
                            }
                        }
                    }
        }else if(IS_INIT&&IS_PAUSED_BY_SENSOR&& !IS_PLAYING){
                server.resumeSong();
                count=0;
            }
        }
    };
    Runnable isUnderHand=new Runnable() {
        @Override
        public void run() {
            count++;
            if(count==CHECKING_TIME){
                if(IS_INIT&&IS_PLAYING) {
                    server.pauseSong();
                    IS_PAUSED_BY_SENSOR=true;
                }
            }
            if(DISTANCE==0){
                sensorHandler.postDelayed(this,500);
            }else{
                count=0;
            }
        }
    };


    public static void customNotification(Context ctx){
        Log.d("State","MainActivity: customNotification()");
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent play=new Intent(ctx,pendingIntentListener.class);
        RemoteViews contentView;
        if(IS_PLAYING) {
            contentView = new RemoteViews(ctx.getPackageName(), R.layout.notif_pause);
            play.setAction(ACTION_PAUSE);
        }else{
            contentView = new RemoteViews(ctx.getPackageName(), R.layout.notif);
            play.setAction(ACTION_PLAY);
        }
        if(cursor_numb==1) {
            all.moveToPosition(pos);
            contentView.setTextViewText(R.id.title_n,all.getString(TITLES_INT));
        }
        if(cursor_numb==2) {
            cursor.moveToPosition(pos);
            contentView.setTextViewText(R.id.title_n,cursor.getString(TITLES_INT));
        }
        Intent prev=new Intent(ctx,pendingIntentListener.class);
        Intent next=new Intent(ctx,pendingIntentListener.class);
        Intent close=new Intent(ctx,pendingIntentListener.class);
        Intent click=new Intent(ctx,MainActivity.class);
        prev.setAction(ACTION_PREV);
        next.setAction(ACTION_NEXT);
        close.setAction(ACTION_CLOSE);
        PendingIntent prev_p = PendingIntent.getBroadcast(ctx, 0,prev,0);
        PendingIntent play_p = PendingIntent.getBroadcast(ctx, 0,play,0);
        PendingIntent next_p = PendingIntent.getBroadcast(ctx, 0,next,0);
        PendingIntent close_p = PendingIntent.getBroadcast(ctx, 0,close,0);
        PendingIntent click_p=PendingIntent.getActivity(ctx, 0,click,0);
        contentView.setOnClickPendingIntent(R.id.prev_n,prev_p);
        contentView.setOnClickPendingIntent(R.id.play_n,play_p);
        contentView.setOnClickPendingIntent(R.id.next_n,next_p);
        contentView.setOnClickPendingIntent(R.id.close_n,close_p);


        notification = new Notification.Builder(ctx).setContent(contentView).setContentIntent(click_p).setSmallIcon(R.drawable.ic_launcher).build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(NOTIF_ID, notification);
    }
    public static class pendingIntentListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("State","pendingIntentListener: onReceive()");
            Log.d("State","Action: "+intent.getAction());
            switch (intent.getAction()){
                case ACTION_NEXT:server.musicCompleted();break;
                case ACTION_PREV:server.prev();break;
                case ACTION_PLAY:server.resumeSong();break;
                case ACTION_PAUSE:server.pauseSong();break;
                case ACTION_CLOSE:server.stopSong();server.un_init();break;
                default:break;
            }

        }
    }
}

