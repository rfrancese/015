package applicaton.lavoro_matic_test;

import org.json.JSONException;
import org.json.JSONObject;

import it.connessioni.CaricaDipendente;
import it.connessioni.CaricaMySelf;
import it.listeners.StartModificaDipendente;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.os.Build;

public class Impostazioni extends ActionBarActivity {
	private static int idUtente;
	private static boolean started=false;
	private static AsyncTask<String, String, String> task;

	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impostazioni);

		if(!started)
		{
			Intent intent = getIntent();
			idUtente = intent.getExtras().getInt("IDUTENTE");
			started=true;
		}

		task=new CaricaMySelf(this).execute("http://lavoromatic.altervista.org/getMySelf.php",""+idUtente);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.impostazioni, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_impostazioni,
					container, false);
			return rootView;
		}
	}

	public void caricato(String result)
	{
		try{

			JSONObject temp = new JSONObject(result);
			TextView nome,cognome,email;

			nome = (TextView) findViewById(R.id.text_nome);
			cognome=(TextView) findViewById(R.id.text_cognome);
			email=(TextView) findViewById(R.id.text_email);
			nome.setText(temp.getString("Nome"));
			cognome.setText(temp.getString("Cognome"));
			email.setText(temp.getString("Email"));
			ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
			ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
			cerchio.setVisibility(View.INVISIBLE);
			scroll.setVisibility(View.VISIBLE);

		}catch(JSONException e)
		{
			e.printStackTrace();
		}
	}

	public void modifica(View view)
	{
		Button bottone =(Button)findViewById(R.id.button1);
		bottone.setEnabled(false);
		Intent intent = new Intent(this,ModificaImpostazioni.class);
		intent.putExtra("IDUTENTE", idUtente);
		startActivity(intent);
		bottone.setEnabled(true);
		
	}

}
