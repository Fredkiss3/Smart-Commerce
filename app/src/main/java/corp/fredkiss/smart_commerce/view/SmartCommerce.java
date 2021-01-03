package corp.fredkiss.smart_commerce.view;

import android.app.Application;

public class SmartCommerce extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypeFaceUtil.overrideFont(getApplicationContext(), "sans",
                "fonts/Exo-Bold.ttf");
    }
}
