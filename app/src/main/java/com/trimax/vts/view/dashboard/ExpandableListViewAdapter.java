package com.trimax.vts.view.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimax.vts.drawer.Category;
import com.trimax.vts.drawer.SubCategory;
import com.trimax.vts.view.R;

import java.util.ArrayList;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Category> categoryName;
    ArrayList<ArrayList<SubCategory>> subCategoryName;
    ArrayList<Integer> subCategoryCount;
    int count;
    Typeface type;
    SubCategory singleChild = new SubCategory();

    public ExpandableListViewAdapter(Context context, ArrayList<Category> categoryName, ArrayList<ArrayList<SubCategory>> subCategoryName, ArrayList<Integer> subCategoryCount) {
        layoutInflater = LayoutInflater.from(context);
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.subCategoryCount = subCategoryCount;
        this.count = categoryName.size();
        this.context=context;
        type = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public int getGroupCount() {
        return categoryName.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return (subCategoryCount.get(i));
    }

    @Override
    public Object getGroup(int i) {
        return categoryName.get(i).getCat_name();
    }

    @Override
    public SubCategory getChild(int i, int i1) {
        ArrayList<SubCategory> tempList = new ArrayList<SubCategory>();
        tempList = subCategoryName.get(i);
        return tempList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.expandablelistcategory, viewGroup, false);
        }
        ImageView imageView = view.findViewById(R.id.icon);
        TextView textView = view.findViewById(R.id.cat_desc_1);
        textView.setText(getGroup(i).toString());

        TextView imExpandableIndicator = view.findViewById(R.id.indicatoricon);
        imExpandableIndicator.setTypeface(type);
        if (isExpanded) {
            imExpandableIndicator.setText(context.getResources().getString(R.string.fa_arrow_up));
        } else {
            imExpandableIndicator.setText(context.getResources().getString(R.string.fa_arrow_down));
        }
        if (getChildrenCount(i) == 0) {
            imExpandableIndicator.setVisibility(View.INVISIBLE);
        } else {
            imExpandableIndicator.setVisibility(View.VISIBLE);
        }

        Category cat = categoryName.get(i);
        Log.e("Category Name"," cName"+cat.getCat_name());
        imageView.setImageResource(cat.getImageid());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.expandablelistviewsubcat, viewGroup, false);
        }
        try {
            singleChild = getChild(i, i1);
            TextView childSubCategoryName = view.findViewById(R.id.subcat_name);

            childSubCategoryName.setText(singleChild.getSubcat_name());
            ImageView imageView = view.findViewById(R.id.iconChild);
            SubCategory ss = subCategoryName.get(i).get(i1);
            imageView.setImageResource(ss.getSubcatimageid());
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
