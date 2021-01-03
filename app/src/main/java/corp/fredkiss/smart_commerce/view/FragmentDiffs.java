package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlDiff;
import corp.fredkiss.smart_commerce.controller.ElementNotExistException;

@SuppressLint("StaticFieldLeak")
public class FragmentDiffs extends Fragment {

    private  Context mContext = null;
    private static FragmentDiffs ourInstance = null;
    public DiffAdapter rvAdapter = null;
    private ObserverView mObserverView = null;
    private TextView mPlaceholder;
    private RecyclerView mRv;

    public void setObserverView(ObserverView observerView) {
        mObserverView = observerView;
    }

    public FragmentDiffs() {}

    public static FragmentDiffs init(Context context, ObserverView obs) {

        /*
         * Notice  : Never make a fragment a singleton because it needs to
         *           create a new instance and change context each time the view
         *           is called.
         * **/
        ourInstance = new FragmentDiffs();
        ourInstance.setObserverView(obs);
        ourInstance.setContext(context);
        ourInstance.createAdapter();
        return ourInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diff, container, false);

        // paramètres de la recyclerView
        mRv = rootView.findViewById(R.id.monney_rv);

        if(ourInstance.getRvAdapter() != null) {
            mRv.setAdapter(ourInstance.getRvAdapter());
            mRv.setLayoutManager(new LinearLayoutManager(mContext));
        }

        // le placeholder
        mPlaceholder = rootView.findViewById(R.id.diff_placeholder_text);

        // Mettre à jour le fragment
        updateFragment();

