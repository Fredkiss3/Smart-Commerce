package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlStock;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> implements ObservableView{

    private ControlStock mControlStock;
    private Context mContext;

    StockAdapter(Context context) {
        mContext = context;
        mControlStock = ControlStock.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.stock_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setCurrentBuyable(position);
    }

    @Override
    public int getItemCount() {
        return mControlStock.getCount();
    }

    @Override
    public List<ObserverView> getObserverList() {
        return observersList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView articleName;
        private TextView articleStock;
        private int currentPos;


        MyViewHolder(final View itemView) {
            super(itemView);

            // récupérer les éléments
            articleName = itemView.findViewById(R.id.stock_new_article_name);
            articleStock = itemView.findViewById(R.id.stock_new_article_nb_left);

            // au clic
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                           ObserverView obs = observersList.get(0);
                            obs.querry("open_details", currentPos);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void setCurrentBuyable(int pos) {
            articleName.setText(capitalizeWord(mControlStock.getBuyableName(pos)));
            articleStock.setText("RESTANT : " + String.valueOf(mControlStock.getBuyableStock(pos)));
            currentPos = pos;
        }

        /**
         * Mettre la première lettre d'un mot en majuscule
         * @param word le mot
         * @return le mot modifié
         */
        private String capitalizeWord(String word) {
            char first = word.charAt(0);
            return word.replace(Character.toString(first), Character.toString(first).toUpperCase());
        }
    }

    /**
     * Mettre à jour l'adapter
     */
    void updateAdapter() {
        notifyDataSetChanged();
    }

}
