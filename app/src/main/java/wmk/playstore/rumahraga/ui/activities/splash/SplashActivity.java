package wmk.playstore.rumahraga.ui.activities.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import wmk.playstore.rumahraga.ui.activities.onboarding.OnBoardingActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                finish();
            }
        }, 1200L);
    }
}