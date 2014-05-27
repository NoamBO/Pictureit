package com.pictureit.leumi.main;

import java.util.ArrayList;

import utilities.server.HttpBase.HttpCallback;
import utilities.view.SoftKeyboard;
import android.app.Activity;
import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.pictureit.leumi.server.GetAutoComplete;
import com.pictureit.leumi.server.parse.Emploee;
import com.pictureit.leumi.server.parse.JsonToObject;

public class AutoCompleteTextViewHandler {

	Context ctx;

	HttpCallback callback;

	AutoCompleteTextView editText;
	
	GetAutoComplete autoComplete;
	
	public boolean isOkToShowDropDown;

	public AutoCompleteTextViewHandler() {
	}
	
	public void setListener(Context _ctx, AutoCompleteTextView _editText,
			final String serverRequestType) {
		setListener(_ctx, _editText, serverRequestType, null);
	}

	public void setListener(Context _ctx, AutoCompleteTextView _editText,
			final String serverRequestType, HttpCallback httpCalback) {

		this.ctx = _ctx;
		this.editText = _editText;

		if(callback == null)
			setCallback();

		editText.addTextChangedListener(new BaseEditTextListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if(s.toString().length() < 1)
					editText.setAdapter(null);
				
				if (editText.isPerformingCompletion()) {
					completionPerforce();
					return;
				}

				String searchString = editText.getText().toString();
				if (searchString.length() < 1) {
					return;
				}
				
				if(autoComplete != null && !autoComplete.isCancelled())
					autoComplete.cancel(true);

				isOkToShowDropDown = true;
				autoComplete = new GetAutoComplete(ctx,
						serverRequestType, callback);
				autoComplete.execute(searchString);
			}

		});
	}
	
	public void cancelTask() {
		if(autoComplete != null && !autoComplete.isCancelled()){
			autoComplete.cancel(true);
		}
		isOkToShowDropDown = false;
		editText.setText("");
		completionPerforce();
	}

	private void setCallback() {
		callback = new HttpCallback() {

			@Override
			public void onAnswerReturn(String answer) {
				if (isOkToShowDropDown) {
					ArrayList<Emploee> emploees = JsonToObject
							.jsonToAutoComplete(answer);
					AutoCompleteAdapter adapter = new AutoCompleteAdapter(ctx,
							android.R.layout.simple_list_item_2, emploees);

					editText.setAdapter(adapter);
					editText.showDropDown();
				}
			}
		};
		
	}

	private void completionPerforce() {
		SoftKeyboard.hideSoftKeyboard((Activity) ctx);
	}
}
