package utilities;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.Settings;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
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

	protected DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.cellprofileimg)
	.showImageOnFail(R.drawable.cellprofileimg)
	.showImageOnLoading(R.drawable.cellprofileimg)
    .resetViewBeforeLoading(false)  // default
    .cacheInMemory(true) // default
    .cacheOnDisc(false) // default
    .considerExifParams(false) // default
    .displayer(new SimpleBitmapDisplayer()) // default
    .build();
	protected com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
}
