package corp.fredkiss.smart_commerce.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import corp.fredkiss.smart_commerce.R;

@SuppressLint("StaticFieldLeak")
public class FragmentEmprunts extends Fragment {

    private Context mContext = null;

    public FragmentEmprunts() {}

    public static FragmentEmprunts init(Context ctx) {
        /*
         * Notice  : Never make a fragment a singleton because it needs to
         *           create a new instance and change context each time the view
         *           is called.
         * **/
        FragmentEmprunts ourInstance = new FragmentEmprunts();
        ourInstance.setContext(ctx);
        return ourInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emprunts, container, false);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

}
