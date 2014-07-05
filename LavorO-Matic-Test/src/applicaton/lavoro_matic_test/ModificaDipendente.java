package applicaton.lavoro_matic_test;

import it.connessioni.CaricaModificaDipendente;
import it.listeners.StartModificaDipendente;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

public class ModificaDipendente extends ActionBarActivity {
	private static AsyncTask<String, String, String> task,task2;
	private static boolean started=false;
	private static int idUtente;

	
	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus() != AsyncTask.Status.FINISHED)
			task.cancel(true);
		if(task2!=null && task2.getStatus() != AsyncTask.Status.FINISHED)
			task2.cancel(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifica_dipendente);
		task = new CaricaModificaDipendente(this);
		if(!started)
		{
			Intent intent = getIntent();
			idUtente = intent.getExtras().getInt("IDUTENTE");
			started=true;
		}
		else
		{
			Intent intent = getIntent();
			int temp = intent.getIntExtra("IDUTENTE", -1);
			if(temp!=-1 && temp != idUtente)
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
		getMenuInflater().inflate(R.menu.modifica_dipendente, menu);
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
					R.layout.fragment_modifica_dipendente, container, false);
			return rootView;
		}
	}
	
	public void caricaDati(String result)
	{
		try{
			JSONObject temp = new JSONObject(result);
			EditText nome,cognome,email,password,ruolo;
			Button modifica = (Button) findViewById(R.id.button1);
			it.listeners.ModificaDipendente list = new it.listeners.ModificaDipendente(this);
			modifica.setOnClickListener(list);
			nome = (EditText) findViewById(R.id.editText_nome);
			cognome=(EditText) findViewById(R.id.editText_cognome);
			email=(EditText) findViewById(R.id.editText_email);
			password=(EditText)findViewById(R.id.editText_password);
			ruolo=(EditText) findViewById(R.id.editText_ruolo);
			nome.setText(temp.getString("Nome"));
			cognome.setText(temp.getString("Cognome"));
			email.setText(temp.getString("Email"));
			password.setText(temp.getString("Password"));
			ruolo.setText(temp.getString("Ruolo"));
			ProgressBar caricamento = (ProgressBar)findViewById(R.id.caricamento_mod_dip);
			caricamento.setVisibility(View.INVISIBLE);
			ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
			scroll.setVisibility(View.VISIBLE);
		}catch(JSONException e)
		{
			
		}
	}
	
	public void effettuaModifica()
	{
		Button bottone = (Button) findViewById(R.id.button1);
		bottone.setEnabled(false);
		EditText nome,cognome,email,password,ruolo;
		nome = (EditText) findViewById(R.id.editText_nome);
		cognome=(EditText) findViewById(R.id.editText_cognome);
		email=(EditText) findViewById(R.id.editText_email);
		password=(EditText)findViewById(R.id.editText_password);
		ruolo=(EditText) findViewById(R.id.editText_ruolo);
		task2= new it.connessioni.ModificaDipendente(this).execute("http://lavoromatic.altervista.org/modificaDipendente.php",""+idUtente,nome.getText().toString(),cognome.getText().toString(),email.getText().toString(),password.getText().toString(),ruolo.getText().toString());
	}
	
	public void modificaEffettuata(String result)
	{
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this,VisualizzaDipendente.class);
		intent.putExtra("IDUTENTE", idUtente);
		Button bottone = (Button) findViewById(R.id.button1);
		bottone.setEnabled(true);
		startActivity(intent);
	}

}
