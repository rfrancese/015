package it.listeners;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import applicaton.lavoro_matic_test.ModifyJob;
import applicaton.lavoro_matic_test.SeeJob;

public class StartModifyJob implements OnClickListener {

	private static int idLavoro;
	private static SeeJob see;

	public StartModifyJob(int idLavoro,SeeJob see)
	{
		this.idLavoro=idLavoro;
		this.see=see;
	}

	public void onClick(View arg0) {
		Intent intent = new Intent(see,ModifyJob.class);
		intent.putExtra("IDLAVORO", idLavoro);
		see.startActivity(intent);

	}

}
