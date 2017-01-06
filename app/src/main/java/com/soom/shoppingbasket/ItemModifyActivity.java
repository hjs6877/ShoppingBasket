package com.soom.shoppingbasket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class ItemModifyActivity extends AppCompatActivity {
    private EditText editModifyItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modify);
        editModifyItemText = (EditText) findViewById(R.id.editModifyItemText);

        Intent intent = getIntent();
        int regId = intent.getIntExtra("regId", 0);
        String itemText = intent.getStringExtra("itemText");
        int position = intent.getIntExtra("position", 0);


        editModifyItemText.setText(itemText);
        editModifyItemText.setSelection(editModifyItemText.length());
        /**
         * TODO
         * 1. EditText에 수정할 아이템 표시.(ㅇ)
         * 2. 수정된 아이템을 DB에 업데이트.
         * 3. 응답 데이터로 수정된 아이템 텍스트와 position을 전송.
         * 4. MainActivity에서 cartItemList의 cartItem 객체의 아이템 텍스트를 수정 저장 및 NotifyChanged.
         */
    }
}
