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

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private FirebaseUser user;
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

        itemData=new ArrayList<>();
        shopingItemAdatpter=new ShopingItemAdatpter(this,itemData);
        mRecycleView.setAdapter(shopingItemAdatpter);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setUpItems();

    }


    private void setUpItems(){
        itemData.add( new ShopingItem("nagy","20",R.drawable.szonyeg1));
        itemData.add( new ShopingItem("közepes","15",R.drawable.szonyeg2));
        itemData.add( new ShopingItem("kicsi","10",R.drawable.szonyeg3));
        itemData.add( new ShopingItem("pici","5",R.drawable.szonyeg4));
        itemData.add( new ShopingItem("plusz","12",R.drawable.szonyeg5));
        itemData.add( new ShopingItem("nagyon nagy","30",R.drawable.szonyeg6));

        shopingItemAdatpter.notifyDataSetChanged();
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
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
