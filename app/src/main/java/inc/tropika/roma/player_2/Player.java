package inc.tropika.roma.player_2;

import android.content.res.AssetFileDescriptor;
import android.gesture.GestureOverlayView;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class Player extends android.support.v4.app.Fragment implements TextureView.SurfaceTextureListener{
    public static ImageButton play;
    public static GestureOverlayView gov;
    public static TextView gest_txt;
    public static boolean card=true;
    public static SeekBar mood,duration;
    public static View cardBack,cardFace,all;
    public static TextView title,currTime,allTime,mood_t,min1,min2,min3,min4,min5,max1,max2,max3,max4,max5,freq1,freq2,freq3,freq4,freq5;
    public static SeekBar seekBar1,seekBar2,seekBar3,seekBar4,seekBar5;
    public static MediaPlayer video;
    Utils utils;
    public static RelativeLayout root;



    ImageButton shuffle,next,prev,repeat;
    public static View rootLayout;
    View cardBackCont;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("State", "Player: onCreateView()");
        utils=new Utils(getActivity().getBaseContext());
        View view=inflater.inflate(R.layout.fragment_player, container, false);
        all=view;
        root =(RelativeLayout)view.findViewById(R.id.rootik);
        TextureView tv=(TextureView)view.findViewById(R.id.textureView);
        tv.setSurfaceTextureListener(this);
        gov=(GestureOverlayView)view.findViewById(R.id.overlay);
        gest_txt=(TextView)view.findViewById(R.id.gest_txt);
        gest_txt.setTypeface(MainActivity.tf);
        mood=(SeekBar)view.findViewById(R.id.mood);
        duration=(SeekBar)view.findViewById(R.id.duration);
        mood_t=(TextView)view.findViewById(R.id.mood_t);
        mood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!MainActivity.IS_INIT){
                 mood_t.setText(getResources().getString(R.string.mood_not_chosen));
                 }else if (progress != 0) {
                    String res=getResources().getString(R.string.mood);
                 mood_t.setText(String.format("%s: %s", res, Integer.toString((progress - 1))));
                 }else{
                 mood_t.setText(getResources().getString(R.string.mood_not_chosen));
                 }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            //Потрібний для інтерфейсу
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            if(!MainActivity.IS_INIT){
        seekBar.setProgress(0);
            }
            }
        });
        shuffle=(ImageButton)view.findViewById(R.id.shuffle);
        play=(ImageButton)view.findViewById(R.id.play);
        next=(ImageButton)view.findViewById(R.id.next);
        prev=(ImageButton)view.findViewById(R.id.prev);
        repeat=(ImageButton)view.findViewById(R.id.repeat);
        title=(TextView)view.findViewById(R.id.title);
        currTime=(TextView)view.findViewById(R.id.currTime);
        allTime=(TextView)view.findViewById(R.id.allTime);
        currTime.setTypeface(MainActivity.tf);
        allTime.setTypeface(MainActivity.tf);
        mood_t.setTypeface(MainActivity.tf);


        min1=(TextView)view.findViewById(R.id.textView2);
        min2=(TextView)view.findViewById(R.id.textView5);
        min3=(TextView)view.findViewById(R.id.textView9);
        min4=(TextView)view.findViewById(R.id.textView13);
        min5=(TextView)view.findViewById(R.id.textView16);
        max1=(TextView)view.findViewById(R.id.textView3);
        max2=(TextView)view.findViewById(R.id.textView6);
        max3=(TextView)view.findViewById(R.id.textView10);
        max4=(TextView)view.findViewById(R.id.textView12);
        max5=(TextView)view.findViewById(R.id.textView15);
        freq1=(TextView)view.findViewById(R.id.textView);
        freq2=(TextView)view.findViewById(R.id.textView4);
        freq3=(TextView)view.findViewById(R.id.textView8);
        freq4=(TextView)view.findViewById(R.id.textView11);
        freq5=(TextView)view.findViewById(R.id.textView14);
        seekBar1=(SeekBar)view.findViewById(R.id.seekBar);
        seekBar2=(SeekBar)view.findViewById(R.id.seekBar2);
        seekBar3=(SeekBar)view.findViewById(R.id.seekBar3);
        seekBar4=(SeekBar)view.findViewById(R.id.seekBar4);
        seekBar5=(SeekBar)view.findViewById(R.id.seekBar5);

        rootLayout = view.findViewById(R.id.main_activity_root);
        cardFace = view.findViewById(R.id.main_activity_card_face);
        cardBack = view.findViewById(R.id.main_activity_card_back);
        cardBackCont = view.findViewById(R.id.cont_2);
        cardFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flipCard();
            }
        });
        cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        cardBackCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
        gestures();
        buttons();
        titles();
        setupEqualizer();
        style();
        return view;
    }

