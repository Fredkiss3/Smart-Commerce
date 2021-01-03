package corp.fredkiss.smart_commerce.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.SharedPref;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private  DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean nightMode = SharedPref.getInstance(this).loadNightMode();
        setTheme(nightMode ? R.style.AppTheme :  R.style.DayMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fadein et out pour les transitions d'entrées-sorties
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        mDrawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    private void init() {
        // FULLSCREEN MODE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // les éléments de la vue
        ImageButton mMainMenuBtn = findViewById(R.id.main_hamburger_menu_btn);
        CardView mUserCard = findViewById(R.id.main_user_card);
        LinearLayout mJournalOption = findViewById(R.id.main_journal_line);
        LinearLayout mStockOption = findViewById(R.id.main_stock_line);
        LinearLayout mApproOption = findViewById(R.id.main_appro_line);
        LinearLayout mStatsOption = findViewById(R.id.main_stats_line);
        LinearLayout mPackOption = findViewById(R.id.main_pack_line);

        // Distirbution des tags
        mUserCard.setTag(0);
        mMainMenuBtn.setTag(1);
        mJournalOption.setTag(2);
        mStockOption.setTag(3);
        mApproOption.setTag(4);
        mStatsOption.setTag(5);
        mPackOption.setTag(6);

        // gestion des clics
        mUserCard.setOnClickListener(this);
        mMainMenuBtn.setOnClickListener(this);
        mJournalOption.setOnClickListener(this);
        mStockOption.setOnClickListener(this);
        mApproOption.setOnClickListener(this);
        mStatsOption.setOnClickListener(this);
        mPackOption.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_parametres) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            // Lancer la 2e activité et quitter
            startActivity(intent);

        } else if (id == R.id.nav_exit) {
            finish();
            System.exit(0);

        } else if (id == R.id.nav_note) {
            // todo : Handle the combo action
            showNotif("clic sur noter");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switch (tag) {
            case 0:
                showNotif("paramètres utilisateur");
                break;

            case 1:
                mDrawer.openDrawer(GravityCompat.START);
                break;

            case 2:
                // OUVRIR LE JOURNAL
                Intent i_journal = new Intent(MainActivity.this, JournalActivity.class);
                // Lancer la 2e activité
                startActivity(i_journal);
                break;

            case 3:
                // OUVRIR LE STOCK
                Intent i_stock = new Intent(MainActivity.this, StockActivity.class);
                // Lancer la 2e activité
                startActivity(i_stock);
                break;

            case 4:
                // OUVRIR L'APPROVISIONNEMENT
//                Intent i_basket = new Intent(MainActivity.this, BasketActivity.class);
                // Lancer la 2e activité
//                startActivity(i_basket);
                break;

            case 5:
                // todo : open benefices activity
                showNotif("clic sur statistiques");
                break;

            case 6:
                showTips();
                break;

            default:
                break;

        }
    }

    /**
     * Afficher une notification
     * @param message le message
     */
    private void showNotif(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * demander d'initialiser les prix d'achats
     */
    public void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fonction Premium")
                .setMessage(R.string.premium)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
