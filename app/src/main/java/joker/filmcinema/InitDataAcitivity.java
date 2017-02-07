package joker.filmcinema;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;

import joker.filmcinema.DataModel.FilmModel;
import joker.filmcinema.DataModel.StaticData;
import joker.filmcinema.LoadData.LoadFilmList;
import joker.filmcinema.LoadData.LoadFilmList.FinishLoadFilmList;


public class InitDataAcitivity extends AppCompatActivity implements FinishLoadFilmList{

    LoadFilmList loadFilmList;
    TextView tvTitle;
    Typeface titleCustomFont;
    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_init_data_acitivity);

        tvTitle = (TextView) findViewById(R.id.applicationTitle);
        titleCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Admiration Pains.ttf");
        tvTitle.setTypeface(titleCustomFont);
        StaticData.initData(this);
        if (!StaticData.isLoaded){
            loadData();
            StaticData.isLoaded = true;
            if (isLoginFacebook()) gotoMainActivity();
        }
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(InitDataAcitivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(InitDataAcitivity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(InitDataAcitivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isLoginFacebook(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(InitDataAcitivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loadData(){
        StaticData.isLoaded = true;
        for (int i=0; i<StaticData.linkCaterogy.length; i++){
            loadFilmList = new LoadFilmList(this, i);
            loadFilmList.delegate = this;
            loadFilmList.execute(StaticData.linkCaterogy[i] + StaticData.curPage[i]);
        }
    }

    @Override
    public void processFinish(ArrayList<FilmModel> listFilm, int position) {
        StaticData.listFilm[position].clear();
        StaticData.listFilm[position].addAll(listFilm);
        StaticData.adapter[position].notifyDataSetChanged();
    }

    @Override
    public void processFinish(ArrayList<FilmModel> listFilm) {

    }
}
