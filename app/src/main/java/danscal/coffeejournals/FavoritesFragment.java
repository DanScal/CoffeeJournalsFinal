package danscal.coffeejournals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import static com.google.android.gms.cast.framework.media.MediaUtils.getImageUri;

public class FavoritesFragment extends Fragment {
    private static final String TAG = "FavoritesFragment";
    FirebaseListAdapter mAdapter;
    ListView mListView;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    StorageReference mStorageRef;

    TextView name;
    TextView vibeRating;
    TextView coffeeRating;
    //TextView location;
    Button websiteBTN;
    ImageView image;
    ImageButton removeBTN;
    ImageButton cameraBTN;
    Context mContext;

    ProgressDialog mProgressDialog;

    ImageView pic;

    public static final int CAMERA_REQUEST = 1888;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        mContext = getContext();
        mProgressDialog = new ProgressDialog(getContext());


        Query query = mRef.child(userID).child("favorites");

        FirebaseListOptions<CoffeeShop> options = new FirebaseListOptions.Builder<CoffeeShop>()
                .setQuery(query, CoffeeShop.class)
                .setLayout(R.layout.favorites_list_item_coffeeshop)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new FirebaseListAdapter<CoffeeShop>(options) {
            @Override
            protected void populateView(View view, final CoffeeShop shop, int position) {
                FirebaseUser user = mAuth.getCurrentUser();
                final String userID = user.getUid();

                name = view.findViewById(R.id.coffeeshop_name_favorites);
                vibeRating = view.findViewById(R.id.vibe_rating_favorites);
                coffeeRating = view.findViewById(R.id.coffee_rating_favorites);
                //location = view.findViewById(R.id.location);
                image = view.findViewById(R.id.logo_image_favorites);
                websiteBTN = view.findViewById(R.id.website_button_favorites);
                removeBTN = view.findViewById(R.id.imageButton_favorites);


                name.setText(shop.getName());
                vibeRating.setText("Vibe Rating: " + shop.getVibe() + "/5");
                coffeeRating.setText("Coffee Rating: " + shop.getCoffee() + "/5");
                //location.setText(shop.getLocation());


                Glide.with(FavoritesFragment.this)
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

                removeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mRef.child(userID).child("favorites").orderByChild("name").equalTo(shop.getName()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mRef.child(userID).child("favorites").child(shop.getShopID()).removeValue();
                                Toast.makeText(getContext(), "Removed From Favorites List!",
                                        Toast.LENGTH_SHORT).show();
                                mRef.child(userID).child("favorites").removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                });
            }
        };
        mListView = (ListView) view.findViewById(R.id.fragment_list_view);
        mListView.setAdapter(mAdapter);
        return view;
    }

}



    //Attempted 'create your own coffeeshop picture'

    /*mStorageRef.child("users/").child(userID + "/").child("image.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(FavoritesFragment.this)
                                    .load(uri)
                                    .into(image);
                        }
                    });

                    final String shopName = shop.name;
                    mRef.child(userID).child("favorites").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                                System.out.println(uniqueKeySnapshot.getKey());
                                for(DataSnapshot shopSnapshot : uniqueKeySnapshot.getChildren()){
                                    //System.out.println(shopSnapshot.getKey());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
            //if the request code is camera request
            if (requestCode == CAMERA_REQUEST) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //pic.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));


                mProgressDialog.setMessage("Uploading...");
                mProgressDialog.show();


                StorageReference filePath = mStorageRef.child("users/").child(userID + "/").child("image.jpg");

                UploadTask uploadTask = filePath.putFile(tempUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgressDialog.dismiss();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }
        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

        public String getRealPathFromURI(Uri uri) {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }*/

    /*cameraBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //mProgressDialogue.setMessage("Uploading...");
                        //mProgressDialogue.show();
                        //send an intent
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //You need to check in mediastore for the list of codes
                        //start activity
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        Uri uri = cameraIntent.getData();
                    }
                });*/

