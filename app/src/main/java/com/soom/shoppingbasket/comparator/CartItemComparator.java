package com.soom.shoppingbasket.comparator;

import com.soom.shoppingbasket.model.CartItem;

import java.util.Comparator;

/**
 * Created by kjs on 2016-12-23.
 */

public class CartItemComparator implements Comparator<CartItem> {
    @Override
    public int compare(CartItem o1, CartItem o2) {

        return o2.getCreateDate().compareTo(o1.getCreateDate());
    }
}
