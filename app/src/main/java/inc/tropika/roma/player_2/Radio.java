package inc.tropika.roma.player_2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class Radio extends android.support.v4.app.Fragment {
    public static RelativeLayout root;
    public static ListView radio_list;
    public static RadioAdapter ra;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("State", "Radio: OnCreateView()");
        View view=inflater.inflate(R.layout.fragment_radio, container, false);
        root =(RelativeLayout)view.findViewById(R.id.root);
        radio_list=(ListView)view.findViewById(R.id.radio_list);
        ra=new RadioAdapter(getActivity().getBaseContext());
        radio_list.setAdapter(ra);

        if(MainActivity.settings.getInt(5)==0){
            root.setBackground(getResources().getDrawable(R.drawable.lv_4));
        }
        else{
            root.setBackground(getResources().getDrawable(R.drawable.st_4));
        }
        radio_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                drag();
                if(!MainActivity.IS_INIT&&!MainActivity.IS_FIRST_TIME_BY_SESSION_PLAY){
                    MainActivity.server.initMusicPlayer();
                }
                    if(MainActivity.IS_PLAYING&&MainActivity.IS_RADIO&&position==MainActivity.pos_radio){

                    }else {
                        try {
                            MainActivity.radioTmp.startRadio(MainActivity.radio_paths[position]);
                            MainActivity.pos_radio = position;
                            MainActivity.IS_RADIO = true;
                            MainActivity.IS_RADIO_TMP = true;
                            MainActivity.server.playSong();
                            Player.title.setText(getResources().getString(R.string.conecting_to_station_title));
                            Player.duration.setMax(0);
                            Player.video.pause();
                            Player.allTime.setText("00:00");
                            Player.currTime.setText("00:00");
                            MainActivity.createToast(getResources().getString(R.string.conecting_to_station_title));
                            //if radio
                        } catch (Exception e) {
                            Log.d("State", e.toString());
                            MainActivity.createToast(getResources().getString(R.string.error_radio_toast));
                        }
                    }
            }
        });
        return view;
    }


    public void drag(){
        Log.d("State", "List: drag()");
        ViewPagerScroller.mScrollDuration=1500;
        MainActivity.pager.setCurrentItem(2, true);
        ViewPagerScroller.mScrollDuration=300;
    }
}
