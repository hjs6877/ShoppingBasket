package com.soom.shoppingbasket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soom.shoppingbasket.R;
import com.soom.shoppingbasket.database.DBController;
import com.soom.shoppingbasket.database.SQLData;
import com.soom.shoppingbasket.model.CartItem;
import com.soom.shoppingbasket.service.CartItemService;
import com.soom.shoppingbasket.utils.DataTypeUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kjs on 2016-12-08.
 * 아이템 데이터를 리스트뷰에 제공해주는 어댑터 클래스
 */

public class CartItemListAdapter extends BaseAdapter {
    private final int DEFAULT_ITEM_TEXT_COLOR = Color.parseColor("#000000");
    private final int CLICKED_ITEM_TEXT_COLOR = Color.parseColor("#DCDCDC");

    /**
     * 리스트 뷰에 아이템으로 표시되는 위젯 객체들을 보관하는 내부 클래스
     */
    private class ViewHolder {
        CheckBox itemCheckBox;
        TextView itemTextView;
        Button itemPurchasedButton;
    }
    private List<CartItem> cartItemList;
    private Map<Integer, CartItem> checkedItemMap;
    private LayoutInflater inflater;
    private DBController dbController;
    private CartItemService cartItemService;

    public CartItemListAdapter(Context context, List<CartItem> cartItemList, DBController dbController) {
        inflater = LayoutInflater.from(context);
        this.cartItemList = cartItemList;
        checkedItemMap = new HashMap<>();
        this.dbController = dbController;
        cartItemService = new CartItemService(dbController);
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public List<CartItem> getCartItemList(){
        return cartItemList;
    }

    /**
     * 아이템 추가
     * @param cartItem
     */
    public void addItem(CartItem cartItem){
        cartItemList.add(cartItem);
    }

    /**
     * 체크박스 선택 체크된 아이템 맵을 반환.
     *
     * @return
     */
    public Map<Integer, CartItem> getCheckedItemMap(){
        return this.checkedItemMap;
    }

    /**
     * 체크된 아이템 맵을 비운다.
     */
    public void clearCheckedItemMap(){
        this.checkedItemMap.clear();
    }

    /**
     * cartItemList에서 선택한 아이템들 제거
     *
     * @param checkedItemMap
     */
    public void removeItems(Map<Integer, CartItem> checkedItemMap){
        subtractCheckedCartItem(checkedItemMap);

        notifyDataSetChanged();
        this.clearCheckedItemMap();                                 // 체크 된 아이템들을 제거.
    }

    /**
     * 전체 아이템 목록에서 체크된 아이템을 제거한다.
     * @param checkedItemMap
     */
    private void subtractCheckedCartItem(Map<Integer, CartItem> checkedItemMap) {
        List<CartItem> checkedCartItemList = new ArrayList<>(checkedItemMap.values());
        Collection<CartItem> removedCartItemList = CollectionUtils.removeAll(cartItemList, checkedCartItemList);
        setCartItemList((List<CartItem>) removedCartItemList);      // 삭제되고 남은 아이템으로 재할당.
    }

    /**
     * 아이템의 갯수 반환. 반환 된 수만큼 실제 리스트뷰에 아이템이 표시 됨.
     * @return
     */
    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Nullable
    @Override
    public CartItem getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 리스트뷰에 표시 될 뷰그룹(아이템 포함)을 생성. 아이템의 갯수만큼 내부적으로 반복 호출 됨.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if(view == null){
            // 아이템 뷰 inflation.
            view = inflater.inflate(R.layout.item_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.itemCheckBox = (CheckBox) view.findViewById(R.id.itemChkbox);
            viewHolder.itemTextView = (TextView) view.findViewById(R.id.itemText);
            viewHolder.itemPurchasedButton = (Button) view.findViewById(R.id.itemPurchasedButton);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        setWidget(viewHolder, position);
        return view;
    }

    /**
     * 리스트 뷰에 그려질 아이템 위젯들을 셋팅한다.
     * @param viewHolder
     * @param position
     */
    private void setWidget(ViewHolder viewHolder, int position) {

        // TODO position 값도 파라미터로 넘겨서 CartItemList의 위젯 상태값을 업데이트 하도록 수정하고, 갱신해본다.
        setItemCheckBox(viewHolder, position);
        setItemTextView(viewHolder, position);
        setItemPurchasedButton(viewHolder, position);

    }

    private void setItemPurchasedButton(ViewHolder viewHolder, int position) {
        CartItem cartItem = getItem(position);
        String buttonText = getButtonText(cartItem); // TODO DB에서 가져올 때 포함 시키도록 수정 필요.
        viewHolder.itemPurchasedButton.setText(buttonText);

        viewHolder.itemPurchasedButton.setOnClickListener(new ItemPurchasedButtonClickListener(viewHolder, position));
    }

    private void setItemTextView(ViewHolder viewHolder, int position) {
        CartItem cartItem = getItem(position);
        viewHolder.itemTextView.setText(cartItem.getItemText());

        if(isPurchased(cartItem))
            viewHolder.itemTextView.setTextColor(CLICKED_ITEM_TEXT_COLOR);
        else
            viewHolder.itemTextView.setTextColor(DEFAULT_ITEM_TEXT_COLOR);
    }

    private void setItemCheckBox(ViewHolder viewHolder, int position) {
        CartItem cartItem = getItem(position);
        viewHolder.itemCheckBox.setChecked(isChecked(cartItem));

        viewHolder.itemCheckBox.setOnCheckedChangeListener(new ItemCheckedChangeListener(position));
    }

    private String getButtonText(CartItem cartItem) {
        return cartItem.isPurchased() == 1 ? "구매완료" : "구매전";
    }

    private boolean isChecked(CartItem cartItem) {
        return cartItem.isChecked() == 1 ? true : false;
    }

    private boolean isPurchased(CartItem cartItem) {
        return cartItem.isPurchased() == 1 ? true : false;
    }

    /**
     * 아이템을 체크 선택. 아이템 삭제 용도로 사용된다.
     */
    class ItemCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        private int position;

        ItemCheckedChangeListener(int position){
            this.position = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CartItem cartItem = cartItemList.get(position);
            int regId = cartItem.getRegId();

            /**
             * checked 아이템을 컬렉션에 담고, unchecked 아이템은 컬렉션에서 제거한다.
             */
            if(isChecked)
                checkedItemMap.put(regId, cartItem);
            else
                checkedItemMap.remove(regId);

            cartItemService.updateIsChecked(SQLData.SQL_UPDATE_IS_CHECKED, regId, DataTypeUtils.convertBooleanToInt(isChecked));
            cartItemList.get(position).setChecked(DataTypeUtils.convertBooleanToInt(isChecked));
        }
    }

    /**
     * 아이템 구매 버튼 클릭 리스너
     * 1. CartItem isPurchased가 false이면
     *      - isPurchased를 true로 변경
     *      - 버튼 텍스트를 '구매완료'로 변경
     *      - 버튼 색깔을 회색으로 변경.
     * 2. CartItem isPurchased가 true이면
     *      - isPurchased를 false로 변경
     *      - 버튼 텍스트를 '구매전'으로 변경
     *      - 버튼 색깔을 원래 색으로 변경.
     */
    class ItemPurchasedButtonClickListener implements View.OnClickListener{
        private ViewHolder viewHolder;
        private int position;

        public ItemPurchasedButtonClickListener(ViewHolder viewHolder, int position){
            this.viewHolder = viewHolder;
            this.position = position;
        }

        /**
         * 구매 여부에 따라 구매 상태 변경 및 텍스트 변경.
         * @param v
         */
        @Override
        public void onClick(View v) {
            CartItem cartItem = getItem(position);

            boolean isPurchased = DataTypeUtils.convertIntToBoolean(cartItem.isPurchased());
            int regId = cartItem.getRegId();
            boolean purchasedStauts;
            String buttonText;
            int buttonColor;

            if(isPurchased){
                purchasedStauts = false;
                buttonText = "구매전";
                buttonColor = DEFAULT_ITEM_TEXT_COLOR;
            }else{
                purchasedStauts = true;
                buttonText = "구매완료";
                buttonColor = Color.parseColor("#DCDCDC");
            }

            viewHolder.itemPurchasedButton.setText(buttonText);
            viewHolder.itemTextView.setTextColor(buttonColor);

            // TODO 쓰레드 전환 검토.
            cartItemService.updateIsPurchased(SQLData.SQL_UPDATE_IS_PURCHASED, regId, DataTypeUtils.convertBooleanToInt(purchasedStauts));
            cartItemList.get(position).setPurchased(DataTypeUtils.convertBooleanToInt(purchasedStauts));
        }
    }
}
