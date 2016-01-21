package inc.tropika.roma.player_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper implements BaseColumns {


    public static final String SETTINGS="settings";
    public static final String F1="f1";
    public static final String F2="f2";
    public static final String F3="f3";
    public static final String F4="f4";
    public static final String MISTAKE="mistake";
    public static final String FIRST_START="first";

    public static final String TABLE="mytable";
    public static final String IDS="ids";
    public static final String TITLES="titles";
    public static final String PATHS="paths";
    public static final String MOOD="mood";

    public static final String BD="myDB";

    public DBHelper(Context context) {
        super(context,BD,null,1);
        Log.d("State", "DBHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("State", "DBHelper: OnCreate()");
        db.execSQL("CREATE TABLE "+TABLE+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+IDS+" INTEGER, "+TITLES+" TEXT, "+PATHS+" TEXT, "+MOOD+" INTEGER DEFAULT 101);");
        db.execSQL("CREATE TABLE "+SETTINGS+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+MISTAKE+" INTEGER DEFAULT 10, "+F1+" INTEGER DEFAULT 1, "+F2+" INTEGER DEFAULT 1, "+F3+" INTEGER DEFAULT 1, "+F4+" INTEGER DEFAULT 1, "+FIRST_START+" INTEGER DEFAULT 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("State", "DBHelper: OnUpgrade()");
    }
}
