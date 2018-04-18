package danscal.coffeejournals;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DanielScal on 4/16/18.
 */

public class CoffeeShop {
    public String name;
    public String coffeeRating;
    public String vibeRating;
    public String location;

    public CoffeeShop () {

    }
    public CoffeeShop (String name, String coffeeRating, String vibeRating, String location){
        this.name = name;
        this.coffeeRating = coffeeRating;
        this.vibeRating = vibeRating;
        this.location = location;
    }
    public String getName(){
        return name;
    }
    public String getVibe(){
        return vibeRating;
    }
    public String getCoffee(){
        return coffeeRating;
    }
    public String getLocation(){
        return location;
    }


    /*FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("coffee shops");

    public ArrayList<CoffeeShop> getCoffeeShopsFromDatabase(DatabaseReference mRef, Context context){
        ArrayList<CoffeeShop> shops = new ArrayList<>();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            CoffeeShop coffeeShop = new CoffeeShop();
                            coffeeShop.name = snapshot.child("name").getValue().toString();
                            coffeeShop.coffeeRating = (String) snapshot.child("cofee").getValue();
                            coffeeShop.vibeRating = snapshot.child("vibe").getValue().toString();
                            coffeeShop.location = snapshot.child("location").getValue().toString();
                            shops.add(coffeeShop);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                    public ArrayList<CoffeeShop> getList () {
                        ArrayList<CoffeeShop> shoplist = shops;
                        return shoplist;
                    }
                });


        return shops;
    }*/
    public static ArrayList<CoffeeShop> getShopFromFile (String filename, Context context) {
        ArrayList<CoffeeShop> shopList = new ArrayList<>();

        try {
            String jsonString = loadJsonFromAsset("coffee-jounrals-export.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray shops = json.getJSONArray("coffee shops");

            for (int i = 0; i < shops.length(); i++){
                CoffeeShop shop = new CoffeeShop();

                shop.name = shops.getJSONObject(i).getString("name");
                shop.vibeRating = shops.getJSONObject(i).getString("vibe");
                shop.coffeeRating = shops.getJSONObject(i).getString("coffee");
                shop.location = shops.getJSONObject(i).getString("location");

                shopList.add(shop);

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return shopList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
