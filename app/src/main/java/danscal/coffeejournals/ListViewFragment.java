package danscal.coffeejournals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListViewFragment extends Fragment {
    private static final String TAG = "ListViewFragment";

    FirebaseListAdapter mAdapter;
    ListView mListView;
    Context mContext;

    TextView name;
    TextView vibeRating;
    TextView coffeeRating;
    TextView location;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_coffeeshop_listview, container, false);

        Query query = FirebaseDatabase.getInstance().getReference().child("coffee shops");

        FirebaseListOptions<CoffeeShop> options = new FirebaseListOptions.Builder<CoffeeShop>()
                .setQuery(query, CoffeeShop.class)
                .setLayout(R.layout.list_item_coffeeshop)
                .setLifecycleOwner(this)
                .build();

        //Finally you pass them to the constructor here:
        mAdapter = new FirebaseListAdapter<CoffeeShop>(options) {
            @Override
            protected void populateView(View view, CoffeeShop shop, int position) {
                //Set the value for the views
                name = view.findViewById(R.id.coffeeshop_name);
                vibeRating = view.findViewById(R.id.vibe_rating);
                coffeeRating = view.findViewById(R.id.coffee_rating);
                location = view.findViewById(R.id.location);


                name.setText(shop.getName());
                vibeRating.setText(shop.getVibe());
                coffeeRating.setText(shop.getCoffee());
                location.setText(shop.getLocation());
            }
        };

        mListView = (ListView) view.findViewById(R.id.coffeeListView);
        mListView.setAdapter(mAdapter);
        return view;
    }


}

