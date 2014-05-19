package it.listeners;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import applicaton.lavoro_matic_test.HomePage_amm;
import applicaton.lavoro_matic_test.SeeJob;

public class StartSeeJob implements OnClickListener {
	private int idLavoro;
	private HomePage_amm home;

	public StartSeeJob(HomePage_amm home,int idLavoro)
	{
		this.home=home;
		this.idLavoro = idLavoro;
	}
	
	public void onClick(View v) {
		Intent intent = new Intent(home,SeeJob.class);
		intent.putExtra("LavoroID", idLavoro);
		home.startActivity(intent);
		
	}

}
