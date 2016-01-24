package inc.tropika.roma.player_2;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;
import java.util.ArrayList;

public class Search extends android.support.v4.app.Fragment {
    public static String tmp="SELECT * FROM "+DBHelper.TABLE;
    public static int mistake;
FloatingActionButton fab;
    SharedPreferences pref;
    SharedPreferences.Editor ed;
    public static TextView positiv;
    public static CheckBox uncategory;
    public static SeekBar search;
    int progress;
    public static ListView list;
    public static AllAdapter aa;
    boolean enabled;
    public static RelativeLayout root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("State", "Search: onCreateView()");
        View view=inflater.inflate(R.layout.fragment_search, container, false);

        root =(RelativeLayout)view.findViewById(R.id.root);


       positiv =(TextView)view.findViewById(R.id.positiv);
       uncategory =(CheckBox)view.findViewById(R.id.uncategory);
        if(MainActivity.settings.getInt(5)==0){
            root.setBackground(getResources().getDrawable(R.drawable.lv_1));
            positiv.setTextColor(getResources().getColor(R.color.text_color));
            uncategory.setTextColor(getResources().getColor(R.color.text_color));
        }
        else{
            root.setBackground(getResources().getDrawable(R.drawable.st_1));
            positiv.setTextColor(getResources().getColor(R.color.text_color));
            uncategory.setTextColor(getResources().getColor(R.color.text_color));

        }
       fab=(FloatingActionButton)view.findViewById(R.id.fab);
       search=(SeekBar)view.findViewById(R.id.search);

        list=(ListView)view.findViewById(R.id.listView2);
        aa=new AllAdapter(getActivity().getBaseContext());
        list.setAdapter(aa);
        if(MainActivity.all.getCount()==0){
            list.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.VISIBLE);
        }
        pref=getActivity().getPreferences(Context.MODE_PRIVATE);
        ed=pref.edit();
        ed.apply();

        if(!MainActivity.settings.moveToFirst()){
            getActivity().getBaseContext().deleteDatabase(DBHelper.BD);
           getActivity().finish();
            System.exit(0);
        }
        mistake=MainActivity.settings.getInt(1);

        listeners();

        progress=pref.getInt(MainActivity.SEARCH,50);
        enabled=pref.getBoolean(MainActivity.UNCATEGORY,true);

        search.setProgress(progress);
        uncategory.setChecked(enabled);

        int tmp_1=getMin();
        int tmp_2=getMax();

        positiv.setTypeface(MainActivity.tf);
        positiv.setText(R.string.mood_songs+": "+tmp_1+"-"+tmp_2);
        uncategory.setTypeface(MainActivity.tf);


        return view;
    }


    public void listeners(){
        Log.d("State","Search: listeners()");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.cursor_numb=1;
                if(!MainActivity.IS_INIT&&!MainActivity.IS_FIRST_TIME_BY_SESSION_PLAY){
                    MainActivity.server.initMusicPlayer();
                }
                MainActivity.all.moveToPosition(position);
                if(MainActivity.all.getInt(MainActivity.IDS_INT)==MainActivity.id){
                    drag(2);
                    MainActivity.pos = position;
                    if(!MainActivity.IS_INIT){
                        MainActivity.server.playSong();
                    }
                }else {
                    drag(2);
                    MainActivity.pos = position;
                    MainActivity.server.playSong();
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.all.moveToPosition(position);
                if(MainActivity.all.getInt(MainActivity.MOOD_INT)==101) {
                    MainActivity.createToast(R.string.mood_not_chosen+"");
                }else{
                    MainActivity.createToast(R.string.mood+": " + MainActivity.all.getInt(MainActivity.MOOD_INT));
                }
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.notification!=null){
                    MainActivity.mNotificationManager.cancel(MainActivity.NOTIF_ID);
                }
                int tmp_1=getMin();
                int tmp_2=getMax();
                if(uncategory.isChecked()) {
                    tmp = "SELECT * FROM " + DBHelper.TABLE + " WHERE (" + DBHelper.MOOD + " BETWEEN "+tmp_1+" AND "+tmp_2 + ") OR " + DBHelper.MOOD + "=101";
                }else {
                    tmp = "SELECT * FROM " + DBHelper.TABLE + " WHERE " + DBHelper.MOOD + " BETWEEN "+tmp_1+" AND "+tmp_2;
                }
                Log.d("State",tmp);
                Log.d("State",MainActivity.last_where);
                ed.putString(MainActivity.WHERE,tmp);
                MainActivity.IS_NEW_LIST = !MainActivity.last_where.equals(tmp);
                MainActivity.last_where=tmp;
                Log.d("State","New List: "+MainActivity.IS_NEW_LIST);
                ed.commit();
                MainActivity.cursor=MainActivity.db.rawQuery(tmp,null);
                MainActivity.ids.clear();
                MainActivity.ids=new ArrayList<>();
                if(MainActivity.cursor.moveToFirst()){
                    do{
                        MainActivity.ids.add(MainActivity.cursor.getString(MainActivity.TITLES_INT));
                        Log.d("State", ""+MainActivity.cursor.getString(MainActivity.TITLES_INT));
                    }while(MainActivity.cursor.moveToNext());
                }
                List.ma.notifyDataSetChanged();
                toast();
            }
        });
        search.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pr, boolean fromUser) {
                ed.putInt(MainActivity.SEARCH,pr);
                progress=pr;
                ed.commit();
                int tmp_1=getMin();
                int tmp_2=getMax();
                positiv.setText(R.string.mood_songs+": "+tmp_1+"-"+tmp_2);
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
        uncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.putBoolean(MainActivity.UNCATEGORY,uncategory.isChecked());
                ed.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("State","Search: onResume()");
    }

    public static int getMin(){
        Log.d("State","Search: getMin()");
        MainActivity.settings.moveToFirst();
        mistake=MainActivity.settings.getInt(1);
        int tmp;
        if(search.getProgress()-mistake<0){
            tmp=0;
        }else{
            tmp=search.getProgress()-mistake;
        }
        return tmp;
    }

   public static int getMax(){
       Log.d("State","Search: getMax()");
       MainActivity.settings.moveToFirst();
       mistake=MainActivity.settings.getInt(1);
        int tmp;
        if(search.getProgress()+mistake>100){
            tmp=100;
        }else{
            tmp=search.getProgress()+mistake;
        }
       return tmp;
    }


    public void toast(){
        Log.d("State","Search: toast()");
        if(MainActivity.ids.size()==0){
            MainActivity.createToast(R.string.songs_not_found+"");
            List.nothing.setVisibility(View.VISIBLE);
            List.lv.setVisibility(View.INVISIBLE);
        }else{
            List.nothing.setVisibility(View.INVISIBLE);
            List.lv.setVisibility(View.VISIBLE);
            if(MainActivity.ids.size()%10==1) {
                MainActivity.createToast(R.string.founded+" " + MainActivity.ids.size() +" "+R.string.song_1);
            }else if(MainActivity.ids.size()%10==2 || MainActivity.ids.size()%10==3 || MainActivity.ids.size()%10==4){
                MainActivity.createToast(R.string.founded+" " + MainActivity.ids.size() +" "+R.string.song_2);
            }else{
                MainActivity.createToast(R.string.founded+" " + MainActivity.ids.size() +" "+ R.string.song_3);
            }
            drag(1);
        }
    }

    public void drag(int i){
        Log.d("State","Search: drag()");
        ViewPagerScroller.mScrollDuration=1500;
        MainActivity.pager.setCurrentItem(i,true);
        ViewPagerScroller.mScrollDuration=300;
    }
}


