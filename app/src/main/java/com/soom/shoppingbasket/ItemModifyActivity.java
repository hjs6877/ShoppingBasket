package com.soom.shoppingbasket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.soom.shoppingbasket.database.DBController;
import com.soom.shoppingbasket.database.SQLData;
import com.soom.shoppingbasket.model.CartItem;
import com.soom.shoppingbasket.service.CartItemService;

/**
 * 아이템 수정 팝업 액티비티. 메인 액티비티에서 아이템을 long click하면 오픈 되는 액티비티 임.
 */
public class ItemModifyActivity extends AppCompatActivity {
    private EditText editModifyItemText;
    private Button buttonModifyItem;
    private Button buttonCloseModifyItem;
    private CartItem cartItem;
    private DBController dbController;
    private CartItemService cartItemService;

    private int position;

    public ItemModifyActivity() {
        dbController = new DBController(this);
        cartItemService = new CartItemService(dbController);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modify);

        initViews();
    }

    /**
     * 화면에 표시되는 뷰에 대한 초기화 작업을 진행한다.
     */
    private void initViews(){
        Intent intent = getIntent();
        cartItem = (CartItem) intent.getSerializableExtra("cartItem");
        String itemText = cartItem.getItemText();
        position = intent.getIntExtra("position", 0);
        editModifyItemText = (EditText) findViewById(R.id.editModifyItemText);
        editModifyItemText.setText(itemText);
        editModifyItemText.setSelection(editModifyItemText.length());

        buttonModifyItem = (Button) findViewById(R.id.buttonModifyItem);
        buttonModifyItem.setOnClickListener(new ItemTextModifyButtonClickListener());

        buttonCloseModifyItem = (Button) findViewById(R.id.buttonCloseModifyItem);
        buttonCloseModifyItem.setOnClickListener(new ItemModifyCloseButtonClickListener());
    }

    /**
     * 아이템 수정 버튼 클릭 리스너
     */
    private class ItemTextModifyButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            updateCartItem();
            setResultData();
            finish();
        }

        private void updateCartItem(){
            String modifiedItemText = editModifyItemText.getEditableText().toString();
            cartItem.setItemText(modifiedItemText);
            cartItemService.updateCartItem(SQLData.SQL_UPDATE_ITEM, cartItem);
        }

        /**
         * 수정된 아이템 텍스트를 리스트뷰에 반영하기 위한 데이터를 setting한다.
         */
        private void setResultData(){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("modifiedItemText", cartItem.getItemText());
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
        }
    }

    /**
     * 닫기 버튼 클릭 리스너
     */
    private class ItemModifyCloseButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
