package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlStock;

public class StockDetailsDialog implements View.OnClickListener, ObservableView{
    private EditText buyableName;
    private EditText buyableUnitPrice;
    private TextView buyableNbLeft;
    private TextView buyableRef;
    private LinearLayout modif_line;
    private LinearLayout normal_line;
    private Dialog dialogFiche;
    private Context mContext;
    private int currentPos;
    private ObserverView obs;

    StockDetailsDialog(Context ctx, final int pos) {
        obs = (ObserverView) ctx;
        mContext = ctx;
        currentPos = pos;
        dialogFiche = new Dialog(ctx);

        initGui();

        // Le contenu
        setContent(pos);
    }

    /**
     * Initialiser les éléments de la vue
     */
    private void initGui() {

        // pas de titre
        dialogFiche.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialogFiche.setContentView(R.layout.stock_item_detail);

        // les éléments de la vue
        buyableName = dialogFiche.findViewById(R.id.stock_new_item_details_ch_name);
        buyableUnitPrice = dialogFiche.findViewById(R.id.stock_new_item_unit_price);
        buyableNbLeft = dialogFiche.findViewById(R.id.stock_new_item_nb_left);
        buyableRef = dialogFiche.findViewById(R.id.stock_new_item_ref);
        Button cancel_btn = dialogFiche.findViewById(R.id.stock_new_item_cancel_btn);
        Button valid_btn = dialogFiche.findViewById(R.id.stock_new_item_valid_btn);
        Button modif_btn = dialogFiche.findViewById(R.id.stock_new_item_modify_btn);
        modif_line = dialogFiche.findViewById(R.id.stock_new_item_modify_line);
        normal_line = dialogFiche.findViewById(R.id.stock_new_item_normal_line);


        // Ajouter les évènements aux Boutons
        modif_btn.setOnClickListener(this);
        modif_btn.setTag(1);

        valid_btn.setOnClickListener(this);
        valid_btn.setTag(2);

        cancel_btn.setOnClickListener(this);
        cancel_btn.setTag(3);
    }

    /**
     * Afficher la boîte de dialogue
     */
    public void show() {
        // afficher la dialog
        dialogFiche.show();
    }

    /**
     * Activer le Mode de modification
     * @param pos la position de l'élément dont on doit afficher les données
     */
    @SuppressLint("SetTextI18n")
    private void setModifState(int pos) {
        // Visibilité des linearlayouts
        modif_line.setVisibility(View.VISIBLE);
        normal_line.setVisibility(View.GONE);

        // Récupérer les données
        String name = ControlStock.getInstance(mContext).getBuyableName(pos);
        int pU = ControlStock.getInstance(mContext).getBuyableCost(pos);
        final int stock = ControlStock.getInstance(mContext).getBuyableStock(pos);
        String ref = ControlStock.getInstance(mContext).getBuyableRef(pos);

        // affecter les données aux éléments de la vue
        buyableName.setEnabled(true);
        buyableUnitPrice.setEnabled(true);
        buyableName.setText(name);
        buyableNbLeft.setText(String.valueOf(stock));
        buyableRef.setText(ref);
        buyableUnitPrice.setText(pU + "");
    }

    /**
     * Activer le Mode Normal
     * @param pos la position de l'élément dont on doit afficher les données
     */
    @SuppressLint("SetTextI18n")
    private void setContent(int pos) {
        // Visibilité des linearlayouts
        modif_line.setVisibility(View.GONE);
        normal_line.setVisibility(View.VISIBLE);

        // Récupérer les données
        String name = ControlStock.getInstance(mContext).getBuyableName(pos);
        int pU = ControlStock.getInstance(mContext).getBuyableCost(pos);
        final int stock = ControlStock.getInstance(mContext).getBuyableStock(pos);
        String ref = ControlStock.getInstance(mContext).getBuyableRef(pos);

        // affecter les éléments
        buyableName.setEnabled(false);
        buyableUnitPrice.setEnabled(false);
        buyableName.setText(name);
        buyableNbLeft.setText(String.valueOf(stock));
        buyableRef.setText(ref);
        buyableUnitPrice.setText(pU + " F");
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switch (tag) {
            case 1:
                setModifState(currentPos);
                break;
            case 2:
                save(currentPos);
                break;
            case 3:
                setContent(currentPos);
                break;
        }
    }

    /**
     * Enregistrement
     * @param pos la position
     */
    private void save(int pos) {
        String name = buyableName.getText().toString().trim();
        int pu = 0;

        try {
            pu = Integer.parseInt(buyableUnitPrice.getText().toString());
        } catch (Exception ignore) {}

        if(name.isEmpty() || pu == 0) {
            Toast.makeText(mContext, "Saisie invalide.", Toast.LENGTH_SHORT).show();
        } else {
            if(ControlStock.getInstance(mContext).updateArticle(pos, name, pu)) {
                Toast.makeText(mContext, "Modifié avec succès.", Toast.LENGTH_SHORT).show();
                setContent(pos);

                try {
                    obs.querry("update", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Une erreur est survenue.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public List<ObserverView> getObserverList() {
        return observersList;
    }
}
