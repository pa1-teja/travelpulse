package com.trimax.vts.helper;

import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.view.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

public class GooglePlaceAreaTextChangeListener implements TextWatcher {
	
	public Context ctx;
	public AutoCompleteTextView aedtArea;
	CommonClass commonClass;
	
	public GooglePlaceAreaTextChangeListener(Context ctx, AutoCompleteTextView aedtArea) {
		this.ctx = ctx;
		this.aedtArea = aedtArea;
		commonClass = new CommonClass(ctx);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(commonClass.isConnected(ctx)) {
        	GetGooglePlacesHelper getGooglePlaces = new GetGooglePlacesHelper();
        	getGooglePlaces.ctx = ctx;
        	getGooglePlaces.aedtArea = aedtArea;
        	getGooglePlaces.execute(s.toString());
        }
        else {
        	commonClass.DisplayToast(ctx, ctx.getString(R.string.network_error_message), "bottom");
        }
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}
