package applicaton.lavoro_matic_test;

import it.connessioni.CaricaCommenti;
import it.connessioni.CaricaCommentiDipendente;
import it.connessioni.InserisciCommentoDipendente;
import it.interfacce.Commento;
import it.interfacce.Impiegato;
import it.listeners.AdapterCommenti;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GestioneCommentiDipendente extends ActionBarActivity {

	private static AsyncTask<String,String,String> task,task2,task3;
	private static int idLavoro;
	private static boolean started=false;
	private static String nomeLavoro;
	private static Impiegato mySelf;
	private static GestioneCommentiDipendente meStesso;


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
		setContentView(R.layout.activity_gestione_commenti_dipendente);
		meStesso=this;

		if(!started)
		{
			Intent intent = getIntent();
			idLavoro = intent.getIntExtra("LavoroID", -1);
			nomeLavoro=intent.getStringExtra("NOMELAVORO");
			mySelf = (Impiegato)intent.getSerializableExtra("mySelf_impiegato");
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
		task = new CaricaCommentiDipendente(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);

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

							String autore = mySelf.getCognome()+" "+mySelf.getNome();
							Dialog dialog2 = Dialog.class.cast(dialog);
							EditText testo =(EditText) dialog2.findViewById(R.id.editTextTesto);
							String text = testo.getText().toString();
							task2= new InserisciCommentoDipendente(meStesso).execute("http://lavoromatic.altervista.org/AggiungiCommento.php",""+idLavoro,""+mySelf.getId(),text);
							meStesso.caricamento();
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
					R.layout.fragment_gestione_commenti_dipendente, container,
					false);
			TextView lavoro = (TextView)rootView.findViewById(R.id.textViewNomeLavoro);
			lavoro.setText(nomeLavoro);
			return rootView;
		}
	}

	public void caricaCommenti(String result)
	{
		ListView lista =(ListView) findViewById(R.id.listView1);
		
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
	
	public void inserimentoEffettuato(String result)
	{
		LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
		ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
		cerchio.setVisibility(View.VISIBLE);
		scroll.setVisibility(View.INVISIBLE);
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

		task = new CaricaCommentiDipendente(this).execute("http://lavoromatic.altervista.org/getCommenti.php",""+idLavoro);
	}
	
	
	public void caricamento()
	{
		LinearLayout scroll = (LinearLayout)findViewById(R.id.LinearCommenti);
		ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
		cerchio.setVisibility(View.VISIBLE);
		scroll.setVisibility(View.INVISIBLE);
		
	}

}
