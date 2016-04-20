package Constant;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by USER on 20/4/2016.
 */
public class HideKeyBoard {

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.getLastInputMethodSubtype();
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
