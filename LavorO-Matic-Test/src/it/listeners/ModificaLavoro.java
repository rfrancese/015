package it.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import applicaton.lavoro_matic_test.ModifyJob;
import applicaton.lavoro_matic_test.R;

public class ModificaLavoro implements OnClickListener{
	private static ModifyJob modify;
	private static int idLavoro;

	public ModificaLavoro(ModifyJob mod,int idLavoro)
	{
		this.idLavoro=idLavoro;
		modify=mod;
	}

	public void onClick(View v) {
		EditText nome,descrizione,indirizzo;
		
		nome = (EditText)modify.findViewById(R.id.editText_nome);
		descrizione = (EditText)modify.findViewById(R.id.editText_descrizione);
		indirizzo=(EditText)modify.findViewById(R.id.editText_indirizzo);
		
		String name = nome.getText().toString();
		String description = descrizione.getText().toString();
		String address = indirizzo.getText().toString();
		
		new it.connessioni.ModificaLavoro(modify).execute("http://lavoromatic.altervista.org/modificaLavoro.php",""+idLavoro,name,description,address);
		modify.findViewById(R.id.button1).setEnabled(false);
	}

}
