package corp.fredkiss.smart_commerce.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlUser;

public class DashboardActivity_ extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageButton mSmartBtn;
    private CardView mJournalCard;
    private CardView mStockCard;
    private CardView mBenefCard;
    private TextView mUserNameText;
    private DrawerLayout mDrawer;
    private TextView[] mTextViews = new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    /**
     * mettre en place tous éléments visuels
     */
    private void init() {
        mSmartBtn = findViewById(R.id.dash_smart_btn);
        mJournalCard = findViewById(R.id.dash_card_journal);
        mBenefCard = findViewById(R.id.dash_card_benefice);
        mStockCard = findViewById(R.id.dash_card_stock);
        mDrawer = findViewById(R.id.drawer_layout);
        mTextViews[0] = findViewById(R.id.app_name);
        mTextViews[1] = findViewById(R.id.dash_journal);
        mTextViews[2] = findViewById(R.id.dash_benefice);
        mTextViews[3] = findViewById(R.id.dash_stock);

        mSmartBtn.setTag(1);
        mJournalCard.setTag(2);
        mBenefCard.setTag(3);
        mStockCard.setTag(4);

        mSmartBtn.setOnClickListener(this);
        mJournalCard.setOnClickListener(this);
        mBenefCard.setOnClickListener(this);
        mStockCard.setOnClickListener(this);

        // appliquer la police à tous mes textes
        for (TextView t:mTextViews) {
            if (t != null)
                t.setTypeface(TypeFaceUtil.getFont(this));
        }

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // TODO -> TO CHANGE
                mUserNameText = findViewById(R.id.dash_username);
                mUserNameText.setTypeface(TypeFaceUtil.getFont(DashboardActivity_.this));
                try {
                    mUserNameText.setText(ControlUser.getInstance(
                            DashboardActivity_.this.
                            getPreferences(MODE_PRIVATE)).
                            getUserName().
                            toUpperCase());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        // FULLSCREEN MODE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_combo:
                // todo : Handle the combo action
                showNotif("clic sur pack");
                break;

            case R.id.nav_settings:
                // todo : Handle the settings action
                showNotif("clic sur paramètres");

                break;
            case R.id.nav_about:
                Intent intent = new Intent(DashboardActivity_.this, AboutActivity.class);
                // Lancer la 2e activité et quitter
                startActivity(intent);
                break;
            case R.id.nav_quit:
                finish();
                System.exit(0);
        }


        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();

        switch (tag){
            case 1:
                mDrawer.openDrawer(GravityCompat.START);
                mUserNameText = findViewById(R.id.dash_username);
                System.out.println(mUserNameText);
                break;
            case 2:
                Intent intent = new Intent(DashboardActivity_.this, JournalActivity.class);
                // Lancer la 2e activité et quitter
                startActivity(intent);
                break;
            case 3:
                // todo : open benefices activity
                showNotif("clic sur bénéfices");
                break;
            case 4:
                Intent intent_stock = new Intent(DashboardActivity_.this, StockActivity_.class);

                // Lancer la 2e activité et quitter
                startActivity(intent_stock);
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
}
