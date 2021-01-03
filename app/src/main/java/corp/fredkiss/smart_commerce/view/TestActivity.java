package corp.fredkiss.smart_commerce.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import corp.fredkiss.smart_commerce.R;
import corp.fredkiss.smart_commerce.controller.ControlDiff;
import corp.fredkiss.smart_commerce.controller.ElementNotExistException;
import corp.fredkiss.smart_commerce.model.User;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnAdd;
    private Button mBtnView;
    private Button mBtnDel;
    private Button mBtnModif;
    private EditText mChNom;
    private EditText mChChbre;
    private EditText mChSom;
    private EditText mChId;
    private ControlDiff mControlCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        test();
    }

    private void test() {
        User user = new User("mark", "wtf");
        String msg = "username : " + user.getName() + "\n" + "password : " + user.getPassword();
        showMessage("result", msg);
    }

    private void init() {
        // bouton
        mBtnAdd = findViewById(R.id.tes_btn_add);
        mBtnView = findViewById(R.id.tes_btn_view_all);
        mBtnDel = findViewById(R.id.tes_btn_remove);
        mBtnModif = findViewById(R.id.test_btn_modif);

        // tags
        mBtnAdd.setTag(1);
        mBtnView.setTag(2);
        mBtnDel.setTag(3);
        mBtnModif.setTag(4);

        // les listeners
        mBtnModif.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnView.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);

        // champs
        mChNom = findViewById(R.id.test_ch_name);
        mChChbre = findViewById(R.id.test_ch_chbre);
        mChSom = findViewById(R.id.test_ch_sum);
        mChId = findViewById(R.id.test_ch_id);

        // Le contrôleur
        mControlCred = ControlDiff.getInstance(this);

        // the status bar must be transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    @Override
    public void onClick(View v) {
        int todo = (int) v.getTag();

        switch (todo){
            case 1:
                addData();
                break;
            case 2:
                viewAll();
                break;

            case 3:
                removeData();
                break;

            case 4:
                modifyData();
                break;

        }
    }

    private void showNotif(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void viewAll() {
        StringBuilder message = new StringBuilder();

        if(mControlCred.getCount() == 0){
            showMessage("Error", "Nothing found");
        }
        else{
            for(int i = 0; i < mControlCred.getCount(); i++){
                try {
                    Map<String, String> attributes  = mControlCred.getCrediteurByIndex(i);

                    for(Map.Entry<String, String> e: attributes.entrySet()){
                        message.append(e.getKey()).append(" : ").append(e.getValue()).append("\n");
                    }

                    message.append("\n\n-----------------------\n\n");

                } catch (ElementNotExistException e) {
                    e.printStackTrace();
                }
            }

            if(!message.toString().equals(""))
                showMessage("Data", message.toString());
            else {
                showMessage("Error", "We couldn't find the Element");
            }

        }

    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void addData() {
        final String nom = mChNom.getText().toString();
        final String chbre = mChChbre.getText().toString();
        int sum;

        try {
            sum = Integer.parseInt(mChSom.getText().toString());
        }catch (Exception ignored){
            showMessage("Alerte ", "Saisie Incorecte.");
            return;
        }

        if (mControlCred.addCrediteur(nom, chbre, sum))
            showNotif("Data Inserted !");
        else
            showNotif("Data Not Inserted !");

    }

    private void removeData(){
        int id;
        try {
           id = Integer.parseInt(mChId.getText().toString());
        }catch (Exception ignored){
            showMessage("Alerte ", "Saisie Incorecte.");
            return;
        }

        try {
            if(mControlCred.getCrediteurById(id).size() > 0){
                if(mControlCred.removeCrediteur(id))
                    showNotif("Data deleted !");
                else
                    showNotif("Data Not deleted !");
            }

        } catch (ElementNotExistException e) {
            showMessage("Error ", "Not in DataBase !");
        }
    }

    private void modifyData(){
        int id;
        int sum;
        String chbre = mChChbre.getText().toString();
        String nom = mChNom.getText().toString();
        try {
            id = Integer.parseInt(mChId.getText().toString());
            sum = Integer.parseInt(mChSom.getText().toString());
        }catch (Exception ignored){
            showMessage("Alerte ", "Saisie Incorecte.");
            return;
        }

        try {
            if(mControlCred.getCrediteurById(id).size() > 0){
                if(mControlCred.modifyCrediteur(id, nom, chbre, sum))
                    showNotif("Data modified !");
                else
                    showNotif("Data Not modified !");
            }

        } catch (ElementNotExistException e) {
            showMessage("Error ", "Not in DataBase !");
        }

    }
}
