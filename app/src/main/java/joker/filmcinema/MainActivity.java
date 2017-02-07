package joker.filmcinema;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import io.karim.MaterialTabs;
import joker.filmcinema.Adapter.ViewPagerAdapter;
import joker.filmcinema.DataModel.StaticData;
import joker.filmcinema.LoadData.LoadUserData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoadUserData.FinishLoadUserData{

    ViewPager viewPager;
    MaterialTabs tabs;
    ViewPagerAdapter adapter;
    private View header;
    ProfilePictureView profilePictureView;
    TextView tvUsername, tvEmail;
    LoadUserData loadUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.fire_engine_red));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Cinebox");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        headerInit();


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        viewPager.setCurrentItem(0);

        tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);

        tabs.setBackgroundColor(getResources().getColor(R.color.fire_engine_red));
        tabs.setIndicatorColor(getResources().getColor(R.color.white));
        tabs.setIndicatorHeight(3);
        tabs.setAllCaps(true);
        tabs.setTabPaddingLeftRight(14);
        tabs.setSameWeightTabs(true);
        tabs.setTextColorUnselected(getResources().getColor(R.color.white));
        tabs.setTextColorSelected(getResources().getColor(R.color.gorse));
        tabs.setTypefaceSelected(Typeface.DEFAULT_BOLD);

    }

    private void headerInit() {
        profilePictureView = (ProfilePictureView) header.findViewById(R.id.ivUserPic);
        tvUsername = (TextView) header.findViewById(R.id.tvUsername);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        loadUserData = new LoadUserData();
        loadUserData.delegate = this;
        loadUserData.getUserProfile();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.searchbutton){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.logout){
            StaticData.isLoaded = false;
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, InitDataAcitivity.class);
            startActivity(intent);
            item.setCheckable(false);
            finish();
        }
        else if (id == R.id.historyList){
            item.setCheckable(false);
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.favoriteList){
            item.setCheckable(false);
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.friends){
            item.setCheckable(false);
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(String id, String name, String email) {
        profilePictureView.setProfileId(id);
        tvUsername.setText(name);
        tvEmail.setText(email);
    }
}
