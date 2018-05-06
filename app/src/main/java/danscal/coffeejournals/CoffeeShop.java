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

public class CoffeeShop {
    public String name;
    public String coffee;
    public String vibe;
    public String location;
    public String imageURL;
    public String website;
    public String shopID;

    public CoffeeShop () {

    }

    public CoffeeShop (String name){
        this.name = name;
    }

    public CoffeeShop (String name, String coffeeRating, String vibeRating, String location, String website, String imageURL, String shopID){
        this.name = name;
        this.coffee = coffeeRating;
        this.vibe = vibeRating;
        this.location = location;
        this.website = website;
        this.imageURL = imageURL;
        this.shopID = shopID;
    }
    public String getName(){
        return name;
    }
    public String getVibe(){
        return vibe;
    }
    public String getCoffee(){
        return coffee;
    }
    public String getLocation(){
        return location;
    }
    public String getImageURL(){
        return imageURL;
    }
    public String getWebsite(){
        return website;
    }
    public String getShopID(){
        return shopID;
    }


}
