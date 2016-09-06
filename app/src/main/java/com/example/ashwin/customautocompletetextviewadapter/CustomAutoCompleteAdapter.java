package com.example.ashwin.customautocompletetextviewadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ashwin on 6/9/16.
 */

public class CustomAutoCompleteAdapter extends BaseAdapter implements Filterable {

    Context context;

    private final Object mLock = new Object();
    private ArrayList<String> fullList;
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;
    private OnItemSelectClickListener mSelectListener;
    private OnItemDeleteClickListener mDeleteListener;

    TextView itemName, itemDel;

    private LayoutInflater mInflater;

    public interface OnItemSelectClickListener {
        public void onItemSelectClicked();
    }

    public interface OnItemDeleteClickListener {
        public void onItemDeleteClicked();
    }

    /**
     * Set listener for clicks on the footer item
     */
    /*public void setOnItemSelectClickListener(OnItemSelectClickListener listener) {
        mSelectListener = listener;
    }*/

    /*public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        mDeleteListener = listener;
    }*/

    public CustomAutoCompleteAdapter(Context context, String[] objects) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fullList = new ArrayList<String>( Arrays.asList(objects) );
        this.context = context;
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public String getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView =  inflater.inflate(R.layout.custom_adapter_row, parent, false);

        itemName = (TextView) rowView.findViewById(R.id.itemName);
        itemDel = (TextView) rowView.findViewById(R.id.itemDel);

        itemName.setText(getItem(position));

        //clear button click listener
        itemDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullList.remove(getItem(position));
                notifyDataSetChanged();
                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    //filter items which does not contain typed text
    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {

            FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<String>(fullList);
				}
			}

			if (prefix == null || prefix.length() == 0) {
                final ArrayList<String> list;
                synchronized (mLock) {
					list = new ArrayList<String>(mOriginalValues);
				}
                results.values = list;
                results.count = list.size();
			} else {
				final String prefixString = prefix.toString().toLowerCase();

				final ArrayList<String> values;

                synchronized (mLock) {
                    values = new ArrayList<String>(mOriginalValues);
                }
                results.values = values;
                results.count = values.size();

				final int count = values.size();

				final ArrayList<String> newValues = new ArrayList<String>();

				for (int i = 0; i < count; i++) {
					String item = values.get(i);
					if (item.toLowerCase().contains(prefixString)) {
						newValues.add(item);
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            fullList = (ArrayList<String>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }


    }

}
