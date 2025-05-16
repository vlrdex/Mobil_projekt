package com.example.szonyeg_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private CollectionReference items;
    private RecyclerView mRecycleView;
    private ArrayList<ShopingItem> itemData;
    private ShopingItemAdatpter shopingItemAdatpter;

    public static ArrayList<ShopingItem> cartList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            finish();
        }

        mRecycleView=findViewById(R.id.recyclerViewProducts);
        db=FirebaseFirestore.getInstance();

        itemData=new ArrayList<>();
        shopingItemAdatpter=new ShopingItemAdatpter(this,itemData);
        mRecycleView.setAdapter(shopingItemAdatpter);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        items=db.collection("items");
        setUpItems();

    }


    private void setUpItems(){
        itemData.clear();
        items.whereGreaterThan("amount",0).orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(e->{
            for (QueryDocumentSnapshot b:e){
                ShopingItem item=b.toObject(ShopingItem.class);
                item.id=b.getId();
                itemData.add(item);
                shopingItemAdatpter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                shopingItemAdatpter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.cart){
            Intent intent = new Intent(this,CartActivity.class);
            startActivity(intent);
        } else if (id==R.id.log_out_button) {
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }else if(id==R.id.profile){
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.prev_carts) {
            Intent intent=new Intent(this,PrevCartsActivity.class);
            startActivity(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
