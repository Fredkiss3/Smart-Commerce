package corp.fredkiss.smart_commerce.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import corp.fredkiss.smart_commerce.R;

public class MainActivity_ extends AppCompatActivity {

    private ImageView mMainMenuBtn;
    private CardView mUserCard;
    private LinearLayout mJournalOption;
    private LinearLayout mStockOption;
    private LinearLayout mApproOption;
    private LinearLayout mStatsOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        init();
    }

    private void init() {
        // FULLSCREEN MODE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // les éléments de la vue
        mMainMenuBtn = findViewById(R.id.main_hamburger_menu_btn);
        mUserCard = findViewById(R.id.main_user_card);
        mJournalOption = findViewById(R.id.main_journal_option);
        mStockOption = findViewById(R.id.main_stock_option);
        mApproOption = findViewById(R.id.main_appro_option);
        mStatsOption = findViewById(R.id.main_stats_option);

        //

    }
}
