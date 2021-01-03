package corp.fredkiss.smart_commerce.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import corp.fredkiss.smart_commerce.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        // les éléments de la vue
        ImageButton infoBtn = findViewById(R.id.logo_btn);

        // FULLSCREEN MODE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // au clic sur le bouton infos
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
            }
        });
    }

    /**
     * Afficher les informations relatives à l'application (à propos)
     */
    public void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Description de l'application")
                .setMessage(R.string.about_text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

}
