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

    // TODO 메서드 추출
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modify);
        editModifyItemText = (EditText) findViewById(R.id.editModifyItemText);

        Intent intent = getIntent();
        cartItem = (CartItem) intent.getSerializableExtra("cartItem");
        String itemText = cartItem.getItemText();
        position = intent.getIntExtra("position", 0);


        editModifyItemText.setText(itemText);
        editModifyItemText.setSelection(editModifyItemText.length());

        buttonModifyItem = (Button) findViewById(R.id.buttonModifyItem);
        buttonModifyItem.setOnClickListener(new ModifyItemTextClickListener());

        buttonCloseModifyItem = (Button) findViewById(R.id.buttonCloseModifyItem);
        buttonCloseModifyItem.setOnClickListener(new CloseModifyItemClickListener());
    }

    /**
     * 아이템 수정 버튼 클릭 리스너
     */
    private class ModifyItemTextClickListener implements View.OnClickListener {

        // TODO 메서드 추출
        @Override
        public void onClick(View v) {
            String modifiedItemText = editModifyItemText.getEditableText().toString();
            cartItem.setItemText(modifiedItemText);
            cartItemService.updateCartItem(SQLData.SQL_UPDATE_ITEM, cartItem);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("modifiedItemText", modifiedItemText);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    /**
     * 닫기 버튼 클릭 리스너
     */
    private class CloseModifyItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
