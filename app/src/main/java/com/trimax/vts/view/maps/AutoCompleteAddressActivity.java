package com.trimax.vts.view.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.AutoSuggestSearch;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.adapters.AutoSuggestAdapter;
import com.trimax.vts.view.model.AutoSuggestModel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class AutoCompleteAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AutoCompleteAddressActi";
    private RecyclerView rv_address;
    private AutoSuggestAdapter adapter;
    private List<AutoSuggestModel> suggestModels;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Search place");
        infoPref = new TravelpulseInfoPref(this);

        suggestModels = new ArrayList<>();
        rv_address = findViewById(R.id.rv_address);
        rv_address.setLayoutManager(new LinearLayoutManager(this));
        rv_address.setHasFixedSize(true);
        rv_address.setItemAnimator(new DefaultItemAnimator());
        adapter = new AutoSuggestAdapter(suggestModels,this);
        rv_address.setAdapter(adapter);

        MapEngine mapEngine = MapEngine.getInstance();
        ApplicationContext appContext = new ApplicationContext(this);
        mapEngine.init(appContext, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // Post initialization code goes here
                    Log.d(TAG, "onEngineInitializationCompleted: ");
                } else {
                    // handle factory initialization failure
                }
            }
        });
        final TextInputEditText tie_search = findViewById(R.id.tie_search);
        rv_address = findViewById(R.id.rv_address);
        tie_search.requestFocus();

        tie_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()==0){
                    suggestModels.clear();
                    adapter.addAddresses(suggestModels);
                }
            }
        });

        tie_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tie_search.getRight() - tie_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        loadPlaces(tie_search.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void loadPlaces(String search){
        TextAutoSuggestionRequest request = new TextAutoSuggestionRequest(search);
        request.setSearchCenter(new GeoCoordinate(19.078054, 72.898879));
        request.setCollectionSize(20);
        request.setFilters(EnumSet.of(TextAutoSuggestionRequest.AutoSuggestFilterType.PLACE));
        request.execute(new AutoSuggestionQueryListener());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_select){
            AutoSuggestModel model = (AutoSuggestModel) view.getTag();
            infoPref.putString(infoPref.GEOFENCE_PLACE,model.getTitle(), PrefEnum.Login);
            infoPref.putString(infoPref.GEOFENCE_ADDRESS,model.getAddress().replace("<br/>"," "), PrefEnum.Login);
            infoPref.putString(infoPref.GEOFENCE_LAT,String.valueOf(model.getLat()), PrefEnum.Login);
            infoPref.putString(infoPref.GEOFENCE_LNG,String.valueOf(model.getLng()), PrefEnum.Login);
            finish();
        }
    }

    class AutoSuggestionQueryListener implements ResultListener<List<AutoSuggest>> {

        @Override
        public void onCompleted(List<AutoSuggest> data, ErrorCode error) {
            Log.d(TAG, "onCompleted: "+data.size());
            if (error == ErrorCode.NONE){
                suggestModels.clear();
                for(AutoSuggest autoSuggest : data){
                    switch (autoSuggest.getType()){
                        case PLACE:
                            AutoSuggestPlace autoSuggestPlace = (AutoSuggestPlace) autoSuggest;
                            suggestModels.add(new AutoSuggestModel(autoSuggestPlace.getTitle(),autoSuggestPlace.getVicinity(),autoSuggestPlace.getPosition().getLatitude(),autoSuggestPlace.getPosition().getLongitude()));
                            Log.d(TAG, "onCompleted: "+autoSuggestPlace.getTitle());
                            Log.d(TAG, "onCompleted: "+autoSuggestPlace.getVicinity());
                            Log.d(TAG, "onCompleted: "+autoSuggestPlace.getPosition().getLongitude()+"  "+autoSuggestPlace.getPosition().getLatitude());
                            break;
                        case SEARCH:
                            AutoSuggestSearch autoSuggestSearch = (AutoSuggestSearch)autoSuggest;
                            suggestModels.add(new AutoSuggestModel(autoSuggestSearch.getTitle(),""/*autoSuggestSearch.getHighlightedTitle()*/,autoSuggestSearch.getPosition().getLatitude(),autoSuggestSearch.getPosition().getLongitude()));
                            Log.d(TAG, "onCompleted: "+autoSuggestSearch.getTitle());
                            Log.d(TAG, "onCompleted: "+autoSuggestSearch.getHighlightedTitle());
                            Log.d(TAG, "onCompleted: "+autoSuggestSearch.getPosition().getLongitude()+"  "+autoSuggestSearch.getPosition().getLatitude());
                            break;
                        case QUERY:
                            break;
                    }
                }
                adapter.addAddresses(suggestModels);
            }else {
                Log.d(TAG, "onCompleted: "+error);
            }
            /*for (AutoSuggest r : data) {

            }*/
        }
    }
}
