package com.soom.shoppingbasket.model;

/**
 * Created by kjs on 2016-12-08.
 */

public class CartItem {
    private boolean isChecked;
    private boolean isPurchased;
    private String buttonText;
    private String itemText;

    public CartItem(boolean isChecked, boolean isPurchased, String buttonText, String itemText) {
        this.buttonText = buttonText;
        this.isChecked = isChecked;
        this.isPurchased = isPurchased;
        this.itemText = itemText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
