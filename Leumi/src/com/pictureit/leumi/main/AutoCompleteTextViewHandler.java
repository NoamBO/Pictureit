package com.pictureit.leumi.main;

import java.util.ArrayList;

import utilities.HttpBase.HttpCalback;
import utilities.SoftKeyboard;
import android.app.Activity;
import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.pictureit.leumi.server.GetAutoComplete;
import com.pictureit.leumi.server.parse.Emploee;
import com.pictureit.leumi.server.parse.JsonToObject;

public class AutoCompleteTextViewHandler {

	Context ctx;

	HttpCalback callback;

	AutoCompleteTextView editText;

	public AutoCompleteTextViewHandler() {
	}
	
	public void setListener(Context _ctx, AutoCompleteTextView _editText,
			final String serverRequestType) {
		setListener(_ctx, _editText, serverRequestType, null);
	}

	public void setListener(Context _ctx, AutoCompleteTextView _editText,
			final String serverRequestType, HttpCalback httpCalback) {

		this.ctx = _ctx;
		this.editText = _editText;

		if(callback == null)
			setCallback();

		editText.addTextChangedListener(new BaseEditTextListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (editText.isPerformingCompletion()) {
					completionPerforce();
					return;
				}

				String searchString = editText.getText().toString();
				if (searchString.length() < 1) {
					return;
				}

				GetAutoComplete autoComplete = new GetAutoComplete(ctx,
						serverRequestType, callback);
				autoComplete.execute(searchString);
			}

		});
	}

	private void setCallback() {
		callback = new HttpCalback() {

			@Override
			public void onAnswerReturn(Object answer) {
				ArrayList<Emploee> emploees = JsonToObject
						.jsonToAutoComplete((String) answer);
				AutoCompleteAdapter adapter = new AutoCompleteAdapter(ctx,
						android.R.layout.simple_list_item_2, emploees);

				editText.setAdapter(adapter);
				editText.showDropDown();
				
			}
		};
		
	}

	private void completionPerforce() {
		SoftKeyboard.hideSoftKeyboard((Activity) ctx);
	}
}
