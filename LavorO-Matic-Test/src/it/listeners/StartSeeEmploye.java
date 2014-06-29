package it.listeners;

import it.interfacce.Utente;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import applicaton.lavoro_matic_test.HomePage_amm;
import applicaton.lavoro_matic_test.VisualizzaDipendente;

public class StartSeeEmploye implements OnClickListener{
	
	private int idDipendente;
	private static HomePage_amm home;
	private static Utente mySelf;
	public StartSeeEmploye(HomePage_amm h,int id,Utente mySelf)
	{
		idDipendente=id;
		home = h;
		this.mySelf=mySelf;
	}
	
	public void onClick(View v) {
		Intent intent = new Intent(home,VisualizzaDipendente.class);
		intent.putExtra("IDDIPENDENTE", idDipendente);
		intent.putExtra("mySelf_utente", mySelf);
		home.startActivity(intent);
		
	}

}
