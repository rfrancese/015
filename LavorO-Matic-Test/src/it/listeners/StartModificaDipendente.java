package it.listeners;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import applicaton.lavoro_matic_test.ModificaDipendente;
import applicaton.lavoro_matic_test.VisualizzaDipendente;

public class StartModificaDipendente implements OnClickListener {
	private static VisualizzaDipendente vis;
	private static int idUtente;

	public StartModificaDipendente(VisualizzaDipendente vis,int idUtente)
	{
		this.vis=vis;
		this.idUtente=idUtente;
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(vis,ModificaDipendente.class);
		intent.putExtra("IDUTENTE", idUtente);
		vis.startActivity(intent);
		
	}

}
