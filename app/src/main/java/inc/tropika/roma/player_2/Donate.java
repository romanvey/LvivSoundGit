package inc.tropika.roma.player_2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


public class Donate extends android.support.v4.app.Fragment {
    public static RelativeLayout root;
    public static Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("State", "Donate: OnCreateView()");
        View view=inflater.inflate(R.layout.fragment_radio, container, false);
        root =(RelativeLayout)view.findViewById(R.id.root);
        btn=(Button)view.findViewById(R.id.button2);
        if(MainActivity.settings.getInt(5)==0){
            root.setBackground(getResources().getDrawable(R.drawable.lv_4));
            btn.setTypeface(MainActivity.tf);
        }
        else{
            root.setBackground(getResources().getDrawable(R.drawable.st_4));
            btn.setTypeface(MainActivity.tf);
        }

        return view;
    }
}
