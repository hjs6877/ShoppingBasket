package com.soom.shoppingbasket.service;

import android.database.Cursor;
import android.util.Log;

import com.soom.shoppingbasket.database.DBController;
import com.soom.shoppingbasket.model.CartItem;

import java.util.ArrayList;

/**
 * Created by kjs on 2017-01-13.
 */

public class CartItemService {
    private final String TAG = "CartItemService";
    private DBController dbController;

    public CartItemService(DBController dbController){
        this.dbController = dbController;
    }

    public void insertCartItem(String sql, CartItem cartItem){
        Log.d(TAG, "## insert to cart item.");
        Object[] sqlData = cartItem.getCartItemDataArray();
        dbController.openDb();
        dbController.getSqLiteDatabase().execSQL(sql, sqlData);
        dbController.closeDb();
    }

    public void updateIsPurchased(String sql, int regId, int isPurchased){
        Log.d(TAG, "## update isPurchased.");
        Object[] sqlData = {isPurchased, regId};
        dbController.openDb();
        dbController.getSqLiteDatabase().execSQL(sql, sqlData);
        dbController.closeDb();
    }

    public void updateIsChecked(String sql, int regId, int isChecked){
        Log.d(TAG, "## update isChecked.");
        Object[] sqlData = {isChecked, regId};
        dbController.openDb();
        dbController.getSqLiteDatabase().execSQL(sql, sqlData);
        dbController.closeDb();
    }

    public void updateCartItem(String sql, CartItem cartItem){
        Log.d(TAG, "## update cart item.");
        Object[] sqlData = {
                cartItem.isChecked(),
                cartItem.isPurchased(),
                cartItem.getItemText(),
                cartItem.getRegId()
        };
        dbController.openDb();
        dbController.getSqLiteDatabase().execSQL(sql, sqlData);
        dbController.closeDb();
    }

    public void deleteData(String sql, int regId){
        Log.d(TAG, "## delete from cart_item table.");
        Object[] sqlData = new Object[]{regId};
        dbController.openDb();
        dbController.getSqLiteDatabase().execSQL(sql, sqlData);
        dbController.closeDb();
    }

    public ArrayList<CartItem> selectAll(String sql){
        dbController.openDb();
        Log.d(TAG, "## cart_item table select.");
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        Cursor results = dbController.getSqLiteDatabase().rawQuery(sql, null);
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
        dbController.closeDb();
        return cartItemList;
    }

    public int selectMaxRegId(String sql){
        dbController.openDb();
        Log.d(TAG, "## select max reg_id.");
        int maxRegId;
        Cursor results = dbController.getSqLiteDatabase().rawQuery(sql, null);
        results.moveToFirst();
        maxRegId = results.getInt(0);
        results.close();
        dbController.closeDb();
        return maxRegId;
    }

}
