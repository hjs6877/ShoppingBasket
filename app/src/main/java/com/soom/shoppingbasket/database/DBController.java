package com.soom.shoppingbasket.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.soom.shoppingbasket.model.CartItem;

import java.util.ArrayList;

/**
 * SQLite Database 컨트롤러
 */

public class DBController {
    public static final String TAG = "DBController";
    private final String DB_NAME = "db_shopping_basket";
    private final int DB_VERSION = 3;

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private OpenHelper openHelper;

    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TAG", "### create table");
            db.execSQL(SQLData.SQL_CREATE_TABLE_CART_ITEM);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public DBController(Context context){
        this.context = context;
        this.openHelper = new OpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void openDb(){
        Log.d(TAG, "## DB open.");
        this.sqLiteDatabase = openHelper.getWritableDatabase();
    }

    public void closeDb(){
        Log.d(TAG, "## DB closed.");
        this.sqLiteDatabase.close();
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
