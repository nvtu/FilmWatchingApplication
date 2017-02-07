package joker.filmcinema;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuvanninh.universalvideoview.UniversalMediaController;
import com.example.tuvanninh.universalvideoview.UniversalVideoView;

import java.util.ArrayList;

import joker.filmcinema.Adapter.EpisodeAdapter;
import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.LoadData.LoadAPITask;
import joker.filmcinema.LoadData.LoadTrailer;
import joker.filmcinema.SQLite.SQLiteHelper;


public class WatchingFilmActivity extends AppCompatActivity implements LoadTrailer.FinishLoadTrailer, LoadAPITask.FinishLoadFilmURL, UniversalVideoView.VideoViewCallback{

    private static final String TAG = "WatchingFilmActivity";
    EpisodeAdapter epsAdapter;
//    String filmURL;
    ArrayList<Pair<String,String>> listEps;
    GridView gridView;
    ImageView gotoFilm;
    EditText inpText;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mVideoLayout;
    FilmModel models;
    private int mSeekPosition = 0;
    private int cachedHeight;
    private int curEps = 0;
    private boolean isFullscreen;
//    LoadFilmTask loadFilmTask;
    LoadAPITask loadAPITask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_film);
        models = (FilmModel) getIntent().getSerializableExtra("filmInfo");
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(models.geteName());
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fire_engine_red)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initLayout();
    }

    private void initLayout() {
        inpText = (EditText) findViewById(R.id.episode);
        gotoFilm = (ImageView) findViewById(R.id.go);
        gotoFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.valueOf(inpText.getText().toString());
                if (position > 0 && position < listEps.size() + 1){
                    curEps = position - 1;
                    loadData(listEps.get(position - 1).second);
                }
                else Toast.makeText(WatchingFilmActivity.this, "Your input is invalid", Toast.LENGTH_SHORT).show();
            }
        });
        gridView = (GridView) findViewById(R.id.listEps);
        listEps = new ArrayList<>();
        for (int i=0; i<models.getEpsName().size(); i++){
            listEps.add(new Pair<String,String>(models.getEpsName().get(i), models.getFilmEps().get(i)));
        }
        epsAdapter = new EpisodeAdapter(this, R.layout.item_eps, listEps);
        gridView.setAdapter(epsAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView vEps = (TextView) view.findViewById(R.id.epsInfo);
                vEps.setTextColor(getResources().getColor(R.color.fire_engine_red));
                curEps = position;
                loadData(listEps.get(position).second);
            }
        });
        if (listEps.size() > 0) loadData(listEps.get(curEps).second);
        else Toast.makeText(this, "This link is dead", Toast.LENGTH_SHORT).show();
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoViewCallback(this);
        setVideoAreaSize();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });

    }

    private void loadData(String url){
        if (models.getFilmLength().equals("Trailer")){
            Log.d("abcde", url);
            LoadTrailer loadTrailer = new LoadTrailer(this);
            loadTrailer.delegate = this;
            loadTrailer.execute(url);
        }
        else {
            loadAPITask = new LoadAPITask(this);
            loadAPITask.delegate = this;
            loadAPITask.execute(url);
        }
    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }

    private void startFilm(final String filmPath){
        SQLiteHelper db = new SQLiteHelper(this);
        if (!db.checkRecordExist(models.geteName(), 0)){
            db.insertHistory(models);
        }
        db.close();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mVideoView.setVideoPath(filmPath);
                        mSeekPosition = 0;
                        mVideoView.start();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && mSeekPosition >= 0){
            mVideoView.resume();
//            mVideoView.seekTo(mSeekPosition);
//            mVideoView.start();
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;

        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);


        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);

        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
        mSeekPosition = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
        mediaPlayer.seekTo(mSeekPosition);
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void processFinish(String filmPath) {
        if (filmPath.length() == 0){
            Toast.makeText(this, "Loading Film Failed. Please try again", Toast.LENGTH_SHORT).show();
        }
        else{
            startFilm(filmPath);
        }
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
    public void processLoadTrailerFinish(String filmPath) {
        if (filmPath.length() == 0){
            Toast.makeText(this, "Loading Film Failed. Please try again", Toast.LENGTH_SHORT).show();
        }
        else{
            startFilm(filmPath);
        }
    }
}
