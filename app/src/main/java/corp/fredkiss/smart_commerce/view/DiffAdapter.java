package corp.fredkiss.smart_commerce.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlDiff;


public class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.MyViewHolder> implements ObservableView {

    private Context mContext;
    private static List<Integer> mToDeleteList = new ArrayList<>();
    private static List<CheckBox> mCheckedViews = new ArrayList<>();

    // control créditeurs
    private ControlDiff mControlDiff;

    // pour savoir si l'adapter est en train de supprimer
    private boolean is_deleting = false;

    DiffAdapter(Context context) {
        mContext = context;
        mControlDiff = ControlDiff.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.diff_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setCurrentCred(position);
    }

    /**
    *@return la taille de la liste des créditeurs 
    */
    @Override
    public int getItemCount() {
        return mControlDiff.getCount();
    }

    @Override
    public List<ObserverView> getObserverList() {
        return observersList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        // éléments de la vue
        private TextView CredName;
        private TextView CredSom;
        private CheckBox mCheckBox;

        // attributs
        private String mNomCred;
        private int mSomCred;
        private int mIdCred;

        // pour savoir si la vue est sélectionnée
//        private boolean is_selected = false;

        MyViewHolder(final View itemView) {
            super(itemView);

            // Labels pour le nom et la somme
            CredName = itemView.findViewById(R.id.diff_user_name);
            CredSom = itemView.findViewById(R.id.diff_money);
            mCheckBox = itemView.findViewById(R.id.diff_check);
            mCheckBox.setChecked(false);

            mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!is_deleting) {
                        try {
                            ObserverView obs = observersList.get(0);
                            boolean inActionMode = (boolean) obs.getObserverState("in_action_mode");

                            if(mCheckBox.isChecked()) {
                                if(!inActionMode) {
                                    try {
                                        obs.querry("enable", null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                mToDeleteList.add(mIdCred);
                                mCheckedViews.add(mCheckBox);
                            } else {
                                if (inActionMode) {
                                    mCheckedViews.remove(mCheckBox);
                                    mToDeleteList.remove(Integer.valueOf(mIdCred));

                                    if (mToDeleteList.size() == 0) {
                                        // disable action mode
                                        try {
                                            obs.querry("disable", null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            // querry counter
                            obs.querry("querry", mToDeleteList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    }
                });

            // au clic
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentDiffs.openCredInfo(mIdCred);
                }
            });
        }

        void setCurrentCred(int pos) {
            mNomCred = mControlDiff.getNomCred(pos);
            mSomCred = mControlDiff.getSomCred(pos);
            mIdCred = mControlDiff.getIdCred(pos);

            // affecter le texte
            CredName.setText(mNomCred);

            // La somme
            String som = String.valueOf(mSomCred) + " F";
            CredSom.setText(som);
        }
    }

    /**
     * Supprimer effectivement
     */
    void deleteSelected() {
	    	// supprimer chaque créditeur sélectionné
			for( int  id : mToDeleteList){
                mControlDiff.removeCrediteur(id);
            }

            if(mToDeleteList.size() == 0)
                Toast.makeText(mContext, "Aucun élément supprimé",
                        Toast.LENGTH_SHORT).show();
            else if(mToDeleteList.size() == 1)
                Toast.makeText(mContext, mToDeleteList.size() + " élément supprimé",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext, mToDeleteList.size() + " éléments supprimés",
                        Toast.LENGTH_SHORT).show();

			// tout desélectionner & mettre à jour les créditeurs
			unSelectAll();
    }

    /**
     * enlever l'etat sélectionné à toutes les vues qui ont été sélectionnées
     * Cette méthode est appelée
     */
    void unSelectAll() {
        is_deleting = true;

        for (CheckBox c: mCheckedViews ) {
            c.setChecked(false);
        }

        // mettre à jour l'adapter
        updateAdapter();
    }

    /**
     * Mettre à jour l'adapter
     */
    void updateAdapter(){
        // Vider la liste des vues sélectionnées & des éléments sélectionnés
        mCheckedViews.clear();
        mToDeleteList.clear();

        // mettre à jour le dataSet
        notifyDataSetChanged();
        FragmentDiffs.updateFragment();
        is_deleting = false;
    }

}
