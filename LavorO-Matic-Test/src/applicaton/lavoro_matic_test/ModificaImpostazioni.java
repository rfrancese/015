package applicaton.lavoro_matic_test;

import it.connessioni.CaricaModificaMySelf;

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

public class ModificaImpostazioni extends ActionBarActivity {
	private static AsyncTask<String,String,String> task,task2;
	private static boolean started=false;
	private static int idUtente;

	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);

		if(task2!=null && task2.getStatus()!=AsyncTask.Status.FINISHED)
			task2.cancel(true);

		started=false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifica_impostazioni);

		if(!started)
		{
			Intent intent = getIntent();
			idUtente = intent.getIntExtra("IDUTENTE", -1);
			started = true;
		}
		task = new CaricaModificaMySelf(this).execute("http://lavoromatic.altervista.org/getMySelf.php",""+idUtente);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modifica_impostazioni, menu);
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
					R.layout.fragment_modifica_impostazioni, container, false);
			return rootView;
		}
	}


	public void caricaDati(String result)
	{
		try{
			JSONObject temp = new JSONObject(result);
			EditText nome,cognome,email,password;
			Button modifica = (Button) findViewById(R.id.button1);
			nome = (EditText) findViewById(R.id.editText_nome);
			cognome=(EditText) findViewById(R.id.editText_cognome);
			email=(EditText) findViewById(R.id.editText_email);
			password=(EditText)findViewById(R.id.editText_password);

			nome.setText(temp.getString("Nome"));
			cognome.setText(temp.getString("Cognome"));
			email.setText(temp.getString("Email"));
			password.setText(temp.getString("Password"));

			ProgressBar caricamento = (ProgressBar)findViewById(R.id.caricamento_mod_dip);
			caricamento.setVisibility(View.INVISIBLE);
			ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
			scroll.setVisibility(View.VISIBLE);
		}catch(JSONException e)
		{

		}
	}

	public void eseguiModifica(View view)
	{
		Button bottone = (Button) findViewById(R.id.button1);
		bottone.setEnabled(false);
		EditText nome,cognome,email,password;
		nome = (EditText) findViewById(R.id.editText_nome);
		cognome=(EditText) findViewById(R.id.editText_cognome);
		email=(EditText) findViewById(R.id.editText_email);
		password=(EditText)findViewById(R.id.editText_password);
		task2= new it.connessioni.ModificaMySelf(this).execute("http://lavoromatic.altervista.org/modificaMySelf.php",""+idUtente,nome.getText().toString(),cognome.getText().toString(),email.getText().toString(),password.getText().toString());
	}

	public void modificaEffettuata(String result)
	{
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this,Impostazioni.class);
		intent.putExtra("IDUTENTE", idUtente);
		Button bottone = (Button) findViewById(R.id.button1);
		startActivity(intent);
		bottone.setEnabled(true);

	}
}
