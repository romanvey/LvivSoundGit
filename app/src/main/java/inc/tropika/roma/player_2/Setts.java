package inc.tropika.roma.player_2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Setts extends android.support.v4.app.Fragment {
    View view;
    public static SeekBar mistake;
    public static CheckBox f1,f2,f3,f4;
    public static  TextView diapason;
    boolean f1check,f2check,f3check,f4check;
    int mist;
    SensorManager mySensorManager;
    Sensor myProximitySensor;
    public static Button clear,upd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view =inflater.inflate(R.layout.fragment_setts, container, false);
        clear = (Button) view.findViewById(R.id.clear);
        mistake = (SeekBar) view.findViewById(R.id.mistake);
        upd=(Button)view.findViewById(R.id.manual);
        f1 = (CheckBox) view.findViewById(R.id.f1);
        f2 = (CheckBox) view.findViewById(R.id.f2);
        f3 = (CheckBox) view.findViewById(R.id.autostarting);
        f4 = (CheckBox) view.findViewById(R.id.repeating);
        diapason = (TextView) view.findViewById(R.id.text_mist);
        f1.setTypeface(MainActivity.tf);
        f2.setTypeface(MainActivity.tf);
        f3.setTypeface(MainActivity.tf);
        f4.setTypeface(MainActivity.tf);
        diapason.setTypeface(MainActivity.tf);
        clear.setTypeface(MainActivity.tf);
        upd.setTypeface(MainActivity.tf);


        MainActivity.settings.moveToFirst();
        mist=MainActivity.settings.getInt(1);
        f1check = MainActivity.settings.getInt(2) != 0;
        f2check = MainActivity.settings.getInt(3) != 0;
        f3check = MainActivity.settings.getInt(4) != 0;
        f4check = MainActivity.settings.getInt(5) != 0;

        mistake.setProgress(mist);
        diapason.setText("Діапазон: +/-"+mist);
        f1.setChecked(f1check);
        f2.setChecked(f2check);
        f3.setChecked(f3check);
        f4.setChecked(f4check);
        listeners();
        return view;
    }


    public void listeners(){
        Log.d("State","Settings: listeners()");
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.ctx, R.style.MyAlertDialogStyle);
                builder.setTitle("Очищення");
                builder.setMessage("Очистити весь настрій?");
                builder.setNegativeButton("Скасувати", null);
                builder.setPositiveButton("Очистити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.IS_PLAYING) {
                            try {
                                MainActivity.server.stopSong();
                                MainActivity.server.un_init();
                            } catch (Exception e) {
                                Log.d("State", "Nothing playing, error: " + e.toString());
                            }
                        }
                        ContentValues cv = new ContentValues();
                        cv.put(DBHelper.MOOD, 101);
                        MainActivity.db.update(DBHelper.TABLE, cv, null, null);
                        cv.clear();
                        MainActivity.cursor = MainActivity.db.rawQuery(Search.tmp, null);
                        if (MainActivity.ids.size() > 0) {
                            MainActivity.ids.clear();
                            MainActivity.ids = new ArrayList<>();
                        }
                        if (MainActivity.cursor.moveToFirst()) {
                            do {
                                MainActivity.ids.add(MainActivity.cursor.getString(MainActivity.TITLES_INT));
                                Log.d("State", "" + MainActivity.cursor.getString(MainActivity.TITLES_INT));
                            } while (MainActivity.cursor.moveToNext());
                        }
                        List.ma.notifyDataSetChanged();
                        List.nothing.setVisibility(View.VISIBLE);
                        List.lv.setVisibility(View.INVISIBLE);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

        mistake.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                update();
                diapason.setText("Діапазон: +/-" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Потрібний для інтерфейсу
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Потрібний для інтерфейсу
            }
        });
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                if (f1.isChecked()) {
                    mySensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
                    myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                    if (myProximitySensor == null) {
                        MainActivity.createToast("Не знайдено датчика");
                        f1.setChecked(false);
                    }
                }
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });






upd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        MainActivity.UPDATE_DB=true;
       getActivity().onBackPressed();
    }
});




    }



    public void update(){
        Log.d("State","Settings: update()");
        MainActivity.settings.moveToFirst();
        f1check = MainActivity.settings.getInt(2) != 0;
        f2check = MainActivity.settings.getInt(3) != 0;
        f3check = MainActivity.settings.getInt(4) != 0;
        f4check = MainActivity.settings.getInt(5) != 0;

        if((MainActivity.settings.getInt(1)==mistake.getProgress())&&(f1check==f1.isChecked())
                &&(f2check==f2.isChecked())&&(f3check==f3.isChecked())&&(f4check==f4.isChecked())){
            MainActivity.done.setVisible(false);
        }else{
            MainActivity.done.setVisible(true);
        }
    }



}
