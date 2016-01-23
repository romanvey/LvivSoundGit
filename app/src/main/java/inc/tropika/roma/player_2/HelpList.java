package inc.tropika.roma.player_2;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HelpList extends android.support.v4.app.Fragment {
    public static TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7,tx8,tx9,tx10,tx11,tx12,tx13,tx14,tx15,tx16,tx17,tx18,tx19,tx20,mood;
    public static EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,ed9,ed10;
    String s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
    View view;
    SharedPreferences prefs;
    SharedPreferences.Editor ed;
    public static Button save,cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_help_list, container, false);
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        ed=prefs.edit();
        ed.apply();
        mood=(TextView)view.findViewById(R.id.textView37);
        save=(Button)view.findViewById(R.id.button);
        cancel=(Button)view.findViewById(R.id.cancel);

        tx1=(TextView)view.findViewById(R.id.textView17);
        tx2=(TextView)view.findViewById(R.id.textView18);
        tx3=(TextView)view.findViewById(R.id.textView19);
        tx4=(TextView)view.findViewById(R.id.textView20);
        tx5=(TextView)view.findViewById(R.id.textView21);
        tx6=(TextView)view.findViewById(R.id.textView22);
        tx7=(TextView)view.findViewById(R.id.textView23);
        tx8=(TextView)view.findViewById(R.id.textView24);
        tx9=(TextView)view.findViewById(R.id.textView25);
        tx10=(TextView)view.findViewById(R.id.textView26);

        tx11=(TextView)view.findViewById(R.id.textView27);
        tx12=(TextView)view.findViewById(R.id.textView28);
        tx13=(TextView)view.findViewById(R.id.textView29);
        tx14=(TextView)view.findViewById(R.id.textView30);
        tx15=(TextView)view.findViewById(R.id.textView31);
        tx16=(TextView)view.findViewById(R.id.textView32);
        tx17=(TextView)view.findViewById(R.id.textView33);
        tx18=(TextView)view.findViewById(R.id.textView34);
        tx19=(TextView)view.findViewById(R.id.textView35);
        tx20=(TextView)view.findViewById(R.id.textView36);

        ed1=(EditText)view.findViewById(R.id.editText);
        ed2=(EditText)view.findViewById(R.id.editText2);
        ed3=(EditText)view.findViewById(R.id.editText3);
        ed4=(EditText)view.findViewById(R.id.editText4);
        ed5=(EditText)view.findViewById(R.id.editText5);
        ed6=(EditText)view.findViewById(R.id.editText6);
        ed7=(EditText)view.findViewById(R.id.editText7);
        ed8=(EditText)view.findViewById(R.id.editText8);
        ed9=(EditText)view.findViewById(R.id.editText9);
        ed10=(EditText)view.findViewById(R.id.editText10);

        setTypeFace();
        singleLine();
        getData();
        listeners();
        return view;
    }

    public void singleLine(){
        save.setFocusable(true);
        cancel.setFocusable(true);
        ed1.setSingleLine(true);
        ed2.setSingleLine(true);
        ed3.setSingleLine(true);
        ed4.setSingleLine(true);
        ed5.setSingleLine(true);
        ed6.setSingleLine(true);
        ed7.setSingleLine(true);
        ed8.setSingleLine(true);
        ed9.setSingleLine(true);
        ed10.setSingleLine(true);
        ed1.setNextFocusDownId(R.id.cancel);
        ed2.setNextFocusDownId(R.id.cancel);
        ed3.setNextFocusDownId(R.id.cancel);
        ed4.setNextFocusDownId(R.id.cancel);
        ed5.setNextFocusDownId(R.id.cancel);
        ed6.setNextFocusDownId(R.id.cancel);
        ed7.setNextFocusDownId(R.id.cancel);
        ed8.setNextFocusDownId(R.id.cancel);
        ed9.setNextFocusDownId(R.id.cancel);
        ed10.setNextFocusDownId(R.id.cancel);
    }


    public void setTypeFace(){
        ed1.setTypeface(MainActivity.tf);
        ed2.setTypeface(MainActivity.tf);
        ed3.setTypeface(MainActivity.tf);
        ed4.setTypeface(MainActivity.tf);
        ed5.setTypeface(MainActivity.tf);
        ed6.setTypeface(MainActivity.tf);
        ed7.setTypeface(MainActivity.tf);
        ed8.setTypeface(MainActivity.tf);
        ed9.setTypeface(MainActivity.tf);
        ed10.setTypeface(MainActivity.tf);
        save.setTypeface(MainActivity.tf);
        cancel.setTypeface(MainActivity.tf);
        mood.setTypeface(MainActivity.tf);
        tx1.setTypeface(MainActivity.tf);
        tx2.setTypeface(MainActivity.tf);
        tx3.setTypeface(MainActivity.tf);
        tx4.setTypeface(MainActivity.tf);
        tx5.setTypeface(MainActivity.tf);
        tx6.setTypeface(MainActivity.tf);
        tx7.setTypeface(MainActivity.tf);
        tx8.setTypeface(MainActivity.tf);
        tx9.setTypeface(MainActivity.tf);
        tx10.setTypeface(MainActivity.tf);
        tx11.setTypeface(MainActivity.tf);
        tx12.setTypeface(MainActivity.tf);
        tx13.setTypeface(MainActivity.tf);
        tx14.setTypeface(MainActivity.tf);
        tx15.setTypeface(MainActivity.tf);
        tx16.setTypeface(MainActivity.tf);
        tx17.setTypeface(MainActivity.tf);
        tx18.setTypeface(MainActivity.tf);
        tx19.setTypeface(MainActivity.tf);
        tx20.setTypeface(MainActivity.tf);
    }

    public void getData(){
        s1=prefs.getString("s1","Злий");
        s2=prefs.getString("s2","Стресовий");
        s3=prefs.getString("s3","Ніякий");
        s4=prefs.getString("s4","Спокійний");
        s5=prefs.getString("s5","Звичайний");
        s6=prefs.getString("s6","Хороший");
        s7=prefs.getString("s7","Пристрасний");
        s8=prefs.getString("s8","Веселий");
        s9=prefs.getString("s9","Енергійний");
        s10=prefs.getString("s10","Для тренувань");
        tx11.setText(s1);
        tx12.setText(s2);
        tx13.setText(s3);
        tx14.setText(s4);
        tx15.setText(s5);
        tx16.setText(s6);
        tx17.setText(s7);
        tx18.setText(s8);
        tx19.setText(s9);
        tx20.setText(s10);
        ed1.setText(s1);
        ed2.setText(s2);
        ed3.setText(s3);
        ed4.setText(s4);
        ed5.setText(s5);
        ed6.setText(s6);
        ed7.setText(s7);
        ed8.setText(s8);
        ed9.setText(s9);
        ed10.setText(s10);
    }


    public void listeners(){
        tx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx11.setVisibility(View.VISIBLE);
                if(ed1.getText().toString().equals("")) {
                    tx11.setText("(1)");
                }else{
                    tx11.setText(ed1.getText().toString());
                }
                ed1.setVisibility(View.GONE);

            }
        });
        tx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx12.setVisibility(View.VISIBLE);
                if(ed2.getText().toString().equals("")) {
                    tx12.setText("(2)");
                }else{
                    tx12.setText(ed2.getText().toString());
                }
                ed2.setVisibility(View.GONE);
            }
        });
        tx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx13.setVisibility(View.VISIBLE);
                if(ed3.getText().toString().equals("")) {
                    tx13.setText("(3)");
                }else{
                    tx13.setText(ed3.getText().toString());
                }
                ed3.setVisibility(View.GONE);
            }
        });
        tx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx14.setVisibility(View.VISIBLE);
                if(ed4.getText().toString().equals("")) {
                    tx14.setText("(4)");
                }else{
                    tx14.setText(ed4.getText().toString());
                }
                ed4.setVisibility(View.GONE);
            }
        });
        tx5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx15.setVisibility(View.VISIBLE);
                if(ed5.getText().toString().equals("")) {
                    tx15.setText("(5)");
                }else{
                    tx15.setText(ed5.getText().toString());
                }
                ed5.setVisibility(View.GONE);
            }
        });
        tx6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx16.setVisibility(View.VISIBLE);
                if(ed6.getText().toString().equals("")) {
                    tx16.setText("(6)");
                }else{
                    tx16.setText(ed6.getText().toString());
                }
                ed6.setVisibility(View.GONE);
            }
        });
        tx7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx17.setVisibility(View.VISIBLE);
                if(ed7.getText().toString().equals("")) {
                    tx17.setText("(7)");
                }else{
                    tx17.setText(ed7.getText().toString());
                }
                ed7.setVisibility(View.GONE);
            }
        });
        tx8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx18.setVisibility(View.VISIBLE);
                if(ed8.getText().toString().equals("")) {
                    tx18.setText("(8)");
                }else{
                    tx18.setText(ed8.getText().toString());
                }
                ed8.setVisibility(View.GONE);
            }
        });
        tx9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx19.setVisibility(View.VISIBLE);
                if(ed9.getText().toString().equals("")) {
                    tx19.setText("(9)");
                }else{
                    tx19.setText(ed9.getText().toString());
                }
                ed9.setVisibility(View.GONE);
            }
        });
        tx10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx20.setVisibility(View.VISIBLE);
                if(ed10.getText().toString().equals("")) {
                    tx20.setText("(10)");
                }else{
                    tx20.setText(ed10.getText().toString());
                }
                ed10.setVisibility(View.GONE);
            }
        });


        tx11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx11.setVisibility(View.GONE);
                ed1.setVisibility(View.VISIBLE);
                ed1.setText(tx11.getText().toString());
            }
        });
        tx12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx12.setVisibility(View.GONE);
                ed2.setVisibility(View.VISIBLE);
                ed2.setText(tx12.getText().toString());
            }
        });
        tx13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx13.setVisibility(View.GONE);
                ed3.setVisibility(View.VISIBLE);
                ed3.setText(tx13.getText().toString());
            }
        });
        tx14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx14.setVisibility(View.GONE);
                ed4.setVisibility(View.VISIBLE);
                ed4.setText(tx14.getText().toString());
            }
        });
        tx15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx15.setVisibility(View.GONE);
                ed5.setVisibility(View.VISIBLE);
                ed5.setText(tx15.getText().toString());
            }
        });
        tx16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx16.setVisibility(View.GONE);
                ed6.setVisibility(View.VISIBLE);
                ed6.setText(tx16.getText().toString());
            }
        });
        tx17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx17.setVisibility(View.GONE);
                ed7.setVisibility(View.VISIBLE);
                ed7.setText(tx17.getText().toString());
            }
        });
        tx18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx18.setVisibility(View.GONE);
                ed8.setVisibility(View.VISIBLE);
                ed8.setText(tx18.getText().toString());
            }
        });
        tx19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx19.setVisibility(View.GONE);
                ed9.setVisibility(View.VISIBLE);
                ed9.setText(tx19.getText().toString());
            }
        });
        tx20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx20.setVisibility(View.GONE);
                ed10.setVisibility(View.VISIBLE);
                ed10.setText(tx20.getText().toString());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("")){
                    ed.putString("s1","(1)");
                }else {
                    ed.putString("s1", ed1.getText().toString());
                }
                if(ed2.getText().toString().equals("")){
                    ed.putString("s2","(2)");
                }else {
                    ed.putString("s2", ed2.getText().toString());
                }
                if(ed3.getText().toString().equals("")){
                    ed.putString("s3","(3)");
                }else {
                    ed.putString("s3", ed3.getText().toString());
                }
                if(ed4.getText().toString().equals("")){
                    ed.putString("s4","(4)");
                }else {
                    ed.putString("s4", ed4.getText().toString());
                }
                if(ed5.getText().toString().equals("")){
                    ed.putString("s5","(5)");
                }else {
                    ed.putString("s5", ed5.getText().toString());
                }
                if(ed6.getText().toString().equals("")){
                    ed.putString("s6","(6)");
                }else {
                    ed.putString("s6", ed6.getText().toString());
                }
                if(ed7.getText().toString().equals("")){
                    ed.putString("s7","(7)");
                }else {
                    ed.putString("s7", ed7.getText().toString());
                }
                if(ed8.getText().toString().equals("")){
                    ed.putString("s8","(8)");
                }else {
                    ed.putString("s8", ed8.getText().toString());
                }
                if(ed9.getText().toString().equals("")){
                    ed.putString("s9","(9)");
                }else {
                    ed.putString("s9", ed9.getText().toString());
                }
                if(ed10.getText().toString().equals("")){
                    ed.putString("s10","(10)");
                }else {
                    ed.putString("s10", ed10.getText().toString());
                }
                ed.apply();
                try {
                    InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    Log.d("State", "Keyboard hidden");
                } catch (Exception e) {
                    Log.d("State", "Error with keyboard: "+e.toString());
                }
                MainActivity.hint.setVisible(true);
                if(MainActivity.PAGE==2){
                    MainActivity.gest.setVisible(true);
                    MainActivity.save.setVisible(true);
                }else{
                    MainActivity.gest.setVisible(false);
                    MainActivity.save.setVisible(false);
                }
                MainActivity.child.setVisibility(View.VISIBLE);
                MainActivity.fr=getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity.fr.remove(MainActivity.hl);
                MainActivity.fr.commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    Log.d("State", "Keyboard hidden");
                } catch (Exception e) {
                    Log.d("State", "Error with keyboard: "+e.toString());
                }
                if(MainActivity.PAGE==0){
                    MainActivity.done.setVisible(false);
                    MainActivity.hint.setVisible(true);
                    MainActivity.setts.setVisible(true);
                    MainActivity.sleep.setVisible(true);
                    MainActivity.save.setVisible(false);
                    MainActivity.gest.setVisible(false);
                }if(MainActivity.PAGE==1){
                    MainActivity.done.setVisible(false);
                    MainActivity.hint.setVisible(true);
                    MainActivity.setts.setVisible(true);
                    MainActivity.sleep.setVisible(true);
                    MainActivity.save.setVisible(false);
                    MainActivity.gest.setVisible(false);
                }if(MainActivity.PAGE==2){
                    MainActivity.done.setVisible(false);
                    MainActivity.hint.setVisible(true);
                    MainActivity.setts.setVisible(true);
                    MainActivity.sleep.setVisible(true);
                    MainActivity.gest.setVisible(true);
                    MainActivity.save.setVisible(true);
                }if(MainActivity.PAGE==3){
                    MainActivity.done.setVisible(false);
                    MainActivity.hint.setVisible(false);
                    MainActivity.gest.setVisible(false);
                    MainActivity.sleep.setVisible(true);
                    MainActivity.setts.setVisible(true);
                    MainActivity.save.setVisible(false);
                }
                MainActivity.child.setVisibility(View.VISIBLE);
                MainActivity.fr=getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity.fr.remove(MainActivity.hl);
                MainActivity.fr.commit();
            }
        });
    }

}
