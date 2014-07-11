package applicaton.lavoro_matic_test;

import it.connessioni.CancellaCommento;
import it.connessioni.CaricaCommenti;
import it.connessioni.CaricaNonAssegnati;
import it.connessioni.InserisciCommento;
import it.interfacce.Commento;
import it.interfacce.Utente;
import it.listeners.AdapterCommenti;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GestioneCommenti extends ActionBarActivity {

	private static AsyncTask<String,String,String> task,task2,task3;
	private static int idLavoro;
	private static boolean started=false;
	private static String nomeLavoro;
	private static Utente mySelf;
	private static GestioneCommenti meStesso;


	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!= AsyncTask.Status.FINISHED)
			task.cancel(true);
		if(task2!=null && task2.getStatus()!= AsyncTask.Status.FINISHED)
			task2.cancel(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestione_commenti);
		meStesso = this;
		if(!started)
		{
			Intent intent = getIntent();
			idLavoro = intent.getIntExtra("LavoroID", -1);
			nomeLavoro=intent.getStringExtra("NOMELAVORO");
			mySelf = (Utente)intent.getSerializableExtra("mySelf_utente");
		}
		else
		{
			Intent intent = getIntent();
			int temp = intent.getIntExtra("LavoroID", -1);
			String temp2= intent.getStringExtra("NOMELAVORO");
			if(temp != idLavoro && temp != -1)
			{
				idLavoro = temp;
				nomeLavoro=temp2;
			}
		}
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task = new CaricaCommenti(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);



		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gestione_commenti, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.aggiungi_commento) {
			LayoutInflater inflater = getLayoutInflater();
			new AlertDialog.Builder(this)
			.setTitle("Aggiungi Commento")
			.setView(inflater.inflate(R.layout.customdialog, null))
			.setMessage("Scrivi il Commento da aggiungere")
			.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {


					Dialog dialog2 = Dialog.class.cast(dialog);
					EditText testo =(EditText) dialog2.findViewById(R.id.editTextTesto);
					String text = testo.getText().toString();
					ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo netinfo = cm.getActiveNetworkInfo();
					if(netinfo==null)
					{
						Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
					}
					task2= new InserisciCommento(meStesso).execute("http://lavoromatic.altervista.org/AggiungiCommento.php",""+idLavoro,""+""+mySelf.getId(),text);



				}
			})
			.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {


				}
			})
			.show();
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
					R.layout.fragment_gestione_commenti, container, false);
			TextView lavoro = (TextView)rootView.findViewById(R.id.textViewNomeLavoro);
			lavoro.setText(nomeLavoro);
			return rootView;
		}
	}


	public void caricaCommenti(String result)
	{
		ListView lista =(ListView) findViewById(R.id.listView1);
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Ritento ", Toast.LENGTH_SHORT).show();
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if(netinfo==null)
			{
				Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
			}
			task = new CaricaCommenti(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);
		}
		else{
			try{
				JSONArray array = new JSONArray(result);
				int num = array.length();

				List<Commento> list = new LinkedList<Commento>();

				for(int i=0;i<num;i++)
				{
					JSONObject obj = array.getJSONObject(i);
					Commento temp = new Commento(idLavoro,obj.getInt("idAggiornamento"),obj.getString("Cognome")+" "+obj.getString("Nome"),obj.getString("Testo"));


					list.add(temp);
				}
				AdapterCommenti adapter = new AdapterCommenti(this, R.layout.rowcommenti,list);
				lista.setAdapter(adapter);
				lista.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> adapter, View view,
							int position, long id) {

						Commento commento =(Commento)adapter.getItemAtPosition(position);


						final int idAggiornamento = commento.getIdAggiornamento();

						new AlertDialog.Builder(meStesso)
						.setTitle("Cancella commento")
						.setMessage("Vuoi davvero cancellare il commento?")
						.setPositiveButton("Sì", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {


								ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
								NetworkInfo netinfo = cm.getActiveNetworkInfo();
								if(netinfo==null)
								{
									Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
								}
								task3=new CancellaCommento(meStesso).execute("http://lavoromatic.altervista.org/cancellaCommento.php",""+idAggiornamento);



								meStesso.caricamento();

							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {


							}
						})
						.show();

						return true;
					}
				});

				if(num>0)
				{
					LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
					ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
					cerchio.setVisibility(View.INVISIBLE);
					scroll.setVisibility(View.VISIBLE);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Nessun Commento", Toast.LENGTH_SHORT).show();
					LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
					ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
					cerchio.setVisibility(View.INVISIBLE);
					scroll.setVisibility(View.VISIBLE);
				}


			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void inserimentoEffettuato(String result)
	{
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Perfavore riprovaare", Toast.LENGTH_SHORT).show();
			
		}
		else
		{
			LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
			ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
			cerchio.setVisibility(View.VISIBLE);
			scroll.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if(netinfo==null)
			{
				Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
			}

			task = new CaricaCommenti(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);
		}

	}

	public void commentoCancellato(String result)
	{
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Perfavore riprovare", Toast.LENGTH_SHORT).show();
			
		}
		LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
		ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
		cerchio.setVisibility(View.VISIBLE);
		scroll.setVisibility(View.INVISIBLE);
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task = new CaricaCommenti(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);
	}

	public void caricamento()
	{
		LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
		ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
		cerchio.setVisibility(View.VISIBLE);
		scroll.setVisibility(View.INVISIBLE);
	}

}
