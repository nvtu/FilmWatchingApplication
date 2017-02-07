package joker.filmcinema.LoadData;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by Tu Van Ninh on 1/17/2017.
 */
public class LoadUserData{

    public FinishLoadUserData delegate = null;


    //Get user profile information: id, name, email, birthday
    public void getUserProfile(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        try {
                            String email = "";
                            String id = object.getString("id");
                            String name = object.getString("name");
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
                            delegate.processFinish(id, name, email);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email");
        request.setParameters(params);
        request.executeAsync();
    }

    public interface FinishLoadUserData{
        void processFinish(String id, String name, String email);
    }
}
