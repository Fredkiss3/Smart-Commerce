package corp.fredkiss.smart_commerce.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.SharedPref;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Mode nuit
        final boolean nightMode = SharedPref.getInstance(this).loadNightMode();
        setTheme(nightMode ? R.style.AppTheme : R.style.DayMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // fadein et out pour les transitions d'entrées-sorties
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // le switch
        Switch mNightModeSwitch = findViewById(R.id.settings_night_mode_sw);

        // l'état du swtich indique le mode nuit
        mNightModeSwitch.setChecked(nightMode);

        mNightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Changer l'état du mode nuit
                (SharedPref.getInstance(SettingsActivity.this)).saveNightMode(!nightMode);
                restartActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Au clic sur retour, retourner à l'activité principale et fermer cette Activité
        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }


    /**
     * Redémarrer l'activité
     */
    private void restartActivity(){
        Intent i = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
    }

}
