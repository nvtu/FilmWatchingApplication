package joker.filmcinema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.DetailActivity;
import joker.filmcinema.R;
import joker.filmcinema.SQLite.SQLiteHelper;

/**
 * Created by Tu Van Ninh on 1/14/2017.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FilmModel> listFilm;
    boolean isHistory = false;
    boolean isFavorite = false;

    public RecycleAdapter(Context context, ArrayList<FilmModel> listFilm){
        mContext = context;
        this.listFilm = listFilm;
    }

    public RecycleAdapter(Context context, ArrayList<FilmModel> listFilm, boolean isHistory, boolean isFavorite){
        mContext = context;
        this.listFilm = listFilm;
        this.isHistory = isHistory;
        this.isFavorite = isFavorite;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FilmModel models = listFilm.get(position);
        if (models.getImgURL().length() > 0) {
            Picasso.with(mContext).load(models.getImgURL()).fit().into(holder.filmImage);
        }
        holder.tvEName.setText(models.geteName());
        holder.tvVName.setText(models.getvName());
        holder.tvEpisode.setText(models.getFilmLength());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("filmInfo", models);
                mContext.startActivity(intent);
            }
        });
        if (isHistory || isFavorite){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You won't be able to recover this file!")
                            .setCancelText("NO")
                            .setConfirmText("YES")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    SQLiteHelper db = new SQLiteHelper(mContext);
                                    if (isHistory) db.deleteFromHistory(listFilm.get(position).geteName());
                                    else if (isFavorite) db.deleteFromFavorite(listFilm.get(position).geteName());
                                    listFilm.remove(position);
                                    notifyDataSetChanged();
                                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Good job!")
                                            .setContentText("The file is deleted!")
                                            .show();
                                    db.close();
                                }
                            })
                            .show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEName, tvVName, tvEpisode;
        ImageView filmImage;


        public ViewHolder(final View itemView) {
            super(itemView);
            tvEName = (TextView) itemView.findViewById(R.id.eName);
            tvVName = (TextView) itemView.findViewById(R.id.vName);
            tvEpisode = (TextView) itemView.findViewById(R.id.filmEpisode);
            filmImage = (ImageView) itemView.findViewById(R.id.filmImage);
        }
    }
}
