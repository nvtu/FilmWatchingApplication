package joker.filmcinema.DataModel;

import android.content.Context;

import java.util.ArrayList;

import joker.filmcinema.Adapter.RecycleAdapter;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class StaticData{

    public static String[] linkCaterogy = new String[]{
            "http://mphim.net/phim-chieu-rap.html?page=",
            "http://mphim.net/the-loai/hanh-dong.html?page=",
            "http://mphim.net/the-loai/vo-thuat-kiem-hiep.html?page=",
            "http://mphim.net/the-loai/tam-ly-tinh-cam.html?page=",
            "http://mphim.net/the-loai/hoat-hinh.html?page=",
            "http://mphim.net/the-loai/kinh-di-ma.html?page=",
            "http://mphim.net/the-loai/hai-huoc.html?page=",
            "http://mphim.net/the-loai/vien-tuong.html?page=",
            "http://mphim.net/the-loai/phim-bo-han-quoc.html?page=",
            "http://mphim.net/the-loai/than-thoai.html?page=",
            "http://mphim.net/the-loai/chien-tranh.html?page=",
            "http://mphim.net/the-loai/the-thao-am-nhac.html?page=",
            "http://mphim.net/the-loai/phieu-luu.html?page=",
            "http://mphim.net/the-loai/music-box.html?page=",
            "http://mphim.net/the-loai/clip-vui.html?page=",
            "http://mphim.net/the-loai/phim-18.html?page=",
    };

    public static Integer[] curPage = new Integer[]{
            1,1,1,1,1,1,
            1,1,1,1,1,1,
            1,1,1,1
    };

    public static boolean isLoaded = false;

    public static RecycleAdapter[] adapter = new RecycleAdapter[17];

    public static ArrayList<FilmModel>[] listFilm = new ArrayList[17];


    public static void initData(Context mContext){
        for (int i=0; i<linkCaterogy.length; i++) {
            listFilm[i] = new ArrayList<>();
            adapter[i] = new RecycleAdapter(mContext, listFilm[i]);
        }
    }


}
