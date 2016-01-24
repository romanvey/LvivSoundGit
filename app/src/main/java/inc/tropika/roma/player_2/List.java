package inc.tropika.roma.player_2;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class List extends android.support.v4.app.Fragment {
    public static ListView lv;
    public static MusicAdapter ma;
    public static TextView nothing;
    Utils utils;
    public static RelativeLayout root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list,null);
        Log.d("State", "List: OnCreateView()");
        root =(RelativeLayout)v.findViewById(R.id.root);
        MainActivity.settings.moveToFirst();
        if(MainActivity.settings.getInt(5)==0){
            root.setBackground(getResources().getDrawable(R.drawable.lv_2));
        }
        else{
            root.setBackground(getResources().getDrawable(R.drawable.st_2));
        }
        ma=new MusicAdapter(getActivity().getBaseContext());
        lv=(ListView)v.findViewById(R.id.listView);
        nothing=(TextView)v.findViewById(R.id.nothing);
        nothing.setTypeface(MainActivity.tf);
        lv.setAdapter(ma);
        utils=new Utils(getActivity().getBaseContext());
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.cursor.moveToPosition(position);
                if(MainActivity.cursor.getInt(MainActivity.MOOD_INT)==101) {
                    MainActivity.createToast(getResources().getString(R.string.mood_unknown));
                }else{
                    String res=getResources().getString(R.string.mood_songs);
                    MainActivity.createToast(String.format("%s: %s", res, Integer.toString(MainActivity.cursor.getInt(MainActivity.MOOD_INT))));
                }
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("State", "List: onItemClick()");
                Player.video.start();
                MainActivity.cursor_numb=2;
                if(!MainActivity.IS_INIT&&!MainActivity.IS_FIRST_TIME_BY_SESSION_PLAY){
                    MainActivity.server.initMusicPlayer();
                }
                MainActivity.cursor.moveToPosition(position);
                if(MainActivity.cursor.getInt(MainActivity.IDS_INT)==MainActivity.id){
                    drag();
                    MainActivity.pos = position;
                    if(!MainActivity.IS_INIT){
                    MainActivity.server.playSong();
                    }
                }else {
                    drag();
                    MainActivity.pos = position;
                    MainActivity.server.playSong();
                }
            }
        });
        return v;

    }
    public void drag(){
        Log.d("State", "List: drag()");
    ViewPagerScroller.mScrollDuration=1500;
        MainActivity.pager.setCurrentItem(2,true);
        ViewPagerScroller.mScrollDuration=300;
    }

}


