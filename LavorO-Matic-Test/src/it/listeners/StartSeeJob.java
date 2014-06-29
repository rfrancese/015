package it.listeners;

import it.interfacce.Utente;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import applicaton.lavoro_matic_test.HomePage_amm;
import applicaton.lavoro_matic_test.SeeJob;

public class StartSeeJob implements OnClickListener {
	private int idLavoro;
	private HomePage_amm home;
	private Utente mySelf;

	public StartSeeJob(HomePage_amm home,int idLavoro,Utente my)
	{
		this.home=home;
		this.idLavoro = idLavoro;
		mySelf=my;
	}
	
	public void onClick(View v) {
		Intent intent = new Intent(home,SeeJob.class);
		intent.putExtra("LavoroID", idLavoro);
		intent.putExtra("mySelf_utente", mySelf);
		home.startActivity(intent);
		
	}

}
