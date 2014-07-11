package applicaton.lavoro_matic_test;

import it.connessioni.CaricaDipendente;
import it.interfacce.Impiegato;
import it.listeners.AdapterDipendentiAssegnati;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizzaDipendentiAssegnati extends ActionBarActivity {

	private static boolean started=false;
	private static AsyncTask<String,String,String> task;
	private static int idLavoro,idAzienda;
	private static String nomeLavoro;

	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!= AsyncTask.Status.FINISHED)
			task.cancel(true);
	}


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizza_dipendenti_assegnati);
		if(!started)
		{
			idLavoro=getIntent().getIntExtra("LavoroID", -1);
			idAzienda = getIntent().getIntExtra("IDAZIENDA", -1);
			nomeLavoro = getIntent().getStringExtra("NOMELAVORO");
		}
		else
		{
			int temp=getIntent().getIntExtra("LavoroID", -1);
			String temp2 = getIntent().getStringExtra("NOMELAVORO");
			int temp3=getIntent().getIntExtra("IDAZIENDA", -1);
			if(temp!=-1 && temp!=idLavoro){
				idLavoro=temp;
				nomeLavoro=temp2;
				idAzienda = temp3;}
		}
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task = new it.connessioni.VisualizzaDipendentiAssegnati(this).execute("http://lavoromatic.altervista.org/getDipendentiAssegnati.php",""+idAzienda,""+idLavoro);



		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizza_dipendenti_assegnati, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_dipendenti_assegnati,
					container, false);
			TextView nome = (TextView)rootView.findViewById(R.id.textViewNomeLavoro);
			nome.setText(nomeLavoro);
			return rootView;
		}
	}


	public void caricaLista(String result)
	{
		ListView lista =(ListView) findViewById(R.id.list);
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Ritento ", Toast.LENGTH_SHORT).show();
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if(netinfo==null)
			{
				Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
			}
			task = new it.connessioni.VisualizzaDipendentiAssegnati(this).execute("http://lavoromatic.altervista.org/getDipendentiAssegnati.php",""+idAzienda,""+idLavoro);
		}
		else{
			try{
				JSONArray array = new JSONArray(result);
				int num = array.length();

				List<Impiegato> list = new LinkedList<Impiegato>();
				for(int i=0;i<num;i++)
				{
					JSONObject obj = array.getJSONObject(i);
					Impiegato temp = new Impiegato(obj.getString("Nome"),obj.getString("Cognome"),
							obj.getString("Email"),obj.getString("Password"),obj.getInt("idUtente"), obj.getInt("idAzienda"),obj.getString("Ruolo"));
					list.add(temp);
				}
				AdapterDipendentiAssegnati adapter = new AdapterDipendentiAssegnati(this, R.layout.customrow,list);
				lista.setAdapter(adapter);

				if(num>0)
				{
					LinearLayout linear = (LinearLayout)findViewById(R.id.LinearAssegnati);
					ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
					cerchio.setVisibility(View.INVISIBLE);
					linear.setVisibility(View.VISIBLE);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Nessun dipendente Assegnato", Toast.LENGTH_SHORT).show();
					super.onBackPressed();
				}


			}catch(Exception e)
			{
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

			}
		}
	}

}
