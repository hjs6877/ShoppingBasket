package com.soom.shoppingbasket.model;

/**
 * Created by kjs on 2016-12-08.
 */

public class CartItem {
    private int regId;
    private int isChecked;
    private int isPurchased;
    private String itemText;

    public CartItem(int isChecked, int isPurchased, String itemText) {
        this.isChecked = isChecked;
        this.isPurchased = isPurchased;
        this.itemText = itemText;
    }

    public CartItem(int regId, int isChecked, int isPurchased, String itemText) {
        this.regId = regId;
        this.isChecked = isChecked;
        this.isPurchased = isPurchased;
        this.itemText = itemText;
    }

    public int getRegId(){
        return regId;
    }

    public int isChecked() {
        return isChecked;
    }

    public void setChecked(int checked) {
        isChecked = checked;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int isPurchased() {
        return isPurchased;
    }

    public void setPurchased(int purchased) {
        isPurchased = purchased;
    }

    public Object[] getCartItemDataArray(){
        Object[] cartItemData = {
                this.isChecked,
                this.isPurchased,
                this.itemText
        };

        return cartItemData;
    }
}
