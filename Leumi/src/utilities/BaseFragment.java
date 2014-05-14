package utilities;

import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.R;

import android.app.Dialog;
import android.app.Fragment;
import android.view.View;

//Written by @Noam Bar-Oz

public class BaseFragment extends Fragment {

	protected void showErrorDialog() {
		Dialogs.generalDialog(getActivity(), getResources().getString(R.string.somthing_went_wrong));
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findView(id);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}
}
