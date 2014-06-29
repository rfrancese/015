package applicaton.lavoro_matic_test;

import it.connessioni.CancellaLavoro;
import it.connessioni.CaricaLavoro;
import it.interfacce.Lavoro;
import it.listeners.StartModifyJob;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SeeJob extends ActionBarActivity {

	private static int idLavoro;
	private static boolean started = false;
	private static Lavoro lavoro;
	private static AsyncTask<String, String, String> task,task2;
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus() != AsyncTask.Status.FINISHED)
			task.cancel(true);
		if(task2!=null && task2.getStatus() != AsyncTask.Status.FINISHED)
			task2.cancel(true);
		
		started=false;
	}
	



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_job);
		task=new CaricaLavoro(this);
		task2=new CancellaLavoro(this);
		if(!started){
			Intent intent = getIntent();
			idLavoro = intent.getIntExtra("LavoroID", -1);
			started=true;
		}
		task.execute("http://lavoromatic.altervista.org/getLavoro.php",""+idLavoro);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.see_job, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.cancella_lavoro) {
			new AlertDialog.Builder(this)
			.setTitle("Cancella Lavoro?")
			.setMessage("Sei sicuro di voler eliminare il Lavoro?")
			.setPositiveButton("Sì", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					task2.execute("http://lavoromatic.altervista.org/CancellaLavoro.php",""+idLavoro);
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
			View rootView = inflater.inflate(R.layout.fragment_see_job,
					container, false);
			return rootView;
		}
	}

	public void caricaLavoro(String result)
	{
		try {
			JSONObject temp = new JSONObject(result);
			TextView nome,descrizione,indirizzo;
			Button mappa,modifica;
			lavoro = new Lavoro(temp.getInt("idLavoro"), temp.getInt("percentuale"), temp.getString("Nome"), temp.getString("Descrizione"), temp.getString("Indirizzo"));
			ProgressBar caricamento;
			nome = (TextView)findViewById(R.id.see_nome);
			descrizione = (TextView)findViewById(R.id.see_descrizione);
			indirizzo = (TextView)findViewById(R.id.see_indirizzo);
			//			mappa= (Button)findViewById(R.id.see_Mappa);
			modifica = (Button)findViewById(R.id.modify_button);
			StartModifyJob start = new StartModifyJob(lavoro.getId(), this);
			modifica.setOnClickListener(start);
			caricamento = (ProgressBar)findViewById(R.id.see_caricamento);
			int percentuale = temp.getInt("percentuale");
			nome.setText(temp.getString("Nome"));
			descrizione.setText(temp.getString("Descrizione"));
			indirizzo.setText(temp.getString("Indirizzo"));
			//			implementare listener button mappa
			caricamento.setVisibility(View.INVISIBLE);
			LinearLayout principale = (LinearLayout)findViewById(R.id.LinearPrincipale);
			principale.setVisibility(View.VISIBLE);


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Lavoro non trovato, qualcuno deve averlo cancellato", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this,HomePage_amm.class);
			intent.putExtra("mySelf_utente", getIntent().getExtras().getSerializable("mySelf_utente"));
			startActivity(intent);
		}
	}

	public void changeStarted(boolean input)
	{
		started=input;
	}

	public void cancellato(String result)
	{
		Intent intent = new Intent(this,HomePage_amm.class);
		intent.putExtra("mySelf_utente", getIntent().getExtras().getSerializable("mySelf_utente"));
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		startActivity(intent);
	}
}
