package joker.filmcinema.Adapter;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import joker.filmcinema.R;

public class EpisodeAdapter extends ArrayAdapter<Pair<String,String>> {

    Context context;
    int resource;
    ArrayList<Pair<String,String>> listEps;

    public EpisodeAdapter(Context context, int resource, ArrayList<Pair<String,String>> listEps) {
        super(context, resource, listEps);
        this.context = context;
        this.resource = resource;
        this.listEps = listEps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView vEps = (TextView) convertView.findViewById(R.id.epsInfo);
        vEps.setText(listEps.get(position).first);
        return convertView;
    }
}