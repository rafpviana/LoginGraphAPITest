package br.unifor.logingraphapitest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback;
//    private LoginButton loginButtonFacebook;
    private Button buttonFacebookLogin;
    private String userFirstName,userLastName, userEmail, userBirthday, userGender, userToken, userId;
    private URL userProfilePictureURL;
    private String TAG = "MainActivity Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        facebookCallback = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                userToken = loginResult.getAccessToken().getToken();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                        try {
                            userId = object.getString("id");
                            userProfilePictureURL = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if(object.has("first_name"))
                                userFirstName = object.getString("first_name");
                            if(object.has("last_name"))
                                userLastName = object.getString("last_name");
                            if (object.has("email"))
                                userEmail = object.getString("email");
                            if (object.has("birthday"))
                                userBirthday = object.getString("birthday");
                            if (object.has("gender"))
                                userGender = object.getString("gender");

                            Toast.makeText(MainActivity.this, "Login Id: "+ userId, Toast.LENGTH_LONG).show();

                            Log.i("Login Id: ", userId);
                            Log.i("Login First Name: ", userFirstName);
                            Log.i("Login Last Name: ", userLastName);
                            Log.i("Login Email: ", userEmail);
                            Log.i("Login Birthday: ", userBirthday);
                            Log.i("Login Gender: ", userGender);

                            Intent intentToSecondActivity = new Intent(MainActivity.this,SecondActivity.class);
                            intentToSecondActivity.putExtra("userId", userId);
                            intentToSecondActivity.putExtra("userFirstName",userFirstName);
                            intentToSecondActivity.putExtra("userLastName",userLastName);
                            intentToSecondActivity.putExtra("userEmail", userEmail);
                            intentToSecondActivity.putExtra("userBirthday", userBirthday);
                            intentToSecondActivity.putExtra("userGender", userGender);
                            intentToSecondActivity.putExtra("userProfilePictureURL",userProfilePictureURL.toString());
                            intentToSecondActivity.putExtra("userToken", userToken);
                            startActivity(intentToSecondActivity);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle bundleParameters = new Bundle();
                bundleParameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                graphRequest.setParameters(bundleParameters);
                graphRequest.executeAsync();



            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        };

        buttonFacebookLogin = (Button) findViewById(R.id.buttonFacebookLoginId);
        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "user_birthday","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
            }
        });

//        loginButtonFacebook = (LoginButton) findViewById(R.id.loginButtonFacebookId);
//        loginButtonFacebook.setHeight(100);
//        loginButtonFacebook.setTextColor(Color.WHITE);
//        loginButtonFacebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        loginButtonFacebook.setCompoundDrawablePadding(0);

//        loginButtonFacebook.setReadPermissions("email", "user_birthday","public_profile");
//        loginButtonFacebook.registerCallback(callbackManager, facebookCallback);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "br.unifor.logingraphapitest",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}