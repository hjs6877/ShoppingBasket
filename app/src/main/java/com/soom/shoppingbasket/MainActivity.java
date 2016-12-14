package com.soom.shoppingbasket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soom.shoppingbasket.adapter.CartItemListAdapter;
import com.soom.shoppingbasket.database.DBController;
import com.soom.shoppingbasket.database.SQLData;
import com.soom.shoppingbasket.model.CartItem;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 * - 아이템 삭제
 * - 아이템 수정
 * - 아이템 구매 상태 변경
 */
public class MainActivity extends AppCompatActivity {
    private ListView itemListView;
    private DBController dbController;

    private Button buttonAdd;
    private EditText editItemText;

    private List<CartItem> cartItemList;
    private CartItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbController = new DBController(this);
        dbController.openDb();
        cartItemList = dbController.selectAll(SQLData.SQL_SELECT_ALL_ITEM);
        dbController.closeDb();

        itemListView = (ListView) findViewById(R.id.itemListView);
        adapter = new CartItemListAdapter(this, R.layout.item_layout, cartItemList);
        itemListView.setAdapter(adapter);


        editItemText = (EditText) findViewById(R.id.editItemText);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new ItemAddClickListener(this));
    }


    class ItemAddClickListener implements View.OnClickListener{
        private Context context;

        public ItemAddClickListener(Context context){
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            String itemText = editItemText.getText().toString();
            if(itemText.isEmpty()){
                Toast.makeText(context, R.string.toast_no_input_item, Toast.LENGTH_SHORT).show();
            }else{
                /**
                 * TODO
                 * - DB에 insert
                 * - 리스트뷰 갱신
                 * - EditText 입력 텍스트 지우기
                 */
                // DB에 아이템 추가
                dbController.openDb();
                dbController.insertData(SQLData.SQL_INSERT_ITEM, new CartItem(0, 0, itemText));
                dbController.closeDb();

                // 리스트뷰에 아이템 추가 및 갱신
                cartItemList.add(new CartItem(0, 0, itemText));
                adapter.notifyDataSetChanged();

                // editText의 텍스트 지워서 초기화.
                editItemText.setText(null);
            }
        }
    }
}
