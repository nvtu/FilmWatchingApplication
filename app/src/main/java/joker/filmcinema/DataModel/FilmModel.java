package joker.filmcinema.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class FilmModel implements Serializable {
    String vName;
    String eName;
    String filmLength;
    String quality;
    String subtitles;
    String description;
    String imgURL;
    String filmURL;
    ArrayList<String> filmEps = new ArrayList<>();
    ArrayList<String> epsName = new ArrayList<>();

    public ArrayList<String> getEpsName() {
        return epsName;
    }

    public void setEpsName(ArrayList<String> epsName) {
        this.epsName = epsName;
    }

    public FilmModel(){

    }

    public FilmModel(String vName, String eName, String filmLength, String imgURL, String filmURL){
        this.vName = vName;
        this.eName = eName;
        this.filmLength = filmLength;
        this.imgURL = imgURL;
        this.filmURL = filmURL;
    }

    public FilmModel(String vName, String eName, String filmLength, String quality, String subtitles, String imgURL, String filmURL){
        this.vName = vName;
        this.eName = eName;
        this.filmLength = filmLength;
        this.quality = quality;
        this.subtitles = subtitles;
        this.imgURL = imgURL;
        this.filmURL = filmURL;
    }

    public String getFilmURL() {
        return filmURL;
    }

    public void setFilmURL(String filmURL) {
        this.filmURL = filmURL;
    }


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(String filmLength) {
        this.filmLength = filmLength;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getFilmEps() {
        return filmEps;
    }

    public void setFilmEps(ArrayList<String> filmEps) {
        this.filmEps = filmEps;
    }
}
