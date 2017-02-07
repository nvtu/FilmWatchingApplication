package joker.filmcinema.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;

import joker.filmcinema.Fragment.CommonFragment;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Phim Chiếu Rạp", "Hành Động", "Võ Thuật-Kiếm Hiệp", "Tâm Lý-Tình Cảm", "Hoạt Hình", "Kinh Dị-Ma", "Hài Hước",
    "Viễn Tưởng", "Phim bộ Hàn Quốc", "Thần Thoại", "Chiến Tranh", "Thể thao-Âm Nhạc", "Phiêu Lưu", "Music Box", "Clip Vui", "Phim 18+"};

    private final ArrayList<String> mTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        Collections.addAll(mTitles, TITLES);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CommonFragment.newInstance(position);
    }
}
