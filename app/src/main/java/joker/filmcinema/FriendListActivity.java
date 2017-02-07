package joker.filmcinema;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import joker.filmcinema.Adapter.FriendListAdapter;
import joker.filmcinema.LoadData.LoadFriendList;
import joker.filmcinema.LoadData.LoadFriendList.FinishLoadFriendList;

public class FriendListActivity extends AppCompatActivity implements FinishLoadFriendList{

    LoadFriendList loadFriendList;
    GridView gv;
    ArrayList<Pair<String,String>> friendList;
    FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fire_engine_red)));
            getSupportActionBar().setTitle("Friends using Application");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initLayout();
    }

    private void initLayout() {
        friendList = new ArrayList<>();
        adapter = new FriendListAdapter(this, R.layout.friend_item, friendList);
        gv = (GridView) findViewById(R.id.gv);
        gv.setAdapter(adapter);
        loadFriendList();
    }

    private void loadFriendList(){
        loadFriendList = new LoadFriendList();
        loadFriendList.delegate = this;
        loadFriendList.getUserFriendList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void processFinish(ArrayList<Pair<String,String>> friendId) {
        friendList.addAll(friendId);
        adapter.notifyDataSetChanged();
    }
}
