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

public class AdapterDipendentiAssegnati extends ArrayAdapter<Impiegato>{

	public AdapterDipendentiAssegnati(Context context, int resource,List<Impiegato> imps) {
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
		        convertView = inflater.inflate(R.layout.customrow, null);
            viewHolder = new ViewHolder();
            viewHolder.nome = (TextView)convertView.findViewById(R.id.textViewNome);
            viewHolder.ruolo = (TextView)convertView.findViewById(R.id.textViewRuolo);
            convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }
		Impiegato i = getItem(position);
        viewHolder.ruolo.setText(i.getRuolo());
        viewHolder.ruolo.setId(i.getId());
        viewHolder.nome.setText(i.getCognome()+" "+i.getNome());
		
		return convertView;
	}
	
	 private class ViewHolder {
	        public TextView nome;
	        public TextView ruolo;
	    }
	

}
