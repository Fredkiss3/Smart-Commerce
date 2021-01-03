package corp.fredkiss.smart_commerce.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import corp.fredkiss.smart_commerce.R;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {

    private View.OnClickListener mParent;
    private Context mContext;
    private List<String> mArticleList = new ArrayList<>();

    ArticleListAdapter (View.OnClickListener parent, Context context) {
        mParent = parent;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.simple_article_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        holder.setArticleName(i);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mArticleName;
        private String article;
        private int currentPos = -1;

        MyViewHolder(View itemView) {
            super(itemView);

            // appliquer les modifications visuelles
            mArticleName = itemView.findViewById(R.id.simple_article_name);
            itemView.setOnClickListener(mParent);
        }

        void setArticleName(int pos) {
            if(mArticleList.size() > 0) {
                article = capitalizeWord(mArticleList.get(pos));
                mArticleName.setText(article);
                currentPos = pos;

                itemView.setTag(currentPos);
            }
        }

        /**
         * Mettre la première lettre d'un mot en majuscule
         * @param word le mot
         * @return le mot modifié
         */
        private String capitalizeWord(String word) {
            Character first = word.charAt(0);
            return word.replaceFirst(first.toString(), first.toString().toUpperCase());
        }
    }

    public void setArticleList(List<String> articleList) {
        mArticleList = articleList;
        notifyDataSetChanged();
    }
}
