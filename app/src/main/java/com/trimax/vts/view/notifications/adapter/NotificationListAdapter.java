package com.trimax.vts.view.notifications.adapter;

import java.util.ArrayList;

import com.trimax.vts.view.R;
import com.trimax.vts.model.notifications.Datum;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter {

	ArrayList<Datum> result;
    Context context;
    private LayoutInflater inflater = null;
    
    public  NotificationListAdapter(Context ctx, ArrayList<Datum> userAlertListResponse) {
        result = userAlertListResponse;
        context = ctx;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	@Override
	public int getCount() {
		
		return result!=null ? result.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        View rowView = inflater.inflate(R.layout.notification_list_item, parent, false);
       	Typeface font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
	    ImageView NotificatnIcon=(ImageView)rowView.findViewById(R.id.txtNotificatnIcon);
		NotificatnIcon.setImageResource(R.drawable.menu_alerts);
	    
	    TextView BellIcon=(TextView)rowView.findViewById(R.id.txtNotificatnBell);
	    TextView NotificatnArrow=(TextView)rowView.findViewById(R.id.txtNotificatnArrow);
	    BellIcon.setVisibility(rowView.INVISIBLE);

		BellIcon.setTypeface(font_awesome);
	    NotificatnArrow.setTypeface(font_awesome);
	    
	      
        TextView NotificationBody = (TextView) rowView.findViewById(R.id.txtNotificatnBody);
        if(result.get(position).getMsg() != null){
        	NotificationBody.setText(result.get(position).getMsg().replace("\\n","\n"));
        }else{
        	NotificationBody.setText("");
        }
       
        TextView NotificationTime = (TextView) rowView.findViewById(R.id.txtNotificatnTime);
        if(result.get(position).getDateTime()!=null)
        {
        	NotificationTime.setText(result.get(position).getDateTime());
        }else
        {
        	NotificationTime.setText("");
        }
		if(result.get(position).getDaysago()!=null)
		{
			NotificatnArrow.setText(result.get(position).getDaysago());
		}else
		{
			NotificatnArrow.setText("");
		}
		return rowView;
	}

	public void add(ArrayList<Datum> list) {
		if(result!=null && list!=null) {
			result.addAll(list);
		}
		
	}

	public void remove(String msg) {
		int a=Integer.parseInt(msg);
		result.remove(a);	
	}
	
}
