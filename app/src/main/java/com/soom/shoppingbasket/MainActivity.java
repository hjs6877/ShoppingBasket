package com.soom.shoppingbasket;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soom.shoppingbasket.adapter.CartItemListAdapter;
import com.soom.shoppingbasket.comparator.CartItemComparator;
import com.soom.shoppingbasket.database.DBController;
import com.soom.shoppingbasket.database.SQLData;
import com.soom.shoppingbasket.model.CartItem;
import com.soom.shoppingbasket.utils.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * TODO
 * (1) 아이템 추가 영역의 배경 설정.(ㅇ)
 * (2) 아이템 추가 시, 추가 한 아이템이 리스트 상단에 보이도록 리스트 정렬(ㅇ)
 *      - 테이블에 create_date, update_date 컬럼 추가(ㅇ)
 *      - apk를 지우고 실행을 다시 해본다.(ㅇ)
 *      - SQLite DB 브라우저에서 확인(ㅇ)
 * (3) 아이템 구매 상태 변경(ㅇ)
 *      - 버튼 누른 아이템의 상태만 DB에 업데이트 되는지 확인(ㅇ)
 * (4) 아이템 추가 및 삭제 후, DB에서 데이터 가져와서 어댑터 리스트에 넣은 후에 notifychange 하도록 수정(X)
 * (4) delete 아이콘 표시(X)
 * (5) 아이템 수정(X)
 * (6) 런처 아이콘(X)
 */
public class MainActivity extends AppCompatActivity {
    private ListView itemListView;
    private DBController dbController;

    private Button buttonAdd;
    private EditText editItemText;

    private List<CartItem> cartItemList;
    private CartItemListAdapter adapter;

    private class ItemAddClickListener implements View.OnClickListener{
        private Context context;

        public ItemAddClickListener(Context context){
            this.context = context;
        }

        /**
         * DB에 아이템 저장.
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            insertItem();
        }

        private void insertItem() {
            String itemText = editItemText.getEditableText().toString();
            if(itemText.isEmpty()){
                Toast.makeText(context, R.string.toast_no_input_item, Toast.LENGTH_SHORT).show();
            }else{
                String currentDate = DateUtil.currentDateToString();
                // DB에 아이템 추가
                dbController.openDb();
                dbController.insertData(SQLData.SQL_INSERT_ITEM, new CartItem(0, 0, itemText,currentDate, currentDate));
                dbController.closeDb();

                // 리스트뷰에 아이템 추가 및 갱신
                cartItemList.add(new CartItem(0, 0, itemText, currentDate, currentDate));
                Collections.sort(cartItemList, new CartItemComparator());
                adapter.notifyDataSetChanged();

                // editText의 텍스트 지워서 초기화.
                editItemText.setText(null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 앱바 추가
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 아이템 조회
        dbController = new DBController(this);
        dbController.openDb();
        cartItemList = dbController.selectAll(SQLData.SQL_SELECT_ALL_ITEM);
        dbController.closeDb();

        // 리스트뷰에 어댑터 연결.
        itemListView = (ListView) findViewById(R.id.itemListView);
        adapter = new CartItemListAdapter(this, R.layout.item_layout, cartItemList, dbController);
        itemListView.setAdapter(adapter);

        // 아이템 입력을 위한 이벤트 리스너 등록.
        editItemText = (EditText) findViewById(R.id.editItemText);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new ItemAddClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Map<Integer, CartItem> checkedItemMap = adapter.getCheckedItemMap();
        List<CartItem> checkedCartItemList = new ArrayList<>();
        for(Map.Entry<Integer, CartItem> map : checkedItemMap.entrySet()){
            CartItem cartItem = map.getValue();
            checkedCartItemList.add(cartItem);
        }

        switch (item.getItemId()){
            /**
             * - DB에서 item 삭제.
             * - cartItemList에서 item 삭제
             * - item 갱신
             */
            case R.id.action_item_delete:
                DBController dbController = new DBController(this);
                dbController.openDb();
                for(CartItem cartItem : checkedCartItemList){
                    Toast.makeText(this, "regId: " + cartItem.getRegId() + ", item: " + cartItem.getItemText(), Toast.LENGTH_LONG).show();
                    dbController.deleteData(SQLData.SQL_DELETE_ITEM, cartItem.getRegId());
                }
                dbController.closeDb();
                adapter.removeItems(checkedCartItemList);
                adapter.notifyDataSetChanged();
//                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
