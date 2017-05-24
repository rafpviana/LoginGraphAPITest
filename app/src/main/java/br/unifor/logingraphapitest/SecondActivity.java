package br.unifor.logingraphapitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class SecondActivity extends AppCompatActivity {

    private String userFirstName, userLastName, userProfilePictureURL, userEmail, userBirthday, userGender, userId, userToken;
    private String TAG = "MainActivity";
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle inBundle = getIntent().getExtras();
        userFirstName = inBundle.getString("userFirstName");
        userLastName = inBundle.getString("userLastName");
        userEmail = inBundle.getString("userEmail");
        userBirthday = inBundle.getString("userBirthday");
        userGender = inBundle.getString("userGender");
        userProfilePictureURL = inBundle.getString("userProfilePictureURL");
        userId = inBundle.getString("userId");
        userToken = inBundle.getString("userToken");


        TextView nameView = (TextView) findViewById(R.id.textViewFullNameId);
        nameView.setText("Full Name: " + userFirstName + " " + userLastName);

        TextView emailView = (TextView) findViewById(R.id.textViewEmailId);
        emailView.setText("Email: " + userEmail);

        TextView birthdayView = (TextView) findViewById(R.id.textViewBirthdayId);
        birthdayView.setText("Birthday: " + userBirthday);

        TextView genderView = (TextView) findViewById(R.id.textViewGenderId);
        genderView.setText("Gender: " + userGender);

        TextView userIdView = (TextView) findViewById(R.id.textViewUserIdId);
        userIdView.setText("User Id: " + userId);

        TextView tokenView = (TextView) findViewById(R.id.textViewTokenId);
        tokenView.setText(userToken);

        new DownloadImage((ImageView) findViewById(R.id.imageViewProfilePictureId)).execute(userProfilePictureURL);

        buttonLogout = (Button) findViewById(R.id.buttonLogoutId);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}