package inc.tropika.roma.player_2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drag();
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