        return rootView;
    }

    /**
     * Créer l'adapter
     */
    private void createAdapter() {
        // instanciate the adapter
        rvAdapter = new DiffAdapter(mContext);

        rvAdapter.getObserverList().clear();
        // add the observer only once
        if(rvAdapter.getObserverList().size() == 0)
            rvAdapter.getObserverList().add(mObserverView);
    }


    /**
     *
     * @return  l'adapter
     */
    public static DiffAdapter getAdapter(){
        	return ourInstance.getRvAdapter();
        }


    /**
     * ouvrir les infos du créditeur
     * @param id son id
     */
    @SuppressLint("SetTextI18n")
    public static void openCredInfo(final int id){
            // l'instance du contrôle
            ControlDiff ctrlCred = ControlDiff.getInstance(ourInstance.getContext());
            List<String> list;

            try {
                list = ctrlCred.getCrediteurById(id);
            } catch (ElementNotExistException e) {
                e.printStackTrace();
                return;
            }

            // dialog de visualisation
            final Dialog dialog_viz = new Dialog(ourInstance.getActivity());
            dialog_viz.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // appliquer la vue
            dialog_viz.setContentView(R.layout.diff_dialog_visualization);

            // Les boutons
            Button cancel_btn = dialog_viz.findViewById(R.id.money_btn_viz_dialog_cancel);
            Button modif_btn = dialog_viz.findViewById(R.id.money_btn_viz_dialog_modify);

            // fermer le dialog
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_viz.cancel();
                }
            });

            // Ouvrir une autre fenêtre
            modif_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modifyCred(id);
                    dialog_viz.cancel();
                }
            });

            // Les textes
            TextView txt_name = dialog_viz.findViewById(R.id.money_nom_viz);
            TextView txt_chbre = dialog_viz.findViewById(R.id.money_chbre_viz);
            TextView txt_som = dialog_viz.findViewById(R.id.money_som_viz);

            // Charger & afficher le texte
            txt_name.setText(list.get(1));
            txt_chbre.setText(list.get(2));
            txt_som.setText(list.get(3) + " F");

            // afficher
            dialog_viz.show();
    }

    /**
     * Ajouter un créditeur
     */
    public static void addCred(){
        final Dialog dialog_add = new Dialog(ourInstance.getActivity());
        dialog_add.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialog_add.setContentView(R.layout.money_dialog_add);

        // Les boutons
        Button cancel_btn = dialog_add.findViewById(R.id.money_btn_add_dialog_cancel);
        Button add_btn = dialog_add.findViewById(R.id.money_btn_add_dialog_valid);

        // Les Spins
        final Spinner resid_spin = dialog_add.findViewById(R.id.money_resid_add_chbre_spinner);
        final Spinner chbre_spin = dialog_add.findViewById(R.id.money_no_add_chbre_spinner);


        /* Spinner de la résidence */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ourInstance.getContext(),
                R.array.resid_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        resid_spin.setAdapter(adapter);

        /* Spinner de la chambre */
        ArrayAdapter<Integer> adapter_2 = new ArrayAdapter<>(ourInstance.getContext(), android.R.layout.simple_spinner_item);
        for (int i = 1; i <= 96; i++) {
            adapter_2.add(i);
        }

        // Specify the layout to use when the list of choices appears
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        chbre_spin.setAdapter(adapter_2);

        // les editText
        final EditText chNom = dialog_add.findViewById(R.id.money_nom_add);
        final EditText chSom = dialog_add.findViewById(R.id.money_som_add);

        // fermer le dialog
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_add.cancel();
            }
        });

        // ajouter le créditeur
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = chNom.getText().toString();
                String chbre = resid_spin.getSelectedItem().toString() + "-" + chbre_spin.getSelectedItem().toString();
                int sum;

                try {
                    sum = Integer.parseInt(chSom.getText().toString());
                }catch (Exception ignored){
                    Toast.makeText(ourInstance.getContext(), "Saisie incorrecte !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sum <= 0 || nom.length() == 0){
                    Toast.makeText(ourInstance.getContext(), "Saisie incorrecte !", Toast.LENGTH_SHORT).show();

                } else {
                    // Le contrôleur
                    ControlDiff ctrlCred = ControlDiff.getInstance(ourInstance.getContext());

                    // Ajouter le créditeur & mettre à jour l'adapter
                    ctrlCred.addCrediteur(nom, chbre, sum);
                    getAdapter().updateAdapter();
                    updateFragment();
                    dialog_add.dismiss();
                }
            }
        });

        dialog_add.show();
    }

    /**
     * modifier les informations du créditeur
     * @param id son id
     */
    public static void modifyCred(final int id) {
        final Dialog dialog_add = new Dialog(ourInstance.getActivity());
        dialog_add.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // appliquer la vue
        dialog_add.setContentView(R.layout.money_dialog_add);

        // Les boutons
        Button cancel_btn = dialog_add.findViewById(R.id.money_btn_add_dialog_cancel);
        Button add_btn = dialog_add.findViewById(R.id.money_btn_add_dialog_valid);

        // Les Spins
        final Spinner resid_spin = dialog_add.findViewById(R.id.money_resid_add_chbre_spinner);
        final Spinner chbre_spin = dialog_add.findViewById(R.id.money_no_add_chbre_spinner);


        /* Spinner de la résidence */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ourInstance.getContext(),
                R.array.resid_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        resid_spin.setAdapter(adapter);

        /* Spinner de la chambre */
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(ourInstance.getContext(), android.R.layout.simple_spinner_item);
        for (int i = 1; i <= 96; i++) {
            adapter_2.add(String.valueOf(i));
        }

        // Specify the layout to use when the list of choices appears
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        chbre_spin.setAdapter(adapter_2);


        // les editText
        final EditText chNom = dialog_add.findViewById(R.id.money_nom_add);
        final EditText chSom = dialog_add.findViewById(R.id.money_som_add);

        // fermer le dialog
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_add.cancel();
            }
        });

        // ajouter le créditeur
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = chNom.getText().toString();
                String chbre = resid_spin.getSelectedItem().toString() + "-" + chbre_spin.getSelectedItem().toString();
                int sum;

                try {
                    sum = Integer.parseInt(chSom.getText().toString());
                }catch (Exception ignored){
                    Toast.makeText(ourInstance.getContext(), "Saisie incorrecte !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sum <= 0 || nom.length() == 0){
                    Toast.makeText(ourInstance.getContext(), "Saisie incorrecte !", Toast.LENGTH_SHORT).show();

                } else {
                    // Le contrôleur
                    ControlDiff ctrlCred = ControlDiff.getInstance(ourInstance.getContext());

                    // Ajouter le créditeur & mettre à jour l'adapter
                    ctrlCred.modifyCrediteur(id, nom, chbre, sum);
                    getAdapter().updateAdapter();
                    updateFragment();
                    dialog_add.dismiss();
                }
            }
        });

        ControlDiff ctrl = ControlDiff.getInstance(ourInstance.getContext());
        List<String> list;
        String resid;
        String no_chbre;

        try {
            list = ctrl.getCrediteurById(id);
        } catch (ElementNotExistException e) {
            e.printStackTrace();
            return;
        }

        // Charger & afficher le texte
        chNom.setText(list.get(1));
        resid = list.get(2).split("-")[0];
        no_chbre = list.get(2).split("-")[1];

        for (int i = 0; i < adapter.getCount(); i++) {
            if(resid.equalsIgnoreCase(resid_spin.getItemAtPosition(i).toString())) resid_spin.setSelection(i);
        }

        for (int i = 0; i < adapter_2.getCount(); i++) {
            if(no_chbre.equalsIgnoreCase(chbre_spin.getItemAtPosition(i).toString())) chbre_spin.setSelection(i);
        }

        chSom.setText(list.get(3));
        dialog_add.show();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void setContext(Context context) {
        mContext = context;
    }

    private DiffAdapter getRvAdapter() {
        return rvAdapter;
    }

    /**
     * Mettre à jour le fragment
     */
    public static void updateFragment() {
        if(ourInstance != null) {
            if (ControlDiff.getInstance(ourInstance.mContext).getCount() == 0) {
                ourInstance.mPlaceholder.setVisibility(View.VISIBLE);
                ourInstance.mRv.setVisibility(View.GONE);
            } else {
                ourInstance. mPlaceholder.setVisibility(View.GONE);
                ourInstance.mRv.setVisibility(View.VISIBLE);
            }
        }
    }

}
