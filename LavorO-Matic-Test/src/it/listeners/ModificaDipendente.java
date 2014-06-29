package it.listeners;

import android.view.View;
import android.view.View.OnClickListener;

public class ModificaDipendente implements OnClickListener{
	private static applicaton.lavoro_matic_test.ModificaDipendente mod;
	
	public ModificaDipendente(applicaton.lavoro_matic_test.ModificaDipendente mod)
	{
		this.mod=mod;
	}
	
	public void onClick(View v) {
		mod.effettuaModifica();
		
	}

}
