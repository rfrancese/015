package applicaton.lavoro_matic_test;

import it.connessioni.CaricaLavoroDipendente;
import it.interfacce.Impiegato;
import it.interfacce.Lavoro;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SeeJob_dip extends ActionBarActivity {

	private static boolean started=false;
	private static AsyncTask<String, String, String> task;
	private static int idLavoro;
	private static Lavoro lavoro;
	private static Impiegato mySelf;

	
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent(this,HomePage_dip.class);
		intent.putExtra("mySelf_impiegato", mySelf);
		startActivity(intent);
	}
	
	
	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null &&  task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_job_dip);
		if(!started)
		{
			Intent intent = getIntent();
			idLavoro = intent.getIntExtra("LavoroID", -1);
			mySelf =(Impiegato) intent.getSerializableExtra("mySelf_impiegato");
			started=true;
		}
		else
		{
			int temp = getIntent().getIntExtra("LavoroID", -1);
			Impiegato temp2=(Impiegato) getIntent().getSerializableExtra("mySelf_impiegato");
			if(temp!=-1 && temp!=idLavoro){
				idLavoro=temp;
			}
			if(temp2!=null)
			{
				int id = temp2.getId();
				if(id!=mySelf.getId())
					mySelf=temp2;
			}
		}
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
			task = new CaricaLavoroDipendente(this).execute("http://lavoromatic.altervista.org/getLavoro.php",""+idLavoro);
		
		
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.see_job_dip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_see_job_dip,
					container, false);
			return rootView;
		}
	}


	public void caricaLavoro(String result)
	{
		
		try {
			JSONObject temp = new JSONObject(result);
			TextView nome,descrizione,indirizzo;
			
			lavoro = new Lavoro(temp.getInt("idLavoro"), temp.getInt("percentuale"), temp.getString("Nome"), temp.getString("Descrizione"), temp.getString("Indirizzo"));
			ProgressBar caricamento;
			nome = (TextView)findViewById(R.id.see_nome);
			descrizione = (TextView)findViewById(R.id.see_descrizione);
			indirizzo = (TextView)findViewById(R.id.see_indirizzo);
			



			caricamento = (ProgressBar)findViewById(R.id.see_caricamento);

			nome.setText(" "+temp.getString("Nome"));
			descrizione.setText(" "+temp.getString("Descrizione"));
			indirizzo.setText(" "+temp.getString("Indirizzo"));

			caricamento.setVisibility(View.INVISIBLE);
			LinearLayout principale = (LinearLayout)findViewById(R.id.LinearPrincipale);
			principale.setVisibility(View.VISIBLE);


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Lavoro non trovato, qualcuno deve averlo cancellato o non c'è connessione", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this,HomePage_dip.class);
			//			intent.putExtra("mySelf_impiegato", getIntent().getExtras().getSerializable("mySelf_impiegato"));
			startActivity(intent);
		}
	}


	public void doCommenti(View view)
	{
		Intent intent = new Intent(this,GestioneCommentiDipendente.class);
		intent.putExtra("LavoroID", idLavoro);
		intent.putExtra("NOMELAVORO", lavoro.getNome());
		intent.putExtra("mySelf_impiegato", mySelf);
		startActivity(intent);
	}

	public void doMappa(View view)
	{
		String uri = String.format(Locale.ITALIAN, "http://maps.google.it/maps?q="+lavoro.getIndirizzo());
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
		startActivity(intent);
	}

}
