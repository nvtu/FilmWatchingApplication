package joker.filmcinema.LoadData;

import android.content.Context;
import android.util.Log;

/**
 * Created by Tu Van Ninh on 1/15/2017.
 */
public class LoadAPITask extends LoadDataTask implements LoadFilmTask.FinishLoadFilmTask{

    LoadFilmTask loadFilmTask;
    public FinishLoadFilmURL delegate = null;

    public LoadAPITask(Context context) {
        super(context);
        loadFilmTask = new LoadFilmTask(context);
        loadFilmTask.delegate = this;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int start = s.indexOf("url = [\"") + 8;
        int end = s.indexOf("\"", start);
        String api = s.substring(start, end);
        Log.d("abc", s.substring(start, end));
        loadFilmTask.execute(api);
    }

    @Override
    public void processFinish(String filmPath) {
        delegate.processFinish(filmPath);
    }

    public interface FinishLoadFilmURL{
        void processFinish(String filmPath);
    }

}
