package com.pictureit.leumi.main;

import java.util.ArrayList;

import com.pictureit.leumi.server.parse.Emploee;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<Emploee>{

	ArrayList<Emploee> arrayList;
	private ArrayList<Emploee> itemsAll;
    private ArrayList<Emploee> suggestions;
	Context ctx;
	
	@SuppressWarnings("unchecked")
	public AutoCompleteAdapter(Context context, 
			int textViewResourceId, ArrayList<Emploee> objects) {
		super(context, textViewResourceId, objects);
		arrayList = objects;
		itemsAll = (ArrayList<Emploee>) arrayList.clone();
		suggestions = new ArrayList<Emploee>();
		ctx = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = ((Activity) ctx).getLayoutInflater().inflate(
				R.layout.simple_textview, null);
		
		TextView textView = (TextView) v.findViewById(R.id.textview1);
		textView.setText(getItem(position).SearchKey);
		return v;
	}
	
	@Override
	public Filter getFilter() {
		return nameFilter;
	}
	
	Filter nameFilter = new Filter() {
		public String convertResultToString(Object resultValue) {
            String str = ((Emploee)(resultValue)).SearchKey; 
            return str;
        }
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			@SuppressWarnings("unchecked")
			ArrayList<Emploee> filteredList = (ArrayList<Emploee>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Emploee c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }			
		}
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if(constraint != null) {
                arrayList.clear();
                for (Emploee emploee : itemsAll) {
                    if(emploee.KeyWords.toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(emploee);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
		}
	};
	
	@Override
	public Emploee getItem(int position) {
		return arrayList.get(position);
	}
	
	@Override
	public int getCount() {
		return arrayList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

}
