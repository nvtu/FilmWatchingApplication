package joker.filmcinema.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

import joker.filmcinema.R;

/**
 * Created by Tu Van Ninh on 1/17/2017.
 */
public class FriendListAdapter extends ArrayAdapter<Pair<String,String>>{

    Context mContext;
    int resource;
    ArrayList<Pair<String,String>>listFriend;

    public FriendListAdapter(Context context, int resource, ArrayList<Pair<String,String>> objects) {
        super(context, resource, objects);
        this.listFriend = objects;
        this.resource = resource;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(resource, parent, false);
        }
        ProfilePictureView fPic = (ProfilePictureView) convertView.findViewById(R.id.fPic);
        TextView fName = (TextView) convertView.findViewById(R.id.fName);
        fPic.setProfileId(listFriend.get(position).first);
        fName.setText(listFriend.get(position).second);
        return convertView;
    }
}
