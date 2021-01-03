package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.SharedPref;

@SuppressLint("RestrictedApi")
public class JournalActivity extends AppCompatActivity implements View.OnClickListener, ObserverView {

    private ViewPager mViewPager;
    private Toolbar tbNormal;
    private Toolbar tbSelection;
    private TextView mCounterText;

    // Boutons
    private FloatingActionButton fab;


    // variables privées nécessaires
    private boolean mActionModeStatus;

    // Des Tags de Boutons pour l'évènement du clic
    private final int FAB_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // THEME
        boolean nightMode = SharedPref.getInstance(this).loadNightMode();
        setTheme(nightMode ? R.style.AppTheme :  R.style.DayMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        // transitions d'entrées-sorties
        overridePendingTransition(R.anim.righttoleft, R.anim.lefttoright);

        init();
    }

    /**
     * Initialisation de l'Activity
     */
    private void init(){
        // Boutons
        ImageButton cancelButton = findViewById(R.id.journal_btn_cancel);
        ImageButton homeButton = findViewById(R.id.journal_btn_home);


        // Les textView
        mCounterText = findViewById(R.id.journal_counter);

        /* La toolbar */
        tbNormal = findViewById(R.id.toolbar_journal);
        tbSelection = findViewById(R.id.toolbar_selection);

        // l'icône de navigation
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableActionMode();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // définir la toolbar comme par défaut
        setSupportActionBar(tbNormal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Le bouton de la monnaie
        fab = findViewById(R.id.journal_fab_add);
        fab.setTag(FAB_TAG);
        fab.setOnClickListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        JournalAdapter journalAdapter = new JournalAdapter(getSupportFragmentManager());
        journalAdapter.addFragment(FragmentDiffs.init(this, this));
        journalAdapter.addFragment(FragmentVentes.init(this));
        journalAdapter.addFragment(FragmentEmprunts.init(this));

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(journalAdapter);

        // the tab layout
        TabLayout tabLayout = findViewById(R.id.tabs);

        // add on page change listeners
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                // désactiver le mode de sélection dès que l'on change de page
                disableActionMode();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // désactiver le mode 'action'
        disableActionMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!mActionModeStatus) {
            getMenuInflater().inflate(R.menu.menu_journal, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_selection, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_history:
                // TODO : Afficher l'historique du journal
                Toast.makeText(this, "HISTORIQUE ", Toast.LENGTH_SHORT).show();
                break;

            // Supprimer
            case R.id.action_delete:
                askConfirmForRemove();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * activer le mode de sélection
     */
    public void enableActionMode() {
        if (!mActionModeStatus) {
//            /* La visibilité des textViews */
//            mCounterText.setVisibility(View.VISIBLE);
//            mJournalHeading.setVisibility(View.GONE);

            // Le bouton de navigation
//            cancelButton.setImageResource(R.drawable.ic_cancel_black_24dp);


            // Changer de toolbar
            tbNormal.setVisibility(View.GONE);
            tbSelection.setVisibility(View.VISIBLE);

            // Remettre la toolbar
            tbSelection.getMenu().clear();
            tbSelection.inflateMenu(R.menu.menu_selection);

//            mCounterText.setText(R.string.selection);

            // définir la toolbar comme par défaut
            setSupportActionBar(tbSelection);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            // Faire disparaître le bouton
            fab.setVisibility(View.GONE);
        }

        mActionModeStatus = true;
    }

    /**
     * désactiver le mode de sélection
     */
    public void disableActionMode() {

        if(mActionModeStatus) {
            // set the visibility of the fab
            fab.setVisibility(View.VISIBLE);

//            // La visibilité des textViews
//            mCounterText.setVisibility(View.GONE);
//            mJournalHeading.setVisibility(View.VISIBLE);

            // Le bouton de navigation
//            cancelButton.setImageResource(R.drawable.back_btn);

            // Remettre la toolbar

            // Changer de toolbar
            tbNormal.setVisibility(View.VISIBLE);
            tbSelection.setVisibility(View.GONE);
            mCounterText.setText(R.string.selection);
            tbNormal.getMenu().clear();
            tbNormal.inflateMenu(R.menu.menu_journal);

            // définir la toolbar comme par défaut
            setSupportActionBar(tbNormal);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);


            // reset l'adapter
            try {
                FragmentDiffs.getAdapter().unSelectAll();
            } catch (NullPointerException ignore) {}
        }

        mActionModeStatus = false;

    }

    @Override
    public void onClick(View view) {
        // Pour chaque vue
        switch ((int) view.getTag()) {
            case FAB_TAG:
                setFabListener();
                break;
        }
    }

    /**
     * Appui sur le bouton 'Retour'
     */
    @Override
    public void onBackPressed() {


        if (mActionModeStatus) {
            disableActionMode();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Le listener pour le bouton flottant
     */
    public void setFabListener(){
        switch (mViewPager.getCurrentItem()) {
            case 0:
                FragmentDiffs.addCred();
                break;

            case 1:
                FragmentVentes.openBuyableList();
                break;

            default:
                showTips();
                break;
        }
    }

    /**
     * mettre à jour le compteur
     * @param count le compte
     */
    public void updateCounterText(int count) {
        String counter_msg;

        if(count > 1) {
            counter_msg = count + " sélectionnés";
        } else {
            counter_msg = count + " sélectionné";
        }

        // Afficher le message
        mCounterText.setText(counter_msg);

    }

    @Override
    public void querry(String arg, Object obj) throws Exception {

        if(arg != null) {
            switch (arg) {
                case "querry":
                    try {
                        int count = (int) obj;

                        // querry the counter text
                        updateCounterText(count);

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "disable":
                    try {
                        // disable Action Mode
                        disableActionMode();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "enable":
                    try {
                        // enable Action Mode
                        enableActionMode();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        } else {
            throw new Exception("notified observer without an argument !");
        }

    }

    @Override
    public Object getObserverState(String querry) {
        Object result = null;

        if(querry != null) {
            switch (querry){
                case "in_action_mode":
                    result = mActionModeStatus;
                    break;
            }

        }

        return result;
    }


    /**
     * Demander pour une confirmation
     */
    private void askConfirmForRemove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Retirer")
                .setMessage("Supprimer ces éléments ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentDiffs.getAdapter().deleteSelected();
                        FragmentDiffs.updateFragment();
                        disableActionMode();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        disableActionMode();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
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
