package joker.filmcinema.LoadData;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import joker.filmcinema.DataModel.FilmModel;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class LoadFilmList extends LoadDataTask {

    public FinishLoadFilmList delegate = null;
    ArrayList<FilmModel> listFilm = new ArrayList<>();
    int position = -1;

    public LoadFilmList(Context context){
        super(context);
    }

    public LoadFilmList(Context context, int position) {
        super(context);
        this.position = position;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        org.jsoup.nodes.Document doc = Jsoup.parse(s);
        Elements film = doc.body().select("ul.list_m").select("li");
        for (org.jsoup.nodes.Element it: film){
            String href = it.select("a").attr("href");
            String imgURL = it.select("img.lazy").attr("data-original");
            String eName = it.select("span.title.real").text();
            String vName = it.select("span.title.display").text();
            String quality = it.select("span.m-label.q").text();
            String subtitle = it.select("span.m-label.lang").text();
            String filmLength = it.select("span.m-label.ep").text();
            FilmModel models = new FilmModel(vName, eName, filmLength, quality, subtitle, imgURL, href);
            listFilm.add(models);
        }
        if (position == -1) delegate.processFinish(listFilm);
        else delegate.processFinish(listFilm, position);
    }

    public interface FinishLoadFilmList{
        void processFinish(ArrayList<FilmModel> listFilm, int position);
        void processFinish(ArrayList<FilmModel> listFilm);
    }
}
