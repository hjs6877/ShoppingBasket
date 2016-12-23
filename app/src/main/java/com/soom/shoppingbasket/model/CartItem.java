package com.soom.shoppingbasket.model;

import java.util.Date;

/**
 * Created by kjs on 2016-12-08.
 */

public class CartItem {
    private int regId;
    private int isChecked;
    private int isPurchased;
    private String itemText;
    private String createDate;
    private String updateDate;

    public CartItem(int isChecked, int isPurchased, String itemText,
                    String createDate, String updateDate) {
        this.isChecked = isChecked;
        this.isPurchased = isPurchased;
        this.itemText = itemText;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public CartItem(int regId, int isChecked, int isPurchased,
                    String itemText, String createDate, String updateDate) {
        this.regId = regId;
        this.isChecked = isChecked;
        this.isPurchased = isPurchased;
        this.itemText = itemText;
        this.createDate = createDate;
        this.updateDate = updateDate;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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
