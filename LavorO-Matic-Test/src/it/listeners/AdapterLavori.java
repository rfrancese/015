package it.listeners;

import it.interfacce.Lavoro;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import applicaton.lavoro_matic_test.R;

public class AdapterLavori extends ArrayAdapter<Lavoro> {

	public AdapterLavori(Context context, int resource,
			List<Lavoro> objects) {
		super(context, resource, objects);

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
		        convertView = inflater.inflate(R.layout.rowlavori, null);
            viewHolder = new ViewHolder();
            viewHolder.nomeLavoro = (TextView)convertView.findViewById(R.id.textViewNomeLavoro);
            
            
            convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }
		Lavoro l = getItem(position);
		viewHolder.nomeLavoro.setText(l.getNome());

        
		return convertView;
	}
	
	 private class ViewHolder {
	        public TextView nomeLavoro;
	        
	       
	    }

}
