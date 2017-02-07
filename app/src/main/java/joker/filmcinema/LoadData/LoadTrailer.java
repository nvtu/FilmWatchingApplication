package joker.filmcinema.LoadData;

import android.content.Context;
import android.util.Log;

/**
 * Created by Tu Van Ninh on 1/18/2017.
 */
public class LoadTrailer extends LoadDataTask {

    public FinishLoadTrailer delegate = null;


    public LoadTrailer(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int start = s.indexOf("file: '") + 7;
        int end = s.indexOf("'", start);
        String filmPath = s.substring(start, end);
        Log.d("abcd", s);
        delegate.processLoadTrailerFinish(filmPath);
    }
     public interface FinishLoadTrailer{
         void processLoadTrailerFinish(String filmPath);
     }
}