public void style(){
    MainActivity.settings.moveToFirst();
    if(MainActivity.settings.getInt(5)==0){
        root.setBackground(getResources().getDrawable(R.drawable.lv_3));
        min1.setTextColor(getResources().getColor(R.color.text_color));
        min2.setTextColor(getResources().getColor(R.color.text_color));
        min3.setTextColor(getResources().getColor(R.color.text_color));
        min4.setTextColor(getResources().getColor(R.color.text_color));
        min5.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        max2.setTextColor(getResources().getColor(R.color.text_color));
        max3.setTextColor(getResources().getColor(R.color.text_color));
        max4.setTextColor(getResources().getColor(R.color.text_color));
        max5.setTextColor(getResources().getColor(R.color.text_color));
        freq1.setTextColor(getResources().getColor(R.color.text_color));
        freq2.setTextColor(getResources().getColor(R.color.text_color));
        freq3.setTextColor(getResources().getColor(R.color.text_color));
        freq4.setTextColor(getResources().getColor(R.color.text_color));
        freq5.setTextColor(getResources().getColor(R.color.text_color));
        title.setTextColor(getResources().getColor(R.color.text_color));
        currTime.setTextColor(getResources().getColor(R.color.text_color));
        allTime.setTextColor(getResources().getColor(R.color.text_color));
        mood_t.setTextColor(getResources().getColor(R.color.text_color));
    }
    else{
        root.setBackground(getResources().getDrawable(R.drawable.st_3));
        min1.setTextColor(getResources().getColor(R.color.text_color));
        min2.setTextColor(getResources().getColor(R.color.text_color));
        min3.setTextColor(getResources().getColor(R.color.text_color));
        min4.setTextColor(getResources().getColor(R.color.text_color));
        min5.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        max1.setTextColor(getResources().getColor(R.color.text_color));
        freq1.setTextColor(getResources().getColor(R.color.text_color));
        freq2.setTextColor(getResources().getColor(R.color.text_color));
        freq3.setTextColor(getResources().getColor(R.color.text_color));
        freq4.setTextColor(getResources().getColor(R.color.text_color));
        freq5.setTextColor(getResources().getColor(R.color.text_color));
        title.setTextColor(getResources().getColor(R.color.text_color));
        currTime.setTextColor(getResources().getColor(R.color.text_color));
        allTime.setTextColor(getResources().getColor(R.color.text_color));
        mood_t.setTextColor(getResources().getColor(R.color.text_color));
    }


}

    public void titles(){
        Log.d("State","Player: titles()");
        title.setMaxWidth((int)(Utils.w *0.8));
        title.setTypeface(MainActivity.tf);
        title.setFocusable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MainActivity.IS_INIT) {
            if(MainActivity.cursor_numb==2) {
                MainActivity.cursor.moveToPosition(MainActivity.pos);
                Player.title.setText(MainActivity.cursor.getString(MainActivity.TITLES_INT));
            }
            if(MainActivity.cursor_numb==1) {
                MainActivity.all.moveToPosition(MainActivity.pos);
                Player.title.setText(MainActivity.all.getString(MainActivity.TITLES_INT));
            }
            Player.play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            Player.play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            MainActivity.duration=MainActivity.server.getDuration();
            Player.duration.setMax(MainActivity.duration);
            Player.allTime.setText(utils.milliSecondsToTimer(MainActivity.server.getDuration()));
            int progress=0;
            if(MainActivity.cursor_numb==2) {
                MainActivity.cursor.moveToPosition(MainActivity.pos);
                progress = MainActivity.cursor.getInt(MainActivity.MOOD_INT);
            }
            if(MainActivity.cursor_numb==1) {
                MainActivity.all.moveToPosition(MainActivity.pos);
                progress = MainActivity.all.getInt(MainActivity.MOOD_INT);
            }


            if(progress==101){
                Player.mood.setProgress(0);
                Player.mood_t.setText(getResources().getString(R.string.mood_not_chosen));
            }else{
                String res=getResources().getString(R.string.mood);
                Player.mood_t.setText(String.format("%s: %s", res,Integer.toString(progress)));
                progress++;
                Player.mood.setProgress(progress);

            }
        }
    }

    public void buttons(){
        Log.d("State", "Player: buttons()");
        duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    currTime.setText(utils.milliSecondsToTimer(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO) {
                    MainActivity.server.seekTo(seekBar.getProgress());
                }else{
                    seekBar.setProgress(0);
                }
            }
        });



    shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    shuffle.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(MainActivity.IS_SHUFFLE){
                    shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    MainActivity.IS_SHUFFLE=false;
                }else{
                    shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor800), PorterDuff.Mode.MULTIPLY);
                    MainActivity.IS_SHUFFLE=true;
                    if(MainActivity.IS_REPEAT){
                        MainActivity.IS_REPEAT=false;
                        repeat.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    }
                }
                if(!MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    MainActivity.IS_SHUFFLE=false;
                }
            }
            try {
                MainActivity.server.setLooping();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    });

    repeat.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    repeat.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                repeat.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(MainActivity.IS_REPEAT){
                    repeat.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    MainActivity.IS_REPEAT=false;
                }else{
                    repeat.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor800), PorterDuff.Mode.MULTIPLY);
                    if(MainActivity.IS_SHUFFLE){
                        MainActivity.IS_SHUFFLE=false;
                        shuffle.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    }
                    MainActivity.IS_REPEAT=true;
                }
                if(!MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    repeat.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                    MainActivity.IS_REPEAT=false;
                }
            }
            try {
                MainActivity.server.setLooping();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    });


    next.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    next.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                next.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                next.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO) {
                    Log.d("State", "Next song selected");
                    MainActivity.server.musicCompleted();
                }
            }
            return true;
        }
    });

    prev.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    prev.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                prev.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                prev.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO) {
                    Log.d("State", "Previous song selected");
                    MainActivity.server.prev();
                }
            }
            return true;
        }
    });


