package com.example.szonyeg_shop;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.szonyeg_shop.Utils.NotiHelper;
import com.example.szonyeg_shop.Utils.PermissionUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity{

    private FirebaseUser user;
    private FirebaseFirestore db;
    private RecyclerView mRecycleView;
    private ArrayList<ShopingItem> itemData;
    private CartItemAdapter cartItemAdatpter;
    private TextView toaltAmount;
    private Toolbar toolbar;
    private Button checkOut;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        db=FirebaseFirestore.getInstance();
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

        checkOut=findViewById(R.id.buttonCheckout);
        checkOut.setOnClickListener(e->{
            saveCart();
            new NotiHelper(this).send("Sikeres megrendelés!");
        });

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
        if(PermissionUtils.isAnyLocationPermissionGranted(this)){
            return price +" Ft (20Ft szálitási költség)";
        }else {
            return price +" Ft (Szálitá nélkül)";
        }

    }

    private int getTotale(){
        int price=0;
        for (ShopingItem item : ShopActivity.cartList){
            price+=(Integer.parseInt(item.getPrice())*item.getAmount());
        }
        return price;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveCart(){
        HashMap<String,Object> cart=new HashMap<>();
        List<Map<String,Object>> savedItems = new ArrayList<>();

        for (ShopingItem item: itemData){
            HashMap<String,Object> itemToSave=new HashMap<>();
            itemToSave.put("id",item.id);
            itemToSave.put("amount",item.getAmount());
            savedItems.add(itemToSave);
        }

        cart.put("user",user.getUid());
        cart.put("items",savedItems);
        cart.put("date", Timestamp.now());
        cart.put("total",getTotale());
        db.collection("cart").add(cart).addOnSuccessListener(e->{
            animate();
        });

        itemData.clear();
        cartItemAdatpter.notifyDataSetChanged();
        updateTotalAmaount();
    }

    private void animate(){
        TextView animatedTextView=findViewById(R.id.animatedTextView);
        animatedTextView.setTextSize(24);
        animatedTextView.animate()
                .alpha(1.0f)
                .setDuration(1500)
                .start();
    }
}
