package it.listeners;

import it.interfacce.Impiegato;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import applicaton.lavoro_matic_test.R;

public class AdapterDipendenti extends ArrayAdapter<Impiegato> {

	public AdapterDipendenti(Context context, int resource,List<Impiegato> imps) {
		super(context, resource,imps);
 
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
		        convertView = inflater.inflate(R.layout.rowdipendenti, null);
            viewHolder = new ViewHolder();
            viewHolder.ruolo = (TextView)convertView.findViewById(R.id.textViewRuoloDipendenti);
            viewHolder.dipendente = (TextView)convertView.findViewById(R.id.textViewNomeDipendente);
            
            convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }
		Impiegato i = getItem(position);
		viewHolder.ruolo.setText(i.getRuolo());
        viewHolder.dipendente.setText(i.getCognome()+" "+i.getNome());
        
		return convertView;
	}
	
	 private class ViewHolder {
	        public TextView ruolo;
	        public TextView dipendente;
	       
	    }

}
