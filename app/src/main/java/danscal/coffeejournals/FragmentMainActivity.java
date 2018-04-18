package danscal.coffeejournals;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by DanielScal on 4/18/18.
 */

public class FragmentMainActivity extends AppCompatActivity {

        private static final String TAG = "MainActivity";

        private SectionsPageAdapter mSectionsPageAdapter;

        private ViewPager mViewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tabs_main);
            Log.d(TAG, "onCreate: Starting.");

            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }

        private void setupViewPager(ViewPager viewPager) {
            SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
            adapter.addFragment(new ListViewFragment(), "LISTVIEW");
            adapter.addFragment(new MapViewFragment(), "MAPVIEW");
            adapter.addFragment(new FavoritesFragment(), "FAVORITES");
            viewPager.setAdapter(adapter);
        }

}
