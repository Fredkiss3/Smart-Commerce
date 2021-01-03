package corp.fredkiss.smart_commerce.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import corp.fredkiss.smart_commerce.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView mSmartText;
    private  TextView[] mTextViews = new TextView[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // L'icône
        mSmartText = findViewById(R.id.splash_smart__);

        // les textes
        mTextViews[0] = findViewById(R.id.splash_commentaire);
        mTextViews[1] = findViewById(R.id.splash_copyright);
//
//        for (TextView t: mTextViews)
//            t.setTypeface(TypeFaceUtil.getFont(this));

        setupAnimations();

    }

    private void setupAnimations() {
        // charger les animations
        final Animation fromTopAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fromtop);
        final Animation fromBottomAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.frombottom);
        final Animation pauseAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.pause);

        // Commencer la 1ère animation
        mSmartText.startAnimation(fromTopAnimation);
        mTextViews[0].startAnimation(fromTopAnimation);
        mTextViews[1].startAnimation(fromBottomAnimation);

        // Les listeners d'animations...
        fromBottomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSmartText.startAnimation(pauseAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pauseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);

                // Lancer la 2e activité et quitter
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
