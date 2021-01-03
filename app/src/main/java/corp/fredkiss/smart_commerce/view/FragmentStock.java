package corp.fredkiss.smart_commerce.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlStock;

public class FragmentStock extends Fragment implements ObserverView {

    private  Context mContext = null;
    private StockAdapter_ mAdapter = null;

    public FragmentStock() {}

    public static FragmentStock init(Context ctx) {
        /*
         * Notice  : Never make a fragment a singleton because it needs to
         *           create a new instance and change context each time the view
         *           is called.
         * **/
        FragmentStock instance = new FragmentStock();
        instance.setContext(ctx);
        instance.createAdapter();
        instance.updateFragment();
        return instance;
    }

    private void createAdapter() {
        mAdapter = new StockAdapter_(mContext);
        mAdapter.getObserverList().clear();

        if(mAdapter.getObserverList().size() == 0)
            mAdapter.getObserverList().add(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);

        RecyclerView stockRv = rootView.findViewById(R.id.stock_rv);

        if(mAdapter != null) {
            stockRv.setAdapter(mAdapter);
            stockRv.setLayoutManager(new LinearLayoutManager(mContext));
        }

        TextView placeholder = rootView.findViewById(R.id.stock_placeholder_text);

        if(ControlStock.getInstance(mContext).getCount() > 0) {
            placeholder.setVisibility(View.GONE);
            stockRv.setVisibility(View.VISIBLE);
        } else {
            placeholder.setVisibility(View.VISIBLE);
            stockRv.setVisibility(View.GONE);
        }


        return rootView;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void querry(String arg, Object obj) throws Exception {
        if (arg != null) {
            switch (arg){
                case "modify":
                    int pos = (int) obj;
                    openModifyDialog(pos);
                    break;
                //                case "get_composition":
//                    try {
//                        // int pos = (int) obj;
//                        // openCompoDialog(pos);
//                    }catch (Exception e){
//                        throw new Exception(e.getMessage());
//                    }
//                    break;
            }

        } else {
            throw new Exception("arg must not be null !");
        }
    }

//    private void openCompoDialog(int pos) {
//        // TODO
//    }

    private void openModifyDialog(final int pos) {
        // la dialog
        final Dialog dialog_modif = new Dialog(getActivity());
        dialog_modif.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialog_modif.setContentView(R.layout.stock_manage_dialog);

        // Les boutons
        Button cancel_btn = dialog_modif.findViewById(R.id.stock_manage_dialog_cancel);
        Button valid_btn = dialog_modif.findViewById(R.id.stock_manage_dialog_valid);

        // les champs de saisie
        final EditText chNom = dialog_modif.findViewById(R.id.stock_manage_ch_name);
        final EditText chPU = dialog_modif.findViewById(R.id.stock_manage_ch_unit_price);

        // récupérer & affecter des valeurs aux champs
        chNom.setText(ControlStock.getInstance(mContext).getBuyableName(pos));
        chPU.setText(String.valueOf(ControlStock.getInstance(mContext).getBuyableCost(pos)));

        // au clic sur 'valider'
        valid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = chNom.getText().toString().trim();
                int cost = 0;

                try {
                    cost = Integer.parseInt(chPU.getText().toString());
                }catch (Exception ignore) {}

                if(cost > 0 && nom.length() > 0) {
                    boolean res =  ControlStock.getInstance(mContext).updateArticle(pos, nom, cost);

                    if(res)
                        Toast.makeText(mContext, "Modifié avec succès", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, "Une erreur est survenue !", Toast.LENGTH_SHORT).show();

                    // dans tous les cas, mettre à jour le fragment
                    updateFragment();
                    dialog_modif.dismiss();
                } else {
                    Toast.makeText(mContext, "Saisie invalide", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // au clic sur 'annuler'
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_modif.dismiss();
            }
        });

        // afficher la dialog
        dialog_modif.show();
    }

    @Override
    public Object getObserverState(String querry) {
        return null;
    }

    public StockAdapter_ getAdapter() {
        return mAdapter;
    }

    /**
     * Mettre à jour lA fenêtre
     */
    public void updateFragment() {
        ControlStock.getInstance(mContext).reload();
        getAdapter().updateAdapter();
    }

}
