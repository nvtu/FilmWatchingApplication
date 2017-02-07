package joker.filmcinema.LoadData;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class LoadFilmEpisode extends LoadDataTask {

    public FinishLoadEpsTask delegate = null;
    ArrayList<String> filmURL = new ArrayList<>();
    ArrayList<String> epsName = new ArrayList<>();

    public LoadFilmEpisode(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        org.jsoup.nodes.Document doc = Jsoup.parse(s);
        String description = doc.body().select("div.entry.entry-m").select("p").text();
        Elements eps = doc.body().select("p.epi").select("a");
        for (org.jsoup.nodes.Element it: eps) {
            filmURL.add(it.attr("href"));
            epsName.add(it.text());
        }
        if (epsName.size() == 0){
            Elements trailerLink = doc.body().select("div.dt").select("a");
            for (org.jsoup.nodes.Element it : trailerLink){
                String href = it.attr("href");
                if (href.contains("xem-phim")){
                    href = href.replace("#xem", "");
                    filmURL.add(href+".html");
                    break;
                }
            }
            epsName.add("Trailer");
        }
        delegate.processFinish(description, filmURL, epsName);
    }

    public interface FinishLoadEpsTask{
        void processFinish(String description, ArrayList<String> filmURL, ArrayList<String> epsName);
    }
}
