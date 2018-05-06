package danscal.coffeejournals;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ListViewFragment extends Fragment {
    private static final String TAG = "ListViewFragment";

    FirebaseListAdapter mAdapter;
    ListView mListView;
    FirebaseAuth mAuth;
    DatabaseReference mRef;


    TextView name;
    TextView vibeRating;
    TextView coffeeRating;
    //TextView location;
    Button websiteBTN;
    ImageButton favoriteBTN;
    ImageView image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_coffeeshop_listview, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query query = FirebaseDatabase.getInstance().getReference().child("coffee shops");

        FirebaseListOptions<CoffeeShop> options = new FirebaseListOptions.Builder<CoffeeShop>()
                .setQuery(query, CoffeeShop.class)
                .setLayout(R.layout.list_item_coffeeshop)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new FirebaseListAdapter<CoffeeShop>(options) {
            @Override
            protected void populateView(View view, final CoffeeShop shop, int position) {
                //Set the value for the views
                name = view.findViewById(R.id.coffeeshop_name);
                vibeRating = view.findViewById(R.id.vibe_rating);
                coffeeRating = view.findViewById(R.id.coffee_rating);
                //location = view.findViewById(R.id.location);
                image = view.findViewById(R.id.logo_image);
                websiteBTN = view.findViewById(R.id.webiste_button);
                favoriteBTN = view.findViewById(R.id.favorite_button);


                name.setText(shop.getName());
                vibeRating.setText("Vibe Rating: " + shop.getVibe() + "/5");
                coffeeRating.setText("Coffee Rating: " + shop.getCoffee() + "/5");
                //location.setText(shop.getLocation());

                Glide.with(ListViewFragment.this)
                        .load(shop.imageURL)
                        .into(image);

                final String url = shop.getWebsite();

                websiteBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                favoriteBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        final String userID = user.getUid();


                        //mRef.child(userID).child("favorites").removeValue();
                        final DatabaseReference mainListener = mRef.child(userID).child("favorites");

                        mainListener.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //System.out.println(dataSnapshot.toString().contains(shop.getName()));
                                    if((dataSnapshot.toString().contains(shop.getName()) != true)){
                                        String newRef = mRef.child(userID).child("favorites").push().getKey();
                                        mRef.child(userID).child("favorites").child(newRef).setValue(new CoffeeShop(shop.getName(), shop.getCoffee(), shop.getVibe(), shop.getLocation(), shop.getWebsite(), shop.getImageURL(), newRef));
                                        Toast.makeText(getContext(), "Added to Favorites List!",
                                                Toast.LENGTH_SHORT).show();
                                        mainListener.removeEventListener(this);
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Already in Favorites List!",
                                                Toast.LENGTH_SHORT).show();
                                        mainListener.removeEventListener(this);
                                    }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }
                });



            }
        };

        mListView = (ListView) view.findViewById(R.id.coffeeListView);
        mListView.setAdapter(mAdapter);
        return view;
    }


}

