package it.listeners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AggiungiDipendente implements OnClickListener {
	
	private applicaton.lavoro_matic_test.AggiungiDipendente root;
	
	public AggiungiDipendente(applicaton.lavoro_matic_test.AggiungiDipendente r) {
		
		root=r;
	}

	public void onClick(View v) {
		new AlertDialog.Builder(root)
		.setTitle("Aggiungi Dipendente")
		.setMessage("Sei sicuro di voler aggiungere un nuovo Dipendete?")
		.setPositiveButton("Sì", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				root.aggiungi();

			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(root.getApplicationContext(), "Annullato", Toast.LENGTH_SHORT).show();
				

			}
		})
		.show();
		
	}

}
