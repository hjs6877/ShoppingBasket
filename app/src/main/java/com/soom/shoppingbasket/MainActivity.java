package com.soom.shoppingbasket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.soom.shoppingbasket.adapter.CartItemListAdapter;
import com.soom.shoppingbasket.model.CartItem;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO DB에서 조회해서 list를 생성하는것으로 변경 해야 됨.
        List<CartItem> itemList = Arrays.asList(
                new CartItem(false, false, "구매전", "하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔하이네캔"),
                new CartItem(false, false, "구매전", "아사히"),
                new CartItem(false, false, "구매전", "바이엔슈테판"),
                new CartItem(false, false, "구매전", "기린이치방"),
                new CartItem(false, false, "구매전", "스텔라")
        );

        itemListView = (ListView) findViewById(R.id.itemListView);
        CartItemListAdapter adapter = new CartItemListAdapter(this, R.layout.item_layout, itemList);
        itemListView.setAdapter(adapter);
    }
}
