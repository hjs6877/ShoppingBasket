package com.soom.shoppingbasket.database;

/**
 * Created by kjs on 2016-12-13.
 */

public class SQLData {
    public static final String SQL_CREATE_TABLE_CART_ITEM
            = "CREATE TABLE cart_item " +
                "(reg_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "is_checked INTEGER NOT NULL, " +
                "is_purchased INTEGER NOT NULL, " +
                "item TEXT NOT NULL)";

    public static final String SQL_INSERT_ITEM
            = "INSERT INTO cart_item " +
                "(is_checked, is_purchased, item) " +
                "VALUES (?, ?, ?)";

    public static final String SQL_UPDATE_ITEM
            = "UPDATE cart_item " +
                "SET is_checked=?, is_purchased=?, item=? " +
                "WHERE reg_id = ?";

    public static final String SQL_DELETE_ITEM
            = "DELETE FROM cart_item WHERE reg_id=?";

    public static final String SQL_SELECT_ALL_ITEM
            = "SELECT * FROM cart_item ORDER BY reg_id";
}
