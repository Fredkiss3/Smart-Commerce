package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlAppro;

@SuppressLint("StaticFieldLeak")
public class FragmentBasket extends Fragment implements View.OnClickListener{

    private Context mContext;
    private BasketAdapter_ mBasketAdapter;
    private static FragmentBasket ourInstance = null;
    private TextView mBasketTitle;
    private TextView mBasketDate;
    private TextView mBasketTotalSom;
    private EditText mBasketSomDepSup;
    private ImageView mBasKetColExpImg;
    private RecyclerView basket_list_rv;
    private CardView mBasketCard;
    private Dialog mDialogList = null;

    public FragmentBasket() {
    }

    public static FragmentBasket init(Context ctx) {

        ourInstance = new FragmentBasket();
        ourInstance.setContext(ctx);

        // mettre à jour le fragment
        ourInstance.cancel();

        return ourInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appro, container, false);
        // initialiser les éléments de la vue
        initGui(rootView);
        return rootView;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * Initialiser le gui
     * @param rootView la vue racine
     */
    private void initGui(View rootView) {
        // paramètres de la recyclerView
        basket_list_rv = rootView.findViewById(R.id.basket_list_rv);
        mBasketAdapter = new BasketAdapter_(mContext);
        basket_list_rv.setAdapter(mBasketAdapter);
        basket_list_rv.setLayoutManager(new LinearLayoutManager(mContext));
        basket_list_rv.setVisibility(View.GONE);

        // les textviews
        mBasketTitle = rootView.findViewById(R.id.basket_title);
        mBasketTotalSom = rootView.findViewById(R.id.basket_total_som);
        mBasketDate = rootView.findViewById(R.id.basket_date);
        mBasketSomDepSup = rootView.findViewById(R.id.basket_som_dep_sup);
        ImageButton basKetClrBtn = rootView.findViewById(R.id.basket_clear_btn);

        setTextViews();

        // les autres éléments
        LinearLayout basKetColExpLine = rootView.findViewById(R.id.basket_collapse_expand_linearlayout);
        LinearLayout basKetAddLine = rootView.findViewById(R.id.basket_add_linearlayout);
        mBasKetColExpImg = rootView.findViewById(R.id.basket_collapse_expand_btn);
        mBasketCard = rootView.findViewById(R.id.basket_card);

        // police personnalisée
        mBasketTitle.setTypeface(TypeFaceUtil.getFont(mContext));
        mBasketTotalSom.setTypeface(TypeFaceUtil.getFont(mContext));
        mBasketDate.setTypeface(TypeFaceUtil.getFont(mContext));
        mBasketSomDepSup.setTypeface(TypeFaceUtil.getFont(mContext));

        // au clic sur "ajouter"
        basKetAddLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListArticle();
            }
        });

        // au clic sur "liste d'achats"
        basKetColExpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (basket_list_rv.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(mBasketCard);
                    basket_list_rv.setVisibility(View.VISIBLE);
                    mBasKetColExpImg.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                } else {
                    mBasKetColExpImg.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                    TransitionManager.beginDelayedTransition(mBasketCard);
                    basket_list_rv.setVisibility(View.GONE);
                }
            }
        });

        // au clic sur le bouton 'corbeille'
        basKetClrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    /**
     * Affecter le contexte
     * @param context contexte
     */
    private void setContext(Context context) {
        mContext = context;
    }

    /**
     * Récupérer l'adapter
     * @return l'adapter
     */
    public static BasketAdapter_ getAdapter() {
        return ourInstance.mBasketAdapter;
    }

    /**
     * Modifier un article dans le panier d'achats
     * @param pos la position
     * @param isCreated si l'article a été créé
     */
    public static void editArticle(int pos, boolean isCreated) {
        ourInstance.openArticleFillingCard(2, pos, isCreated);
        getAdapter().updateAdapter();
    }

    /**
     * Ajouter un article dans le panier d'achats
     * @param pos la position
     */
    public static void addArticle(int pos) {
        ourInstance.openArticleFillingCard(3, pos, false);
        getAdapter().updateAdapter();
    }

    /**
     * Créer un article dans le panier d'achats
     */
    public static void createArticle() {
        ourInstance.openArticleFillingCard(1, 0, true);
        getAdapter().updateAdapter();
    }

    /**
     * Retirer un article du panier d'achats
     * @param pos la position de l'article dans le panier d'achats
     */
    public static void removeArticle(int pos) {
        if (ourInstance != null) {
            ourInstance.askConfirmForRemove(pos);
        }
    }

    /**
     * vider le panier d'achats
     */
    public void cancel() {
        ControlAppro.getInstance(mContext).cancel();
        Toast.makeText(mContext, "Panier d'achats vidé", Toast.LENGTH_SHORT).show();
       updateFragment();
    }

    /**
     * Essayer d'enlever un article
     */
    private void tryRemove(int pos) {
        boolean removed = ControlAppro.getInstance(ourInstance.mContext).removeArticle(pos);
        if (removed) {
            Toast.makeText(ourInstance.mContext, "article retiré", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ourInstance.mContext, "Error : Can't remove this article", Toast.LENGTH_SHORT).show();
        }

        // mettre à jour le fragment et l'adapter
        updateFragment();
    }

    /**
     * Enregistrer le panier
     */
    public static void askSave() {
        if(ControlAppro.getInstance(ourInstance.getContext()).getBasketListCount() > 0) {
           ourInstance.askConfirmForSave();
        } else {
            Toast.makeText(ourInstance.getContext(), "panier vide non enregistré !", Toast.LENGTH_SHORT).show();
        }

        // mettre à jour le Fragment
        ourInstance.updateFragment();
    }

    private boolean save() {
        boolean saved = false;

        try {
            // récupérer les dépenses supplémentaires
            int depSup = 0;
            try {
                depSup = Integer.parseInt(ourInstance.mBasketSomDepSup.getText().toString());
            } catch (Exception ignore) {
            }

            // ajouter les dépenses sup & enregistrer
            ControlAppro.getInstance(ourInstance.getContext()).addDepSup(depSup);
            ControlAppro.getInstance(ourInstance.getContext()).save();

            saved = true;
        } catch (Exception e) {
            Toast.makeText(ourInstance.getContext(), "An error occured while saving !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return saved;
    }

    /**
     * affecter les valeurs des textviews
     */
    private void setTextViews() {
        String title = "Panier d'achats n° " + String.valueOf(
                ControlAppro.getInstance(mContext).getIdPanier());
        String depSup = ControlAppro.getInstance(mContext).getDepSup();

        // date
        if(mBasketDate != null)
            mBasketDate.setText(ControlAppro.getInstance(mContext).getDatePanier());

        // titre
        if(mBasketTitle != null)
            mBasketTitle.setText(title);

        // dépenses supplémentaires
        if(mBasketSomDepSup != null)
            mBasketSomDepSup.setText(depSup);

        // somme totale du panier
        setBasketTotalSom();
    }

    /**
     * Ouvrir la fiche de modification / ajout / création d'un article
     * @param mode le mode d'ouverture de la fiche : <br>
     *              1 -> création <br>
     *              2 -> modification
     *              3 -> ajout
     * @param pos la position
     * @param isCreated si un article est créé
     */
    private void openArticleFillingCard(final int mode, final int pos, final boolean isCreated) {

        // la dialog
        final Dialog dialog_fil = new Dialog(getActivity());
        dialog_fil.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialog_fil.setContentView(R.layout.basket_article_filling_card);

        // Les boutons
        Button cancel_btn = dialog_fil.findViewById(R.id.basket_btn_filling_dialog_cancel);
        Button valid_btn = dialog_fil.findViewById(R.id.basket_btn_filling_dialog_valid);

        // les editText
        final EditText chNom = dialog_fil.findViewById(R.id.basket_ch_filling_name);
        final EditText chUCost = dialog_fil.findViewById(R.id.basket_ch_filling_unit_price);
        final EditText chGCost = dialog_fil.findViewById(R.id.basket_ch_filling_global_price);
        final EditText chNbBought = dialog_fil.findViewById(R.id.basket_ch_filling_nb_boughts);
        final TextView dialogTitle = dialog_fil.findViewById(R.id.basket_filling_card_title);

        String articleName = "";
        int articleUCost = 0;
        int articleGCost = 0;
        int articleNbBought = 0;

        // les différents modes
        switch (mode) {
            case 1:
                // création -> nothing
                dialogTitle.setText(R.string.basket_article_filling_card_title_create);
                break;
            case 2:
                // édition -> récupérer les données
                dialogTitle.setText(R.string.basket_article_filling_card_title_edit);
                articleName = ControlAppro.getInstance(mContext).getBasketArticleName(pos);
                articleUCost = ControlAppro.getInstance(mContext).getBasketArticleBuyingCost(pos);
                articleGCost = ControlAppro.getInstance(mContext).getBasketArticleGCost(pos);
                articleNbBought = ControlAppro.getInstance(mContext).getBasketArticleNbBought(pos);
                break;
            case 3:
                // ajout de l'ancien
                dialogTitle.setText(R.string.basket_article_filling_card_title_add);
                articleName = ControlAppro.getInstance(mContext).getArticleName(pos);
                articleUCost = ControlAppro.getInstance(mContext).getArticleBuyingCost(pos);
                break;
        }


        // A l'ajout d'un chiffre
        View.OnKeyListener editorListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try {
                    int uCost = Integer.parseInt(chUCost.getText().toString());
                    int nbBought = Integer.parseInt(chNbBought.getText().toString());
                    int gCost = uCost * nbBought;
                    chGCost.setText(String.valueOf(gCost));
                } catch (Exception ignore) {
                    chGCost.setText("0");
                }
                return false;
            }
        };

        // Au changement de Focus
        View.OnFocusChangeListener onFocusChange = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    int uCost = Integer.parseInt(chUCost.getText().toString());
                    int nbBought = Integer.parseInt(chNbBought.getText().toString());
                    int gCost = uCost * nbBought;
                    chGCost.setText(String.valueOf(gCost));
                } catch (Exception ignore) {
                    chGCost.setText("0");
                }
            }
        };

        // Appliquer les listeners
        chNbBought.setOnKeyListener(editorListener);
        chNbBought.setOnFocusChangeListener(onFocusChange);
        chNbBought.setOnFocusChangeListener(onFocusChange);
        chUCost.setOnKeyListener(editorListener);
        chGCost.setOnKeyListener(editorListener);

        // attention au champ nom qui ne doit pas changer
        chNom.setText(articleName);
        chNom.setEnabled(isCreated);

        // affecter les différents champs
        chUCost.setText(String.valueOf(articleUCost));
        chGCost.setText(String.valueOf(articleGCost));
        chNbBought.setText(String.valueOf(articleNbBought));

        // fermer le dialog
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_fil.cancel();
            }
        });

        // clic sur le bouton 'valider'
        valid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = chNom.getText().toString().trim();
                int uCost = 0;
                int globalCost;
                int nbBought = 0;

                // convertir les nombres
                try {
                    uCost = Integer.parseInt(chUCost.getText().toString());
                }catch (Exception ignore) {}

                try {
                    nbBought = Integer.parseInt(chNbBought.getText().toString());
                }catch (Exception ignore) {}

                // Calcul de prix global
                globalCost = uCost * nbBought;

                // Il faut que les trois champs correspondant aux nombres soient supérieurs à 0
                if (!(globalCost > 0 && nbBought > 0 && name.length() > 0)) {
                    Toast.makeText(mContext, "Saisie invalide", Toast.LENGTH_SHORT).show();
                }
                else {
                    // si tout se passe bien
                    switch (mode) {
                        case 1:
                            // création
                            boolean created = ControlAppro.getInstance(mContext).
                                    createArticle(name, uCost, nbBought, globalCost);
                            if (created) {
                                Toast.makeText(mContext, "créé avec succès", Toast.LENGTH_SHORT).show();

                                // calculer et affecter une nouvelle somme totale au panier
                                setBasketTotalSom();
                                getAdapter().updateAdapter();
                            } else {
                                Toast.makeText(mContext, "An error occured on creation", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case 2:
                            // édition
                            boolean modified = ControlAppro.getInstance(mContext).
                                    modifyArticle(pos, name, uCost, nbBought, globalCost);
                            if ( modified ) {
                                Toast.makeText(mContext, "modifié avec succès", Toast.LENGTH_SHORT).show();

                                // calculer et affecter une nouvelle somme totale au panier
                                setBasketTotalSom();
                                getAdapter().updateAdapter();
                            }
                            else {
                                Toast.makeText(mContext, "An error occured on modification", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 3:
                            // ajout d'un ancien article
                            boolean added = ControlAppro.getInstance(mContext).
                                    addArticle(pos, uCost, nbBought, globalCost);
                            if (added) {
                                // mettre à jour la somme et l'adapter
                                setBasketTotalSom();
                                getAdapter().updateAdapter();
                                Toast.makeText(mContext, "ajouté avec succès", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "l'article existe déjà dans le panier",
                                        Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    // fermer la dialog
                    dialog_fil.dismiss();

                }
            }
        });

        // afficher la dialog
        dialog_fil.show();
    }

    /**
     * Ouvrir la liste des articles enregistrés
     */
    private void openListArticle() {
        // la dialog
        final Dialog dialogList = new Dialog(getActivity());

        // pointeur vers ce dialog
        mDialogList = dialogList;

        // pas de titre
        dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialogList.setContentView(R.layout.basket_list_articles);

        final LinearLayout create_aticle_line = dialogList.findViewById(R.id.basket_create_article_linearlayout);

        create_aticle_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createArticle();
                dialogList.dismiss();
            }
        });

        // paramètres de la recyclerView
        final RecyclerView basket_article_list_rv = dialogList.findViewById(R.id.basket_article_list_rv);
        final ArticleListAdapter adapter = new ArticleListAdapter(this, mContext);

        List<String> list = ControlAppro.getInstance(mContext).getArticleNameList();
        adapter.setArticleList(list);

        basket_article_list_rv.setAdapter(adapter);
        basket_article_list_rv.setLayoutManager(new LinearLayoutManager(mContext));

        dialogList.show();
    }

    /**
     * Mettre à jour la somme totale du panier
     */
    private void setBasketTotalSom() {
        String totalSom = ControlAppro.getInstance(ourInstance.mContext).getTotalCost() + " F";

        if(mBasketTotalSom != null)
            mBasketTotalSom.setText(totalSom);
    }

    /**
     * Mettre à jour le fragment
     */
    public void updateFragment() {
        setTextViews();
        if(mBasketAdapter != null)
            mBasketAdapter.updateAdapter();
    }

    /**
     * Demander pour une confirmation
     */
    private void askConfirmForRemove(final int pos) {
           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
           builder.setTitle("Retirer")
                   .setMessage("Retirer cet article ?")
                   .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           tryRemove(pos);
                       }
                   })
                   .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                       }
                   })
                   .setCancelable(false)
                   .create()
                   .show();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        addArticle(pos);
        mDialogList.dismiss();
    }

    /**
     * demander d'initialiser les prix d'achats
     */
    public void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("NOUVEAUX ARTICLES DETECTES")
                .setMessage(R.string.important_initialisation)
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
     * Demander pour une confirmation
     */
    private void askConfirmForSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Retirer")
                .setMessage("Confirmer l'enregistrement des articles ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ourInstance.save()) {
                            Toast.makeText(ourInstance.getContext(), "enregistré " +
                                    "avec succès.", Toast.LENGTH_SHORT).show();
                            if(ControlAppro.getInstance(ourInstance.getContext()).getCreatedCount() > 0)
                                ourInstance.showTips();
                        } else {
                            Toast.makeText(ourInstance.getContext(), "non enregistré !", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
