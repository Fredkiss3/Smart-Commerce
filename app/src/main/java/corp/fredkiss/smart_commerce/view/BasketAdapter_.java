package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlAppro;

public class BasketAdapter_ extends RecyclerView.Adapter<BasketAdapter_.MyViewHolder>{

    private Context mContext;
    private ControlAppro mControlAppro;

    BasketAdapter_(Context context) {
        mContext = context;
        mControlAppro = ControlAppro.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.basket_item_bought, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setCurrentArticle(position);
    }

    @Override
    public int getItemCount() {
        return mControlAppro.getBasketListCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private String mArticleName;
        private int mArticleNbBought;
        private int mArticleGCost;

        // éléments de la vue
        private TextView mItemName;
        private TextView mItemTotalBought;
        private TextView mItemGlobalPrice;
        private ImageButton mItemEditBtn;
        private ImageButton mRemoveBtn;
        private boolean isCreated = false;


        MyViewHolder(View itemView) {
            super(itemView);

            // affecter les différents éléments de la vue
            mItemName = itemView.findViewById(R.id.basket_item_name);
            mItemTotalBought = itemView.findViewById(R.id.basket_item_total_bought);
            mItemGlobalPrice = itemView.findViewById(R.id.basket_item_global_price);
            mItemEditBtn = itemView.findViewById(R.id.basket_item_edit_btn);
            mRemoveBtn = itemView.findViewById(R.id.basket_item_delete_btn);

            // police personnalisée
            mItemName.setTypeface(TypeFaceUtil.getFont(mContext));
            mItemTotalBought.setTypeface(TypeFaceUtil.getFont(mContext));
            mItemGlobalPrice.setTypeface(TypeFaceUtil.getFont(mContext));

            // au clic sur 'éditer'
            mItemEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        FragmentBasket.editArticle(getAdapterPosition(), isCreated);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            // au clic sur 'retirer'
            mRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentBasket.removeArticle(getAdapterPosition());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void setCurrentArticle(int pos) {
            mArticleName = mControlAppro.getBasketArticleName(pos);
            mArticleNbBought = mControlAppro.getBasketArticleNbBought(pos);
            mArticleGCost = mControlAppro.getBasketArticleGCost(pos);
            isCreated = mControlAppro.isCreated(pos);

            // nomde l'article
            mItemName.setText(mArticleName);

            // nombre achetés
            mItemTotalBought.setText("x " + mArticleNbBought);
            mItemGlobalPrice.setText(mArticleGCost + " F");
        }
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }
}
