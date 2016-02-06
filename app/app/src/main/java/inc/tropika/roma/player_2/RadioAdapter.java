package inc.tropika.roma.player_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class RadioAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    RadioAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("State", "RadioAdapter constructor");
    }


    @Override
    public int getCount() {
        //Log.d("State", "RadioAdapter: getCount()"); //very often
        return MainActivity.radio_titles.length;
    }

    @Override
    public Object getItem(int position) {
        Log.d("State", "RadioAdapter: getItem()");
        return MainActivity.radio_titles[position];
    }

    @Override
    public long getItemId(int position) {
        Log.d("State", "RadioAdapter: getItemId()");
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        TextView title = (TextView)view.findViewById(R.id.textView7);
        title.setTypeface(MainActivity.tf);
        title.setText(MainActivity.radio_titles[position]);
        return view;
    }
}

