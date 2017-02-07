package joker.filmcinema;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import joker.filmcinema.Adapter.RecycleAdapter;
import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.SQLite.SQLiteHelper;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView rv;
    public static RecycleAdapter adapter;
    public static ArrayList<FilmModel> listFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fire_engine_red)));
            getSupportActionBar().setTitle("Favorite Movie");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        initLayout();
        setDataToView();
    }

    private void setDataToView() {
        SQLiteHelper db = new SQLiteHelper(this);
        listFavorite.addAll(db.getAllFavorite());
        adapter.notifyDataSetChanged();
        db.close();
    }

    private void initLayout() {
        rv = (RecyclerView) findViewById(R.id.rvf);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        listFavorite = new ArrayList<>();
        adapter = new RecycleAdapter(this, listFavorite, false, true);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
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
}
