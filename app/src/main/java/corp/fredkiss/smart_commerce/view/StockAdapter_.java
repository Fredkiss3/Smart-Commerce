package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlStock;

// todo
public class StockAdapter_ extends RecyclerView.Adapter<StockAdapter_.MyViewHolder> implements ObservableView{

    private ControlStock mControlStock;
    private Context mContext;

    StockAdapter_(Context context) {
        mContext = context;
        mControlStock = ControlStock.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.stock_article_item_, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
        private TextView articleUnitPrice;
        private TextView articleRef;
        private TextView articleType;
        private LinearLayout articleDetails;
        private Button actionBtn;


        MyViewHolder(final View itemView) {
            super(itemView);

            // récupérer les éléments
            articleName = itemView.findViewById(R.id.stock_article_name);
            articleStock = itemView.findViewById(R.id.stock_article_stock);
            articleUnitPrice = itemView.findViewById(R.id.stock_article_unit_price);
            articleRef = itemView.findViewById(R.id.stock_article_reference);
            articleType = itemView.findViewById(R.id.stock_article_type);
            articleDetails = itemView.findViewById(R.id.stock_article_info_linearlayout);
            actionBtn = itemView.findViewById(R.id.stock_article_modify_btn);

            // changer la police
            articleName.setTypeface(TypeFaceUtil.getFont(mContext));
            articleStock.setTypeface(TypeFaceUtil.getFont(mContext));
            articleUnitPrice.setTypeface(TypeFaceUtil.getFont(mContext));
            articleRef.setTypeface(TypeFaceUtil.getFont(mContext));
            articleType.setTypeface(TypeFaceUtil.getFont(mContext));

            // état de départ plié
            articleDetails.setVisibility(View.GONE);

            // au clic
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(articleDetails.getVisibility() == View.VISIBLE){
                        // si élargi -> plier
                        articleDetails.setVisibility(View.GONE);

                    } else {
                        // si plié -> élargir
                        articleDetails.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void setCurrentBuyable(final int pos) {
            articleName.setText(capitalizeWord(mControlStock.getBuyableName(pos)));
            articleStock.setText(String.valueOf(mControlStock.getBuyableStock(pos)));
            articleUnitPrice.setText(String.valueOf(mControlStock.getBuyableCost(pos)) + " F");
            articleRef.setText(mControlStock.getBuyableRef(pos));

            if(mControlStock.isCombo(pos)) {
                try {
                    articleType.setText("Pack de " + mControlStock.getComboCount(pos) +" articles");
                    actionBtn.setText("COMPOSITION");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                articleType.setText("Article Simple");
                actionBtn.setText("MODIFIER");
            }

            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ObserverView obs = observersList.get(0);
                        if(mControlStock.isCombo(pos)) {
                            obs.querry("get_composition", pos);
                        } else {
                            obs.querry("modify", pos);
                        }

                    }catch (NullPointerException e){
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


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
