package com.trimax.vts.view.reports.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.model.NotificationPojo;

import java.util.ArrayList;
import java.util.List;

public class AlertTypeAdapter extends ArrayAdapter<NotificationPojo> {
        private Context context;
        private int resourceId;
        private List<NotificationPojo> items, tempItems, suggestions;

        public AlertTypeAdapter(@NonNull Context context, int resourceId, ArrayList<NotificationPojo> items) {
            super(context, resourceId, items);
            this.items = items;
            this.context = context;
            this.resourceId = resourceId;
            tempItems = new ArrayList<>(items);
            suggestions = new ArrayList<>();
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            try {
                if (convertView == null) {
                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    view = inflater.inflate(resourceId, parent, false);
                }
                NotificationPojo fruit = getItem(position);
                TextView name = (TextView) view.findViewById(R.id.textView);

               name.setText(fruit.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }
        @Nullable
        @Override
        public NotificationPojo getItem(int position) {
            return items.get(position);
        }
        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @NonNull
        @Override
        public Filter getFilter() {
            return fruitFilter;
        }
        private Filter fruitFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                NotificationPojo fruit = (NotificationPojo) resultValue;
                return fruit.getTitle();
            }
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence != null) {
                    suggestions.clear();
                    for (NotificationPojo fruit: tempItems) {
                        if (fruit.getTitle().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            suggestions.add(fruit);
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
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList<NotificationPojo> tempValues = (ArrayList<NotificationPojo>) filterResults.values;
                if (filterResults != null && filterResults.count > 0) {
                    clear();
                    for (NotificationPojo fruitObj : tempValues) {
                        add(fruitObj);
                        notifyDataSetChanged();
                    }
                } else {
                    clear();
                    notifyDataSetChanged();
                }
            }
        };
    }

