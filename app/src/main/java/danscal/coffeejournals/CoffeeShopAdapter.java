package danscal.coffeejournals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by DanielScal on 4/16/18.
 */

public class CoffeeShopAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CoffeeShop> mCoffeeShopList;
    private LayoutInflater mInflater;

    public CoffeeShopAdapter(Context mContext, ArrayList<CoffeeShop> mCoffeeShopList) {
        this.mContext = mContext;
        this.mCoffeeShopList = mCoffeeShopList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCoffeeShopList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCoffeeShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //inflate
            convertView = mInflater.inflate(R.layout.list_item_coffeeshop, parent, false);

            //add the views to the holder
            holder = new ViewHolder();
            //views
            holder.nameX = convertView.findViewById(R.id.coffeeshop_name);
            holder.vibeRatingX = convertView.findViewById(R.id.vibe_rating);
            holder.coffeeRatingX = convertView.findViewById(R.id.coffee_rating);
            holder.locationX = convertView.findViewById(R.id.location);

            //add the holder to the view
            //for future use
            convertView.setTag(holder);
        } else {
            //get the view holder from convertview
            holder = (ViewHolder) convertView.getTag();
        }

        //get relevant subview of the row view
        TextView name = holder.nameX;
        TextView vibeRating = holder.vibeRatingX;
        TextView coffeeRating = holder.coffeeRatingX;
        TextView location = holder.locationX;

        //get corresponding recipe for each row
        CoffeeShop coffeeShop = (CoffeeShop) getItem(position);

        name.setText(CoffeeShop.name);
        vibeRating.setText(CoffeeShop.vibeRating);
        coffeeRating.setText(CoffeeShop.coffeeRating);
        location.setText(CoffeeShop.location);


        return convertView;
    }
    private static class ViewHolder {
        public TextView nameX;
        public TextView vibeRatingX;
        public TextView coffeeRatingX;
        public TextView locationX;
    }
}
