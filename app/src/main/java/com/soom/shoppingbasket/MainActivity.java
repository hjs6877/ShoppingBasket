package com.soom.shoppingbasket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * (4) 아이템 추가 시, max reg_id를 DB에서 조회한 후, 추가 할 아이템의 reg_id에 max reg_id + 1로 넣어주도록 수정.(ㅇ)
 *      - DB 스키마 수정: reg_id를 auto_increment를 제거(ㅇ)
 * (5) 아이템 2개 추가 후, 멀티 체크로 모두 지우고, 다시 2개 추가 후, 하나만 체크해서 아이템 삭제하면 2개 다 삭제되는 오류(ㅇ)
 * (6) 아이템 수정(ㅇ)
 *      - 수정 팝업 액티비티 오픈 시, 예외 발생.(ㅇ)
 * (7) delete 아이콘 표시(X)
 * (8) 런처 아이콘(X)
 * (9) 어노테이션 라이브러리 적용
 * (10) DB ORM 적용
 * (11) 코드 리팩토링
 *      - MainActivity
 *      - DBController
 *      - ItemModifyActivity
 *      - CartItemListAdapter
 *      - DB Insert, Update, Delete에 대한 예외 처리.
 */
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ITEMMODIFY = 1001;

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

        initActivity();

        cartItemList = getCartItemList();

        InitViews();

    }


    /**
     * 액티비티 초기화 작업
     */
    private void initActivity() {
        // 앱바 추가
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbController = new DBController(this);
    }

    /**
     * 아이템 조회
     *
     * @return
     */
    private List<CartItem> getCartItemList() {
        dbController.openDb();
        cartItemList = dbController.selectAll(SQLData.SQL_SELECT_ALL_ITEM);
        dbController.closeDb();
        return cartItemList;
    }

    private void InitViews() {
        // 리스트뷰에 어댑터 연결.
        itemListView = (ListView) findViewById(R.id.itemListView);
        adapter = new CartItemListAdapter(this, R.layout.item_layout, cartItemList, dbController);
        itemListView.setAdapter(adapter);

        // 아이템 입력을 위한 이벤트 리스너 등록.
        editItemText = (EditText) findViewById(R.id.editItemText);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new ItemAddClickListener(this));

        // 아이템 long click 시, 아이템 수정을 위한 리스너 등록
        itemListView.setOnItemLongClickListener(new ItemLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Map<Integer, CartItem> checkedItemMap = adapter.getCheckedItemMap();

        switch (item.getItemId()){
            /**
             * - DB에서 item 삭제.
             * - cartItemList에서 item 삭제
             * - item 갱신
             */
            case R.id.action_item_delete:
                deleteCartItem(checkedItemMap);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCartItem(Map<Integer, CartItem> checkedItemMap) {
        dbController.openDb();
        for(Map.Entry<Integer, CartItem> map : checkedItemMap.entrySet()){
            CartItem cartItem = map.getValue();
            dbController.deleteData(SQLData.SQL_DELETE_ITEM, cartItem.getRegId());
        }
        dbController.closeDb();
        adapter.removeItems(checkedItemMap);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 아이템 추가 버튼 클릭 리스너
     */
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
            String itemText = editItemText.getEditableText().toString();
            if(itemText.isEmpty()){
                Toast.makeText(context, R.string.toast_no_input_item, Toast.LENGTH_SHORT).show();
            }else{
                insertItem(itemText);
                refreshCartItems();
            }

        }

        private void insertItem(String itemText) {
            String currentDate = DateUtil.currentDateToString();
            // DB에 아이템 추가
            dbController.openDb();
            // max reg_id를 조회한다.
            int maxRegId = dbController.selectMaxRegId(SQLData.SQL_SELECT_MAX_REG_ID);

            int regId = maxRegId + 1;
            dbController.insertCartItem(SQLData.SQL_INSERT_ITEM, new CartItem(regId, 0, 0, itemText,currentDate, currentDate));
            dbController.closeDb();


            // 리스트뷰에 아이템 추가 및 갱신
            cartItemList.add(new CartItem(regId, 0, 0, itemText, currentDate, currentDate));
            Collections.sort(cartItemList, new CartItemComparator());
            adapter.notifyDataSetChanged();

            // editText의 텍스트 지워서 초기화.
            editItemText.setText(null);
        }

        // TODO 아이템 갱신에 대해서 insertItem에서 추출
        private void refreshCartItems() {
        }
    }

    /**
     * 아이템 Long Click Listener
     */
    private class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private Context context;

        public ItemLongClickListener(Context context){
            this.context = context;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("ItemLongClickListener", "long click!!");
            CartItem cartItem = cartItemList.get(position);

            Intent intent = new Intent(context, ItemModifyActivity.class);
            intent.putExtra("cartItem", cartItem);
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_CODE_ITEMMODIFY);
            return false;
        }
    }

    /**
     * 활성화 상태의 Activity로부터 응답을 받아 처리.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ITEMMODIFY){
            if(resultCode == RESULT_OK){
                String modifiedItemText = data.getExtras().getString("modifiedItemText");
                int position = data.getExtras().getInt("position");

                cartItemList.get(position).setItemText(modifiedItemText);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
