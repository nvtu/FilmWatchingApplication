package joker.filmcinema.LoadData;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tu Van Ninh on 1/15/2017.
 */
public class LoadFilmTask extends LoadDataTask {

    public FinishLoadFilmTask delegate = null;

    public LoadFilmTask(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            String res = "";
            if (s.length() > 2){
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONObject("data").getJSONArray("sources");
                if (array.length() > 0){
                    res = array.getJSONObject(0).getString("file");
                    res = res.replace("\\", "");
                    res = res.replace("*", "&2A");
                }
            }
            delegate.processFinish(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Document doc = Jsoup.parse(s);
//        String body = doc.body().toString();
//        int start = body.indexOf("src: ") + 6;
//        int end = body.indexOf("\",", start);
//        String filmPath = body.substring(start, end);
//        Log.d("abcd", filmPath);
//        delegate.processFinish(filmPath);
    }

    public interface FinishLoadFilmTask{
        void processFinish(String filmPath);
    }
}
