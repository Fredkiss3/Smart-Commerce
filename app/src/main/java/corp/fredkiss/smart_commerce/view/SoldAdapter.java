package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlVentes;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.MyViewHolder> {

    private Context mContext;
    private ControlVentes mControlVentes;

    SoldAdapter(Context context) {
        mContext = context;
        mControlVentes = ControlVentes.getInstance(mContext);
        mControlVentes.reload();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.sold_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.setCurrentSold(i);
    }

    @Override
    public int getItemCount() {
        return mControlVentes.getCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mTotalBought;
        private TextView mTotalLost;
        private int currentPos = 0;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.sold_item_name);
            mTotalBought = itemView.findViewById(R.id.sold_item_total_bought);
            mTotalLost = itemView.findViewById(R.id.sold_item_total_lost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentVentes.openSoldDetails(currentPos);
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void setCurrentSold(int pos) {
            mName.setText(mControlVentes.getJournalBuyableName(pos));
            mTotalBought.setText("+ " + mControlVentes.getJournalBuyableTotalBought(pos) + " F");
            mTotalLost.setText("- " + mControlVentes.getJournalBuyableTotalLost(pos) + " F");
            currentPos = pos;
        }

    }


    void updateAdapter() {
        notifyDataSetChanged();
        FragmentVentes.updateFragment();
    }

}
