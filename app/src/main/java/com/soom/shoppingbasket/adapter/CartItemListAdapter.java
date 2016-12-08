package com.soom.shoppingbasket.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.soom.shoppingbasket.R;
import com.soom.shoppingbasket.model.CartItem;

import java.util.List;

/**
 * Created by kjs on 2016-12-08.
 */

public class CartItemListAdapter extends ArrayAdapter<CartItem> {
    class ViewHolder {
        public CheckBox itemCheckBox;
        public TextView itemTextView;
        public Button itemPurchasedButton;
    }
    private List<CartItem> cartItemList;
    private LayoutInflater inflater;

    public CartItemListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CartItem> cartItemList) {
        super(context, resource, cartItemList);
        inflater = LayoutInflater.from(context);
        this.cartItemList = cartItemList;
    }

    public void addItem(CartItem cartItem){
        cartItemList.add(cartItem);
    }

    // TODO 어떤 파라미터를 받아야하나
    public void removeItems(){

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public CartItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_layout, null);
            viewHolder.itemCheckBox = (CheckBox) view.findViewById(R.id.itemChkbox);
            viewHolder.itemTextView = (TextView) view.findViewById(R.id.itemText);
            viewHolder.itemPurchasedButton = (Button) view.findViewById(R.id.itemPurchasedButton);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        CartItem cartItem = getItem(position);
        viewHolder.itemCheckBox.setChecked(cartItem.isChecked());
        viewHolder.itemTextView.setText(cartItem.getItemText());
        viewHolder.itemPurchasedButton.setText(cartItem.getButtonText());
        viewHolder.itemPurchasedButton.setTag(R.string.isPurchased, cartItem.isPurchased());

        return view;
    }
}
