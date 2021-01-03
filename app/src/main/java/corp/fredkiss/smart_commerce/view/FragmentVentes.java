package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlVentes;

@SuppressLint("StaticFieldLeak")
public class FragmentVentes extends Fragment implements View.OnClickListener{


    private  Context mContext = null;
    private TextView placeHolderText;
    private RecyclerView soldRv;

    private SoldAdapter adapter;

    private Dialog mDialogList = null;
    private static FragmentVentes ourInstance = null;
    public FragmentVentes() {}

    public static FragmentVentes init(Context ctx) {
        /*
         * Notice  : Never make a fragment a singleton because it needs to
         *           create a new instance and change context each time the view
         *           is called.
         * **/
        ourInstance = new FragmentVentes();
        ourInstance.setContext(ctx);
        return ourInstance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ventes, container, false);

        // Le texte de replacement
        placeHolderText = rootView.findViewById(R.id.fragment_sold_placeholder_text);
        soldRv = rootView.findViewById(R.id.fragment_sold_rv);

        // l'adapter
        adapter = new SoldAdapter(mContext);
        soldRv.setAdapter(adapter);
        soldRv.setLayoutManager(new LinearLayoutManager(mContext));

        // mettre à jour le fragment
        updateFragment();

        return rootView;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public SoldAdapter getAdapter() {
        return adapter;
    }

    /**
     * Mettre à jour le fragment
     */
    public static void updateFragment() {
        if(ourInstance != null) {
            // Recharger la BDD
            ControlVentes.getInstance(ourInstance.mContext).reload();

            // La visibilité des éléments
            if(ControlVentes.getInstance(ourInstance.mContext).getCount() > 0) {
                ourInstance.placeHolderText.setVisibility(View.GONE);
                ourInstance.soldRv.setVisibility(View.VISIBLE);
            } else {
                ourInstance.placeHolderText.setVisibility(View.VISIBLE);
                ourInstance.soldRv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Récupérer les détails d'une vente
     * @param pos la position
     */
    @SuppressLint("SetTextI18n")
    public static void openSoldDetails(int pos) {
        if(ourInstance != null) {
            // la dialog
            final Dialog dialogDetails = new Dialog(ourInstance.getActivity());

            // pas de titre
            dialogDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // appliquer la vue
            dialogDetails.setContentView(R.layout.sold_item_details);

            String name = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyableName(pos);
            String date = ControlVentes.getInstance(ourInstance.mContext).getDateJournal();
            int  prixU = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyablePrixUnitaire(pos);
            int nbBought = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyableNbBought(pos);
            int nbLost = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyableNbLost(pos);
            int totalBought = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyableTotalBought(pos);
            int totalLost = ControlVentes.getInstance(ourInstance.mContext).getJournalBuyableTotalLost(pos);

            // les éléments de la vue
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_name)).setText(name);
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_date)).setText(date);
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_unit_price)).setText(prixU + " F");
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_nb_bought)).setText("Vendus x " + nbBought);
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_nb_lost)).setText("Perdus  x " + nbLost);
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_total_bought)).setText(totalBought + " F");
            ((TextView) dialogDetails.findViewById(R.id.sold_item_details_total_lost)).setText(totalLost + " F");

            // Le bouton
            dialogDetails.findViewById(R.id.sold_item_details_ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDetails.dismiss();
                }
            });

            // affiocher la dialog
            dialogDetails.show();

        }

    }

    /**
     * Ajouter une nouvelle vente
     * @param pos la position de l'élément dans la liste des éléments
     */
    @SuppressLint("SetTextI18n")
    private void addVente(final int pos) {
        // la dialog
        final Dialog dialogFiche = new Dialog(getActivity());

        // pas de titre
        dialogFiche.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialogFiche.setContentView(R.layout.sold_item_filling_card);

        // les éléments de la vue
        TextView buyableName = dialogFiche.findViewById(R.id.sold_item_filling_card_name);
        TextView date = dialogFiche.findViewById(R.id.sold_item_filling_card_date);
        TextView prixU = dialogFiche.findViewById(R.id.sold_item_filling_card_unit_price);
        final EditText nbBought = dialogFiche.findViewById(R.id.sold_item_filling_card_nb_bought);
        final EditText nbLost = dialogFiche.findViewById(R.id.sold_item_filling_card_nb_lost);
        Button cancel_btn = dialogFiche.findViewById(R.id.sold_item_filling_card_cancel_btn);
        Button valid_btn = dialogFiche.findViewById(R.id.sold_item_filling_card_valid_btn);

        // todo
        String name = ControlVentes.getInstance(mContext).getBuyableNamesList().get(pos);
        final int pU = ControlVentes.getInstance(mContext).getPrixUnitaire(pos);
        String date_j = ControlVentes.getInstance(mContext).getDateJournal();

        // affecter les éléments
        buyableName.setText(name);
        date.setText(date_j);
        prixU.setText(String.valueOf(pU) + " F");

        // Bouton annuler
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFiche.cancel();
            }
        });

        // Bouton valider
        valid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(pU == 0) {
                       showNotice();
                       dialogFiche.cancel();
                   } else {
                       int bought = 0;
                       int lost = 0;

                       try {
                           bought = Integer.parseInt(nbBought.getText().toString());
                       }catch (Exception ignore) {}

                       try {
                           lost = Integer.parseInt(nbLost.getText().toString());
                       }catch (Exception ignore) {}

                       if(bought > 0 || lost > 0) {
                          if(ControlVentes.getInstance(mContext).sell(pos, bought, lost)) {
                              Toast.makeText(mContext, "Vente effectuée avec succès",
                                      Toast.LENGTH_SHORT).show();

                              getAdapter().updateAdapter();
                              dialogFiche.dismiss();
                          } else {
                              showWarning();
                          }

                       } else {
                           Toast.makeText(mContext, "Saisie invalide.", Toast.LENGTH_SHORT).show();
                       }


                   }
            }
        });

        // afficher la dialog
        dialogFiche.show();
    }

    public static void  openBuyableList() {
        if(ourInstance != null) {
            // la dialog
            final Dialog dialogList = new Dialog(ourInstance.getActivity());

            // pointeur vers ce dialog
            ourInstance.mDialogList = dialogList;

            // pas de titre
            dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // appliquer la vue
            dialogList.setContentView(R.layout.article_list);

            // Modifier le titre
            ((TextView)dialogList.findViewById(R.id.article_list_title)).setText("Articles disponibles");

            // paramètres de la recyclerView
            final RecyclerView article_list_rv = dialogList.findViewById(R.id.article_list_rv);
            final ArticleListAdapter adapter = new ArticleListAdapter(ourInstance,  ourInstance.getContext());

            List<String> list = ControlVentes.getInstance(ourInstance.getContext()).getBuyableNamesList();
            adapter.setArticleList(list);

            article_list_rv.setAdapter(adapter);
            article_list_rv.setLayoutManager(new LinearLayoutManager(ourInstance.getContext()));

            dialogList.show();
        }
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        addVente(pos);
        mDialogList.dismiss();
    }

    /**
     * demander d'initialiser les prix d'achats
     */
    public void showNotice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("NOTICE")
                .setMessage(R.string.notice)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * Afficher l'erreur
     */
    public void showWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ALERTE")
                .setMessage("Vous ne pouvez pas vendre plus que le stock")
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
