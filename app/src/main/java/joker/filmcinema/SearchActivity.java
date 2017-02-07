package joker.filmcinema;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

import joker.filmcinema.Adapter.RecycleAdapter;
import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.LoadData.LoadFilmList;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class SearchActivity extends AppCompatActivity implements LoadFilmList.FinishLoadFilmList {

    LoadFilmList loadFilmList;
    RecycleAdapter adapter;
    ArrayList<FilmModel> filmList;
    RecyclerView rv;
    boolean isContinueLoading = false;
    private int visibleThresh = 4;
    Integer curPage = 1;
    String linkSearch = "http://mphim.net/danh-sach-phim.html/?s=";
    String query;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private boolean isRefreshing = false;
    TreeMap<String, Boolean> checkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fire_engine_red)));
            getSupportActionBar().setTitle("Search Movie");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        filmList = new ArrayList<>();
        adapter = new RecycleAdapter(this, filmList);
        rv = (RecyclerView) findViewById(R.id.rvc);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                if (!isContinueLoading && totalItemCount <= (lastVisibleItem + visibleThresh)) {
                    isContinueLoading = true;
                    loadFilmList = new LoadFilmList(getApplicationContext());
                    loadFilmList.delegate = SearchActivity.this;
                    curPage++;
                    if (loadFilmList.getStatus() != AsyncTask.Status.RUNNING) {
                        loadFilmList.execute(linkSearch + query + "&page=" + curPage);
                    }

                }
            }
        });
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.refresh_swipe);
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.fire_engine_red));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                if (loadFilmList.getStatus() != AsyncTask.Status.RUNNING ) {
                    isRefreshing = true;
                    loadFilmList = new LoadFilmList(SearchActivity.this);
                    loadFilmList.delegate = SearchActivity.this;
                    curPage = 1;
                    loadFilmList.execute(linkSearch + query + "&page=" + curPage);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Iconify the widget
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadFilmList = new LoadFilmList(getApplicationContext());
                loadFilmList.delegate = SearchActivity.this;
                query = query.replace(' ', '-');
                SearchActivity.this.query = query;
                curPage = 1;
                loadFilmList.execute(linkSearch + query + "&page=" + curPage);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
    public void processFinish(ArrayList<FilmModel> listFilm, int position) {

    }

    @Override
    public void processFinish(ArrayList<FilmModel> listFilm) {
        if (isContinueLoading){
            isContinueLoading = false;
            for (FilmModel it : listFilm){
                if (checkName.containsKey(it.geteName())) return;
            }
            filmList.addAll(listFilm);
            adapter.notifyDataSetChanged();
        }
        else{
            filmList.clear();
            if (listFilm.size() == 0){
                Toast.makeText(this, "Sorry, we found no result!", Toast.LENGTH_SHORT).show();
            }
            else{
                checkName = new TreeMap<>();
                if (isRefreshing){
                    isRefreshing = false;
                    filmList.clear();
                }
                for (FilmModel it : listFilm){
                    checkName.put(it.geteName(), Boolean.TRUE);
                }
                filmList.addAll(listFilm);
                adapter.notifyDataSetChanged();
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}