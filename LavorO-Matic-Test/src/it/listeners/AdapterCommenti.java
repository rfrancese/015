package it.listeners;

import it.interfacce.Commento;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import applicaton.lavoro_matic_test.R;

public class AdapterCommenti extends ArrayAdapter<Commento> {

	public AdapterCommenti(Context context, int resource,List<Commento> list) {
		super(context, resource,list);
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getViewOptimize(position, convertView, parent);
		
	}
	
	public View getViewOptimize(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
		             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.rowcommenti, null);
            viewHolder = new ViewHolder();
            viewHolder.autore = (TextView)convertView.findViewById(R.id.textViewAutore);
            viewHolder.commento = (TextView)convertView.findViewById(R.id.textViewTesto);
            
            convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }
		Commento c = getItem(position);
		viewHolder.autore.setText(c.getAutore());
        viewHolder.commento.setText(c.getTesto());
        
		return convertView;
	}
	
	 private class ViewHolder {
	        public TextView autore;
	        public TextView commento;
	       
	    }
	 
	

}
