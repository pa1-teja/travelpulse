package com.trimax.vts.view.maps.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.trimax.vts.view.R;
import com.trimax.vts.view.model.StringClusterItem;

public class MyCustomAdapterForItems  extends DefaultClusterRenderer<StringClusterItem> implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private final IconGenerator mClusterIconGenerator;
    private StringClusterItem clickedClusterItem;
    private final Context mContext;


    public MyCustomAdapterForItems(Context context, GoogleMap map, ClusterManager<StringClusterItem> clusterManager,StringClusterItem clickedClusterItem) {
        super(context, map, clusterManager);
        this.clickedClusterItem=clickedClusterItem;
        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
        myContentsView = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView tvTitle = myContentsView.findViewById(R.id.txtTitle);
        TextView tvSnippet = myContentsView.findViewById(R.id.txtSnippet);
        TextView tvvno = myContentsView.findViewById(R.id.txtvno);
        TextView tvspeed = myContentsView.findViewById(R.id.txtspeed);
        TextView tvdate = myContentsView.findViewById(R.id.txtdate);
        TextView tvign = myContentsView.findViewById(R.id.txtign);
        TextView txtAc = myContentsView.findViewById(R.id.txtAc);
        TextView tvign_n = myContentsView.findViewById(R.id.txtign_n);
        TextView txtAc_n = myContentsView.findViewById(R.id.txtAc_n);
        TextView tvdriver = myContentsView.findViewById(R.id.txtdriver);
        TextView tvowner = myContentsView.findViewById(R.id.txtowner);
        TextView txtgroup = myContentsView.findViewById(R.id.txtgroup);

        if (clickedClusterItem!=null) {
            tvTitle.setText(clickedClusterItem.getTitle());
            tvSnippet.setText(clickedClusterItem.getSnippet());
            tvvno.setText("Vehicle No: " + clickedClusterItem.getVehicle_no());
            tvdate.setText("Updated On:" + clickedClusterItem.getDate());
            tvdriver.setText("Driver: " + clickedClusterItem.getDriver());
            tvowner.setText("Owner: " + clickedClusterItem.getOwner());

            if (clickedClusterItem.getGroup() != null) {
                txtgroup.setText("Group: " + clickedClusterItem.getGroup());
            } else {
                txtgroup.setVisibility(View.GONE);
            }

            if (clickedClusterItem.getNnw().equalsIgnoreCase("In Network")) {
                txtAc.setVisibility(View.VISIBLE);
                tvign.setVisibility(View.VISIBLE);
                txtAc_n.setVisibility(View.VISIBLE);
                tvign_n.setVisibility(View.VISIBLE);
                tvspeed.setVisibility(View.VISIBLE);
                tvspeed.setText("Speed: " + clickedClusterItem.getSpeed());

                if (clickedClusterItem.getIgn().equalsIgnoreCase("ON")) {
                    txtAc.setVisibility(View.VISIBLE);
                    tvign.setVisibility(View.VISIBLE);
                    txtAc_n.setVisibility(View.VISIBLE);
                    tvign_n.setVisibility(View.VISIBLE);
                    tvign.setText("ON");
                    tvign.setBackgroundResource(R.drawable.dashbaordstatusgreen);


                } else {
                    txtAc.setVisibility(View.VISIBLE);
                    tvign.setVisibility(View.VISIBLE);
                    txtAc_n.setVisibility(View.VISIBLE);
                    tvign_n.setVisibility(View.VISIBLE);
                    tvign.setText("OFF");
                    tvign.setBackgroundResource(R.drawable.dashbaordstatus);
                }

                if (clickedClusterItem.getGps().equalsIgnoreCase("0"))
                    tvspeed.append("\nNO GPS SIGNAL");

                if (clickedClusterItem.getAc().equalsIgnoreCase("ON")) {
                    txtAc.setVisibility(View.VISIBLE);
                    tvign.setVisibility(View.VISIBLE);
                    txtAc_n.setVisibility(View.VISIBLE);
                    tvign_n.setVisibility(View.VISIBLE);
                    txtAc.setText("ON");
                    txtAc.setBackgroundResource(R.drawable.dashbaordstatusgreen);


                } else if (clickedClusterItem.getAc().equalsIgnoreCase("NA")) {
                    txtAc.setVisibility(View.GONE);
                    tvign.setVisibility(View.VISIBLE);
                    txtAc_n.setVisibility(View.GONE);
                    tvign_n.setVisibility(View.VISIBLE);


                } else {
                    txtAc.setVisibility(View.VISIBLE);
                    tvign.setVisibility(View.VISIBLE);
                    txtAc_n.setVisibility(View.VISIBLE);
                    tvign_n.setVisibility(View.VISIBLE);
                    txtAc.setText("OFF");
                    txtAc.setBackgroundResource(R.drawable.dashbaordstatus);
                }
            } else {
                txtAc.setVisibility(View.GONE);
                tvign.setVisibility(View.GONE);
                txtAc_n.setVisibility(View.GONE);
                tvign_n.setVisibility(View.GONE);
                tvspeed.setText("NO NETWORK | NO GPS SIGNAL");
                //tvspeed.setVisibility(View.GONE);
            }
        }
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    @Override
    protected void onBeforeClusterItemRendered(StringClusterItem item, MarkerOptions markerOptions) {


        if(item.getNnw().equalsIgnoreCase("No Network")) {

            if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_black));
            } else if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon));
            } else {
                mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon));
            }

            final Bitmap icon = mClusterIconGenerator.makeIcon("");
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        } else {

            if (item.getIgn().equalsIgnoreCase("ON") && item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("In Network")) {

                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_orage));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_orange));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_orage));
                }
                final Bitmap icono = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icono));

            } else if (item.getIgn().equalsIgnoreCase("OFF") && item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("In Network")) {

                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_red));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                }
                final Bitmap iconred = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconred));

            } else if (item.getIgn().equalsIgnoreCase("OFF") && item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("No Network")) {
                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_red));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                }
                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            } else if (item.getIgn().equalsIgnoreCase("ON") && item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("No Network")) {
                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_black));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon));
                }

                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            } else if (item.getIgn().equalsIgnoreCase("ON") && !item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("No Network")) {
                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_orage));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_orange));
                }else  {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_orage));
                }

                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            } else if (item.getIgn().equalsIgnoreCase("OFF") && !item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("IN Network")) {

                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_red));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                }
                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            } else if (item.getIgn().equalsIgnoreCase("OFF") && item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("IN Network")) {

                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_red));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                }

                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            } else if (item.getIgn().equalsIgnoreCase("ON") && !item.getSpeed().equalsIgnoreCase("0") && item.getNnw().equalsIgnoreCase("In Network")) {
                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_green));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_green));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_green));
                }

                final Bitmap icong = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icong));


            } else {
                if (item.getVehicle_type_id().equalsIgnoreCase("1")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                } else if (item.getVehicle_type_id().equalsIgnoreCase("8")) {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bike_topview_red));
                }else {
                    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_car_icon_red));
                }
                final Bitmap icon = mClusterIconGenerator.makeIcon("");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            }
        }

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<StringClusterItem> cluster, MarkerOptions markerOptions) {
        if (cluster.getSize() < 10) {
            mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.skybluesmall));
        } else if (cluster.getSize() < 100) {
            mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.purplesmall));

        } else if (cluster.getSize() < 1000) {
            mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pinksmall));
        }
        mClusterIconGenerator.setTextAppearance(R.style.header_text);
        final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
}
