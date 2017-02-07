package joker.filmcinema.LoadData;

import android.util.Log;
import android.util.Pair;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tu Van Ninh on 1/17/2017.
 */
public class LoadFriendList {

    public FinishLoadFriendList delegate = null;
    ArrayList<Pair<String,String>> friendId = new ArrayList<>();

    //Get user friend list on Facebook
    public void getUserFriendList(){
        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {
                        Log.d("fl", objects.toString());
                        for (int i=0; i<objects.length(); i++){
                            try {
                                JSONObject object = objects.getJSONObject(i);
                                friendId.add(new Pair<String, String>(object.getString("id"), object.getString("name")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        delegate.processFinish(friendId);
                    }
                });
        request.executeAsync();
    }

    public interface FinishLoadFriendList{
        void processFinish(ArrayList<Pair<String,String>> friendId);
    }
}
