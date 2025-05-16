package com.example.szonyeg_shop;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PrevCartsActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore db;
    private RecyclerView mRecycleView;
    private ArrayList<PrevCarts> itemData;
    private PrevCartsAdapter cartsAdapter;
    private RadioGroup radioGroup;
    private TextView ures;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prev_carts);

        db=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        itemData=new ArrayList<>();
        cartsAdapter=new PrevCartsAdapter(this,itemData);
        mRecycleView=findViewById(R.id.prevCartsRecyclerView);
        mRecycleView.setAdapter(cartsAdapter);
        radioGroup=findViewById(R.id.sortOptionsRadioGroup);
        ures=findViewById(R.id.emptyListView);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sortByDateRadioButton) {
                    loadPrevCarts(true);
                } else if (checkedId == R.id.sortByTotalRadioButton) {
                    loadPrevCarts(false);
                }
            }
        });

        loadPrevCarts(true);
        cartsAdapter.notifyDataSetChanged();
    }

    private void loadPrevCarts(Boolean date) {
        itemData.clear();
        CollectionReference prevCartsCollection = db.collection("cart");

        Query query;

        if (date) {
            query = prevCartsCollection.orderBy("date", Query.Direction.DESCENDING);
        } else{
            query = prevCartsCollection.orderBy("total", Query.Direction.DESCENDING);
        }

        query.whereEqualTo("user",user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                PrevCarts item=queryDocumentSnapshot.toObject(PrevCarts.class);
                itemData.add(item);
                cartsAdapter.notifyDataSetChanged();
            }
            if (itemData.isEmpty()){
                ures.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(e -> {
        });
    }

}
