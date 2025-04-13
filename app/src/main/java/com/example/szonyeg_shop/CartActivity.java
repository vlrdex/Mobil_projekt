package com.example.szonyeg_shop;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity{

    private FirebaseUser user;
    private RecyclerView mRecycleView;
    private ArrayList<ShopingItem> itemData;
    private CartItemAdapter cartItemAdatpter;
    private TextView toaltAmount;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            finish();
        }

        itemData=ShopActivity.cartList;
        System.out.println(ShopActivity.cartList);

        mRecycleView=findViewById(R.id.recyclerViewCart);

        cartItemAdatpter =new CartItemAdapter(this,itemData,this);
        mRecycleView.setAdapter(cartItemAdatpter);

        toaltAmount=findViewById(R.id.textViewTotalAmount);
        updateTotalAmaount();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_menu, menu);
        return true;
    }

    public void updateTotalAmaount(){
        toaltAmount.setText(calculateTotalPrice());
    }

    private String calculateTotalPrice(){
        int price=0;
        for (ShopingItem item : ShopActivity.cartList){
            price+=(Integer.parseInt(item.getPrice())*item.getAmount());
        }
        return price +" Ft";
    }
}
