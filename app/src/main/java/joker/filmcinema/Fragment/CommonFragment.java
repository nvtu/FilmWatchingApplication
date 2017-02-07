package joker.filmcinema.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.DataModel.StaticData;
import joker.filmcinema.LoadData.LoadFilmList;
import joker.filmcinema.R;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class CommonFragment extends Fragment implements LoadFilmList.FinishLoadFilmList{

    private static final String ARG_POSITION = "position";
    private int position;
    LoadFilmList loadFilmList;
    private int visibleThresh = 4;
    boolean isContinueLoading = false;
    boolean isRefreshing = false;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;


    public static CommonFragment newInstance(int position) {
        CommonFragment f = new CommonFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
//        loadFilmList = new LoadFilmList(getContext());
//        loadFilmList.delegate = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        initFragmentView(rootView);
        return rootView;
    }

    private void initFragmentView(View rootView) {
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rvs);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(StaticData.adapter[position]);
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
                    StaticData.curPage[position]++;
                    loadFilmList = new LoadFilmList(getContext(), position);
                    loadFilmList.delegate = CommonFragment.this;
                    if (loadFilmList.getStatus() != AsyncTask.Status.RUNNING) {
                        loadFilmList.execute(StaticData.linkCaterogy[position] + StaticData.curPage[position].toString());
                    }

                }
            }
        });

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) rootView.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.fire_engine_red));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                isRefreshing = true;
                loadFilmList = new LoadFilmList(getContext(), position);
                loadFilmList.delegate = CommonFragment.this;
                StaticData.curPage[position] = 1;
                loadFilmList.execute(StaticData.linkCaterogy[position] + StaticData.curPage[position].toString());
            }
        });


    }

    @Override
    public void processFinish(ArrayList<FilmModel> listFilm, int position) {
        isContinueLoading = false;
        if (listFilm.size() == 0) {
            Toast.makeText(getContext(), "You have reached end of page",Toast.LENGTH_SHORT).show();
        } else {
            if (isRefreshing){
                StaticData.listFilm[position].clear();
                isRefreshing = false;
            }
            StaticData.listFilm[position].addAll(listFilm);
            StaticData.adapter[position].notifyDataSetChanged();
            mWaveSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void processFinish(ArrayList<FilmModel> listFilm) {

    }
}
