package danscal.coffeejournals;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by DanielScal on 4/16/18.
 */

public class CoffeeShipListViewActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    //DatabaseReference mRef = mDatabase.getReference("coffee shops").child("0").child("name");
    Query query = FirebaseDatabase.getInstance().getReference().child("coffee shops").child("0").child("name");
    FirebaseListAdapter mAdapter;
    ListView mListView;
    Context mContext;

    TextView name;
    TextView vibeRating;
    TextView coffeeRating;
    TextView location;
    TextView nameTextView;
    ArrayList<CoffeeShop> shopList;
    CoffeeShopAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffeeshop_listview);
        mContext = this;
        /*name = findViewById(R.id.coffeeshop_name);
        vibeRating = findViewById(R.id.vibe_rating);
        coffeeRating = findViewById(R.id.coffee_rating);
        location = findViewById(R.id.location);*/

        /*FirebaseListOptions<CoffeeShop> options = new FirebaseListOptions.Builder<CoffeeShop>()
                .setQuery(query, CoffeeShop.class)
                .setLayout(R.layout.activity_coffeeshop_listview)
                .setLifecycleOwner(this)
                .build();

        //Finally you pass them to the constructor here:
        mAdapter = new FirebaseListAdapter<CoffeeShop>(options) {
            @Override
            protected void populateView(View view, CoffeeShop shop, int position) {
                System.out.println("XXXXXX");
                //Set the value for the views
                name = view.findViewById(R.id.coffeeshop_name);
                name.setText(shop.toString());
                System.out.println("XXXXXX");
                System.out.println(shop);
            }
        };

        mListView = (ListView) findViewById(R.id.coffeeListView);
        mListView.setAdapter(mAdapter);*/

        shopList = CoffeeShop.getShopFromFile("coffee-jounrals-export.JSON", this);
        adapter = new CoffeeShopAdapter(this, shopList);
        mListView = findViewById(R.id.coffeeListView);
        mListView.setAdapter(adapter);





    }

   /* @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }*/

}
