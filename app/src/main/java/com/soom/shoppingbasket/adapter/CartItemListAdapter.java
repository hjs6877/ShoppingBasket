package com.soom.shoppingbasket.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
    private Context context;

    public CartItemListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CartItem> cartItemList) {
        super(context, resource, cartItemList);
        this.context = context;
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
        boolean isChecked = cartItem.isChecked() == 1 ? true : false;
        boolean isPurchased = cartItem.isPurchased() == 1 ? true : false;
        String buttonText = cartItem.isPurchased() == 1 ? "구매완료" : "구매전";

        viewHolder.itemCheckBox.setChecked(isChecked);
        viewHolder.itemTextView.setText(cartItem.getItemText());
        viewHolder.itemPurchasedButton.setText(buttonText);
        viewHolder.itemPurchasedButton.setTag(R.string.isPurchased, isPurchased);

        viewHolder.itemPurchasedButton.setOnClickListener(new ItemPurchasedButtonClickListener());
        return view;
    }

    class ItemPurchasedButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            /**
             * TODO DB 데이터 업데이트가 완료되고 난 후, 아래 작업 진행.
             * 1. CartItem isPurchased가 false이면
             *      - isPurchased를 true로 변경
             *      - 버튼 텍스트를 '구매완료'로 변경
             *      - 버튼 색깔을 회색으로 변경.
             * 2. CartItem isPurchased가 true이면
             *      - isPurchased를 false로 변경
             *      - 버튼 텍스트를 '구매전'으로 변경
             *      - 버튼 색깔을 원래 색으로 변경.
             */
            boolean isPurchased = (boolean) v.getTag(R.string.isPurchased);
            if(isPurchased){
                button.setTag(R.string.isPurchased, false);
                button.setText("구매전");
                // TODO 버튼 색깔.
            }else{
                button.setTag(R.string.isPurchased, true);
                button.setText("구매완료");
                // TODO 버튼 색깔.
            }
        }
    }
}