if(MainActivity.IS_PLAYING) {
    play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
}else{
    play.setImageDrawable(getResources().getDrawable(R.drawable.play));
}
    play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
    play.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                play.getDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                    if (MainActivity.IS_PLAYING) {
                        MainActivity.server.pauseSong();
                    } else {
                        MainActivity.server.resumeSong();
                    }
                }
                    play.getDrawable().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            }
            return true;
        }
    });



}



    public void gestures(){
        Log.d("State", "Player: gestures()");
        gov.setOnTouchListener(new OnSwipeTouchListener(getActivity().getBaseContext()) {
            public void onSwipeTop() {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP){
                    if(MainActivity.VOLUME<100f){
                        MainActivity.VOLUME+=10f;
                        MainActivity.server.setVolume();
                       MainActivity.createToast(MainActivity.VOLUME+"%");
                    }else{
                        MainActivity.createToast(MainActivity.VOLUME + "%");
                    }
                }
                Log.d("State","ToTopSwipe");
            }


            public void onSwipeRight() {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    if(duration.getProgress()+20000>=MainActivity.duration){
                        MainActivity.server.musicCompleted();
                    }else{
                        duration.setProgress(duration.getProgress()+20000);
                        MainActivity.server.seekTo(duration.getProgress());
                    }
                }
                Log.d("State", "ToRightSwipe");
            }


            public void onSwipeLeft() {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    if(duration.getProgress()-20000<=0){
                        duration.setProgress(0);
                        MainActivity.server.seekTo(duration.getProgress());
                    }else{
                        duration.setProgress(duration.getProgress()-20000);
                        MainActivity.server.seekTo(duration.getProgress());
                    }
                }
                Log.d("State","ToLeftSwipe");
            }


            public void onSwipeBottom() {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP){
                    if(MainActivity.VOLUME>0f){
                        MainActivity.VOLUME-=10f;
                        MainActivity.server.setVolume();
                        MainActivity.createToast(MainActivity.VOLUME + "%");
                    }else{
                        MainActivity.createToast(MainActivity.VOLUME + "%");
                    }
                }
                Log.d("State","ToBottomSwipe");
            }

            @Override
            public void onTouchCenter() {
                if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO){
                    if(duration.getProgress()-5000<=0){
                        duration.setProgress(0);
                        MainActivity.server.seekTo(duration.getProgress());
                    }else{
                        duration.setProgress(duration.getProgress()-5000);
                        MainActivity.server.seekTo(duration.getProgress());
                    }
                }
                Log.d("State", "Single Tap");
            }


            public boolean onTouch(View v, MotionEvent event) {
                //Потрібний для інтерфейсу
                return gestureDetector.onTouchEvent(event);
            }});

    }







    public static void setupEqualizer() {
        try {
            final short min;
            final short max;
            final Equalizer equalizer = new Equalizer(0, MainActivity.SESSION);
            equalizer.setEnabled(true);
            min = equalizer.getBandLevelRange()[0];
            max = equalizer.getBandLevelRange()[1];
            min1.setTypeface(MainActivity.tf);
            min2.setTypeface(MainActivity.tf);
            min3.setTypeface(MainActivity.tf);
            min4.setTypeface(MainActivity.tf);
            min5.setTypeface(MainActivity.tf);
            max1.setTypeface(MainActivity.tf);
            max2.setTypeface(MainActivity.tf);
            max3.setTypeface(MainActivity.tf);
            max4.setTypeface(MainActivity.tf);
            max5.setTypeface(MainActivity.tf);
            freq1.setTypeface(MainActivity.tf);
            freq2.setTypeface(MainActivity.tf);
            freq3.setTypeface(MainActivity.tf);
            freq4.setTypeface(MainActivity.tf);
            freq5.setTypeface(MainActivity.tf);
            freq1.setText((equalizer.getCenterFreq((short) 0)/1000)+"hz");
            freq2.setText((equalizer.getCenterFreq((short) 1)/1000)+"hz");
            freq3.setText((equalizer.getCenterFreq((short) 2)/1000)+"hz");
            freq4.setText((equalizer.getCenterFreq((short) 3)/1000)+"hz");
            freq5.setText((equalizer.getCenterFreq((short) 4)/1000)+"hz");
            min1.setText((min/100)+"db");
            min2.setText((min/100)+"db");
            min3.setText((min/100)+"db");
            min4.setText((min/100)+"db");
            min5.setText((min/100)+"db");
            max1.setText((max/100)+"db");
            max2.setText((max/100)+"db");
            max3.setText((max/100)+"db");
            max4.setText((max/100)+"db");
            max5.setText((max/100)+"db");
            seekBar1.setMax(max-min);
            seekBar1.setProgress(equalizer.getBandLevel((short) 0));
            seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                        equalizer.setBandLevel((short) 0, (short) (progress + min));

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            seekBar2.setMax(max-min);
            seekBar2.setProgress(equalizer.getBandLevel((short) 1));
            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                        equalizer.setBandLevel((short) 1, (short) (progress + min));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            seekBar3.setMax(max-min);
            seekBar3.setProgress(equalizer.getBandLevel((short) 2));
            seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                        equalizer.setBandLevel((short) 2, (short) (progress + min));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            seekBar4.setMax(max-min);
            seekBar4.setProgress(equalizer.getBandLevel((short) 3));
            seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                        equalizer.setBandLevel((short) 3, (short) (progress + min));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            seekBar5.setMax(max-min);
            seekBar5.setProgress(equalizer.getBandLevel((short) 4));
            seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(MainActivity.IS_INIT&&!MainActivity.IS_RADIO_TMP) {
                        equalizer.setBandLevel((short) 4, (short) (progress + min));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            Log.d("State","Player: All: "+Runtime.getRuntime().totalMemory()+" Free: "+Runtime.getRuntime().freeMemory());
        } catch (RuntimeException e) {
            Log.d("State","Error in setup equalizer: "+e.toString());
        }
    }








    private void flipCard()
    {
        Log.d("State","Player: flipCard()");
        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE)
        {
            flipAnimation.reverse();
            card=true;
        }else{
            card=false;
        }
        rootLayout.startAnimation(flipAnimation);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            Surface sur=new Surface(surface);
            AssetFileDescriptor afd=getActivity().getAssets().openFd(MainActivity.FILE);
            video=new MediaPlayer();
            video.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            video.setSurface(sur);
            video.setLooping(true);
            video.prepare();
            video.start();
            Handler pauseFirst=new Handler();
            pauseFirst.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(MainActivity.IS_FIRST_TIME_BY_SESSION_PLAY) {
                        video.pause();
                    }
                }
            },500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}

