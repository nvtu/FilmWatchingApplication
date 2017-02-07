package joker.filmcinema;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.LoadData.LoadFilmEpisode;
import joker.filmcinema.SQLite.SQLiteHelper;

public class DetailActivity extends AppCompatActivity implements LoadFilmEpisode.FinishLoadEpsTask{

    LoadFilmEpisode loadFilmEpisode;
    FilmModel models;
    TextView tvVName, tvEName, tvDescription;
    ImageView filmImage;
    FloatingActionButton playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        models = (FilmModel) getIntent().getSerializableExtra("filmInfo");
        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fire_engine_red)));
            getSupportActionBar().setTitle(models.geteName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initLayout();
    }

    private void initLayout() {
        tvVName = (TextView) findViewById(R.id.tvVName);
        tvEName = (TextView) findViewById(R.id.tvEName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        filmImage = (ImageView) findViewById(R.id.ivFilmImg);
        playButton = (FloatingActionButton) findViewById(R.id.playButton);

        tvVName.setText(models.getvName());
        tvEName.setText(models.geteName());
        Picasso.with(this).load(models.getImgURL()).into(filmImage);

        loadFilmEpisode = new LoadFilmEpisode(this);
        loadFilmEpisode.delegate = this;
        loadFilmEpisode.execute(models.getFilmURL());
    }

    @Override
    public void processFinish(String description, ArrayList<String> filmURL, ArrayList<String> epsName) {
        models.setFilmEps(filmURL);
        models.setDescription(description);
        models.setEpsName(epsName);
        Log.d("abc", description);
        tvDescription.setText(description);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, WatchingFilmActivity.class);
                intent.putExtra("filmInfo", models);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        SQLiteHelper db = new SQLiteHelper(this);
        MenuItem item = menu.getItem(0);
        if (db.checkRecordExist(models.geteName(), 1)){
            item.setIcon(R.drawable.favorite);
        }
        else{
            item.setIcon(R.drawable.nonefavorite);
        }
        db.close();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.love){
            SQLiteHelper db = new SQLiteHelper(this);
            if (db.checkRecordExist(models.geteName(), 1)){
                item.setIcon(R.drawable.nonefavorite);
                db.deleteFromFavorite(models.geteName());
                if (FavoriteActivity.adapter != null){
                    FavoriteActivity.listFavorite.clear();
                    FavoriteActivity.listFavorite.addAll(db.getAllFavorite());
                    FavoriteActivity.adapter.notifyDataSetChanged();
                }
            }
            else{
                item.setIcon(R.drawable.favorite);
                db.insertFavorite(models);
                if (FavoriteActivity.adapter != null){
                    FavoriteActivity.listFavorite.add(models);
                    FavoriteActivity.adapter.notifyDataSetChanged();
                }
            }
            db.close();
            return true;
        }
        else if (id == android.R.id.home){
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
