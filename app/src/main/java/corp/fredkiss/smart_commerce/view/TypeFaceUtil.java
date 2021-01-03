package corp.fredkiss.smart_commerce.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TypeFaceUtil {

    private static String sFont = "fonts/Exo-Bold.ttf";

    public static void overrideFont(Context context, String defaultFontName, String customFontFile) {

        final Typeface customFontTypeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/Exo-Bold.ttf");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> fontMap = new HashMap<>();
            fontMap.put(defaultFontName, customFontTypeFace);

            try {
                String systemFontMap = "sSystemFontMap";
                final Field staticField = Typeface.class.getDeclaredField(systemFontMap);
                staticField.setAccessible(true);
                staticField.set(null, fontMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field defaultFontField = Typeface.class.getDeclaredField(defaultFontName);
                defaultFontField.setAccessible(true);
                defaultFontField.set(null, customFontTypeFace);
            } catch (Exception e) {
                System.err.println("can not set default custom Font : " + customFontFile +
                        "instead of " + defaultFontName);
            }
        }

    }

    public static void setFont(String font) {
        sFont = font;
    }

    public static Typeface getFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), sFont);
    }
}
