package utilities.view;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboard {

	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    if(inputMethodManager != null && activity.getCurrentFocus() != null)
	    	inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
}
