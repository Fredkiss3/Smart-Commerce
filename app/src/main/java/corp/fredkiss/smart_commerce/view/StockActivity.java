package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlStock;
import corp.fredkiss.smart_commerce.controller.SharedPref;

public class StockActivity extends AppCompatActivity implements ObserverView {

    private RecyclerView mRv;
    private StockAdapter mAdapter;
    private TextView mPlaceholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Mode nuit
        final boolean nightMode = SharedPref.getInstance(this).loadNightMode();
        setTheme(nightMode ? R.style.AppTheme : R.style.DayMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // transitions d'entrées-sorties
        overridePendingTransition(R.anim.righttoleft, R.anim.lefttoright);

        init();
    }

    /**
     * Initialisation des éléments de la vue
     */
    private void init() {
        // éléments
        mRv = findViewById(R.id.stock_new_rv);
        mAdapter = new StockAdapter(this);
        mPlaceholder = findViewById(R.id.stock_new_placeholder);

        // La liste des obervers ne doit contenir qu'un seul observer
        mAdapter.getObserverList().clear();
        mAdapter.getObserverList().add(this);

        // appliquer l'adapter
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        // la toolbar
        Toolbar tb = findViewById(R.id.stock_new_toolbar);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Mise à jour
        update();
    }

    @Override
    public void querry(String arg, Object obj) throws Exception {
        if(arg != null) {
            switch (arg) {
                case "open_details":
                    int pos = (int) obj;
                    openDetails(pos);
                    break;
                case "update":
                    update();
                    break;
            }
        } else {
            throw new Exception("L'argument ne doit pas être null !");
        }
    }

    @Override
    public Object getObserverState(String querry) {
        return null;
    }

    /**
     * mise à jour de l'activité
     */
    private void update() {
        ControlStock.getInstance(this).reload();
        mAdapter.updateAdapter();

        // Vérification du nombre d'articles
        if (ControlStock.getInstance(this).getCount() == 0) {
            mPlaceholder.setVisibility(View.VISIBLE);
            mRv.setVisibility(View.GONE);
        } else {
            mPlaceholder.setVisibility(View.GONE);
            mRv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Ouvrir les détails d'un article
     * @param pos la position de cet article dans la liste des articles
     */
    @SuppressLint("SetTextI18n")
    private void openDetails(int pos) {
        if (!ControlStock.getInstance(this).isCombo(pos)) {
            StockDetailsDialog detailsDialog = new StockDetailsDialog(this, pos);
            detailsDialog.show();
        } else {
            openComposition(pos);
        }
    }

    /**
     * Récupérer la composition d'un combo
     * @param pos la position
     */
    private void openComposition(int pos) {
        // todo : A faire vraiment plus-tard...
        Toast.makeText(this, "opening composition" + pos, Toast.LENGTH_SHORT).show();
    }

}
