package applicaton.lavoro_matic_test;

import org.json.JSONException;
import org.json.JSONObject;

import it.connessioni.CancellaDipendente;
import it.connessioni.CaricaDipendente;
import it.interfacce.Impiegato;
import it.listeners.StartModificaDipendente;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizzaDipendente extends ActionBarActivity {

	private static boolean started = false;
	private static int idUtente;
	private static AsyncTask<String, String, String> task,task2;

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!= AsyncTask.Status.FINISHED)
			task.cancel(true);
		
	}


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizza_dipendente);
		task2=new CancellaDipendente(this);
		task = new CaricaDipendente(this);

		if(!started){
			Intent intent = getIntent();
			idUtente = intent.getExtras().getInt("IDDIPENDENTE");
			started=true;
		}
		else
		{
			int temp = getIntent().getIntExtra("IDDIPENDENTE", -1);
			if(temp!=-1 && temp!=idUtente)
				idUtente=temp;
		}

		task.execute("http://lavoromatic.altervista.org/getDipendente.php",""+idUtente);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizza_dipendente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.cancella_dipendente) {
			new AlertDialog.Builder(this)
			.setTitle("Cancella Dipendente")
			.setMessage("Sei sicuro di voler eliminare il Dipendente?")
			.setPositiveButton("Sì", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					task2.execute("http://lavoromatic.altervista.org/CancellaDipendente.php",""+idUtente);

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
					R.layout.fragment_visualizza_dipendente, container, false);
			return rootView;
		}
	}

	public void leggiDipendente(String result)
	{
		try{
			JSONObject temp = new JSONObject(result);
			TextView nome,cognome,email,ruolo;
			Button modifica = (Button)findViewById(R.id.button1);
			StartModificaDipendente list = new StartModificaDipendente(this, idUtente);
			modifica.setOnClickListener(list);
			nome = (TextView) findViewById(R.id.text_nome);
			cognome=(TextView) findViewById(R.id.text_cognome);
			email=(TextView) findViewById(R.id.text_email);
			ruolo=(TextView) findViewById(R.id.text_ruolo);
			nome.setText(temp.getString("Nome"));
			cognome.setText(temp.getString("Cognome"));
			email.setText(temp.getString("Email"));
			ruolo.setText(temp.getString("Ruolo"));
			ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
			ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
			cerchio.setVisibility(View.INVISIBLE);
			scroll.setVisibility(View.VISIBLE);

		}catch(JSONException e)
		{
			e.printStackTrace();
		}
	}

	public void eliminato(String result)
	{
		Intent intent = new Intent(this,HomePage_amm.class);
		intent.putExtra("mySelf_utente", getIntent().getExtras().getSerializable("mySelf_utente"));
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		startActivity(intent);

	}
}
