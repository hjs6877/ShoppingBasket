package com.soom.shoppingbasket.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.soom.shoppingbasket.model.CartItem;

import java.util.ArrayList;

/**
 * Created by kjs on 2016-12-13.
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
        this.sqLiteDatabase.close();
    }

    public void insertData(String sql, CartItem cartItem){
        Log.d(TAG, "## insert to cart_item table.");
        Object[] sqlData = cartItem.getCartItemDataArray();
        this.sqLiteDatabase.execSQL(sql, sqlData);
    }

    public void updateIsPurchased(String sql, int isPurchased){
        Log.d(TAG, "## update isPurchased.");
        Object[] sqlData = {isPurchased};
        this.sqLiteDatabase.execSQL(sql, sqlData);
    }

    public void deleteData(String sql, int regId){
        Log.d(TAG, "## delete from cart_item table.");
        Object[] sqlData = new Object[]{regId};
        this.sqLiteDatabase.execSQL(sql, sqlData);
    }

    public ArrayList<CartItem> selectAll(String sql){
        Log.d(TAG, "## cart_item table select.");
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        Cursor results = this.sqLiteDatabase.rawQuery(sql, null);
        results.moveToFirst();

        while(!results.isAfterLast()){
            CartItem cartItem = new CartItem(
                    results.getInt(0),
                    results.getInt(1),
                    results.getInt(2),
                    results.getString(3),
                    results.getString(4),
                    results.getString(5)
            );
            cartItemList.add(cartItem);
            results.moveToNext();
        }
        results.close();
        return cartItemList;
    }

}
