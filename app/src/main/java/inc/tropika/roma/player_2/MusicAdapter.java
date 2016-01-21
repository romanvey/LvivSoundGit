package inc.tropika.roma.player_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    MusicAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("State", "MusicAdapter constructor");
    }


    @Override
    public int getCount() {
        //Log.d("State", "MusicAdapter: getCount()"); //very often
        return MainActivity.ids.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d("State", "MusicAdapter: getItem()");
        return MainActivity.ids.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d("State", "MusicAdapter: getItemId()");
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      //Log.d("State", "MusicAdapter: getView()"); //very often
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        TextView title = (TextView)view.findViewById(R.id.textView7);
        title.setTypeface(MainActivity.tf);
        title.setText(MainActivity.ids.get(position));
        return view;
    }
}
