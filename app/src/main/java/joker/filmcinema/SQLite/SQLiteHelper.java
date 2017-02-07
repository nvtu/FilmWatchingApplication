package joker.filmcinema.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import joker.filmcinema.DataModel.FilmModel;

/**
 * Created by Tu Van Ninh on 1/17/2017.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SQLiteMovieOnlineDataBase.db";
    public static final String TABLE_NAME[] = {"HISTORY", "FAVORITE"};
    public static final String COLUMN_MOVIE_NAME = "MOVIE_NAME";
    public static final String COLUMN_MOVIE_VNAME = "MOVIE_VNAME";
    public static final String COLUMN_NUM_EPS = "NUM_EPS";
    public static final String COLUMN_IMAGE_LINK = "IMAGE_LINKS";
    public static final String COLUMN_MOVIE_LINK = "MOVIE_LINKS";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME[0] + " ( " + COLUMN_MOVIE_NAME + " NVARCHAR PRIMARY KEY, " + COLUMN_MOVIE_VNAME + " NVARCHAR, " + COLUMN_NUM_EPS + " NVARCHAR, " +
                COLUMN_IMAGE_LINK + " NVARCHAR, " + COLUMN_MOVIE_LINK + " NVARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_NAME[1] + " ( " + COLUMN_MOVIE_NAME + " NVARCHAR PRIMARY KEY, " + COLUMN_MOVIE_VNAME + " NVARCHAR, " + COLUMN_NUM_EPS + " NVARCHAR, " +
                COLUMN_IMAGE_LINK + " NVARCHAR, " + COLUMN_MOVIE_LINK + " NVARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean checkRecordExist(String movieName, int tableId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[tableId] + " WHERE " + COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
        boolean ans = cursor.getCount() > 0;
        cursor.close();
        return ans;
    }

    public boolean insertHistory(FilmModel models){
        if (checkRecordExist(models.geteName(), 0)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_NAME, models.geteName());
        cv.put(COLUMN_MOVIE_VNAME, models.getvName());
        cv.put(COLUMN_IMAGE_LINK, models.getImgURL());
        cv.put(COLUMN_MOVIE_LINK, models.getFilmURL());
        cv.put(COLUMN_NUM_EPS, models.getFilmLength());
        db.insert(TABLE_NAME[0], null, cv);
        return true;
    }

    public boolean insertFavorite(FilmModel models){
        if (checkRecordExist(models.geteName(), 1)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_NAME, models.geteName());
        cv.put(COLUMN_MOVIE_VNAME, models.getvName());
        cv.put(COLUMN_IMAGE_LINK, models.getImgURL());
        cv.put(COLUMN_MOVIE_LINK, models.getFilmURL());
        cv.put(COLUMN_NUM_EPS, models.getFilmLength());
        db.insert(TABLE_NAME[1], null, cv);
        return true;
    }

    public int deleteFromHistory(String movieName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME[0], COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
    }

    public int deleteFromFavorite(String movieName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME[1], COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
    }

    public void deleteAllHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME[0]);
    }

    public void deleteAllFavorite(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME[1]);
    }

    public ArrayList<FilmModel> getAllFavorite(){
        ArrayList<FilmModel> listFilm = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[1], null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)),
                    vName = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_VNAME)),
                    imgURL = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)),
                    filmURL = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_LINK)),
                    numEps = cursor.getString(cursor.getColumnIndex(COLUMN_NUM_EPS));
            listFilm.add(new FilmModel(vName, name, numEps, imgURL, filmURL));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return listFilm;
    }

    public ArrayList<FilmModel> getAllHistory(){
        ArrayList<FilmModel> listFilm = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[0], null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)),
                    vName = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_VNAME)),
                    imgURL = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)),
                    filmURL = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_LINK)),
                    numEps = cursor.getString(cursor.getColumnIndex(COLUMN_NUM_EPS));
            listFilm.add(new FilmModel(vName, name, numEps, imgURL, filmURL));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return listFilm;
    }
}
