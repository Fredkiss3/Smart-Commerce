package corp.fredkiss.smart_commerce.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlUser;
import corp.fredkiss.smart_commerce.controller.SharedPref;

public class LoginActivity extends AppCompatActivity {

    private ControlUser mControlUser;
    private Button mBtnValid;
    private EditText mUNameField;
    private EditText mPasswordField;
    private TextView mBanner;
    private Animation mFadeInAnimation;
    private Animation mFadeOutAnimation;
    private Animation mPauseAnimation;
    private final String[] bannerText = {
        "Enregistrez toutes vos dépenses.",
        "Votre stock en temps réel.",
        "Calculez votre bénéfice sur n'importe qu'elle période."
    };
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean nightMode = SharedPref.getInstance(this).loadNightMode();
        setTheme(nightMode ? R.style.AppTheme :  R.style.DayMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mControlUser = ControlUser.getInstance(getPreferences(MODE_PRIVATE));
        init();
        setupAnimations();
    }

    /**
     * mettre en place tous éléments visuels
     */
    private void init() {
        mPasswordField = findViewById(R.id.login_password);
        mUNameField = findViewById(R.id.login_username);
        mBtnValid = findViewById(R.id.login_valid_btn);
        mBanner = findViewById(R.id.login_banner);

//        mBanner.setTypeface(TypeFaceUtil.getFont(this));
//        mUNameField.setTypeface(TypeFaceUtil.getFont(this));
//        mPasswordField.setTypeface(TypeFaceUtil.getFont(this));
//        mBtnValid.setTypeface(TypeFaceUtil.getFont(this));

        setListeners();
    }

    /**
     * le listener pour le bouton
     */
    private void setListeners() {
        mBtnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mControlUser.isFirstTime()){

//                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // Lancer la 2e activité et quitter
                    startActivity(intent);
                    finish();
                }else {
                    String username = mUNameField.getText().toString();
                    String password = mPasswordField.getText().toString();

                    if(username.length() == 0 || password.length() == 0)
                        showMessage("Alerte", "Saisie incorecte.");


                    // vérifier l'identité de l'utilisateur
                    if (mControlUser.verifyUser(username, password)){

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // Lancer la 2e activité et quitter
                        startActivity(intent);
                        finish();
                    }else{
                        showMessage("Erreur", "les informations de correspondent pas.");
                    }
                }
            }
        });
    }

    /**
     * mettre en place les animations
     */
    private void setupAnimations(){
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        mPauseAnimation = AnimationUtils.loadAnimation(this, R.anim.long_pause);

        // PAUSE -> OUT
        mPauseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBanner.startAnimation(mFadeOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // OUT -> IN
        mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(i == bannerText.length){
                    i = 0;
                }
                mBanner.setText(bannerText[i++]);
                mBanner.startAnimation(mFadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // IN -> PAUSE
        mFadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBanner.startAnimation(mPauseAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // commencer les animations
        mBanner.startAnimation(mPauseAnimation);
    }

    /**
     * afficher une dialog en guise de message
     * @param title titre
     * @param message message
     */
    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();
    }
}
