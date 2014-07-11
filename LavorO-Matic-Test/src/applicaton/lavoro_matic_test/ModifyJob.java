package applicaton.lavoro_matic_test;

import it.connessioni.CaricaModificaLavoro;
import it.connessioni.CaricaModificaMySelf;
import it.interfacce.Lavoro;
import it.listeners.ModificaLavoro;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyJob extends ActionBarActivity {

	private static int idLavoro;
	private static boolean started=false;
	private static AsyncTask<String, String, String> task,task2;




	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
		if(task2!=null && task2.getStatus()!=AsyncTask.Status.FINISHED)
			task2.cancel(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_job);
		if(!started)
		{
			Intent intent = getIntent();
			idLavoro = intent.getIntExtra("IDLAVORO", -1);
			started=true;
		}
		else
		{
			Intent intent = getIntent();
			int temp = intent.getIntExtra("IDLAVORO", -1);
			if(temp!=-1 && temp!=idLavoro)
				idLavoro=temp;
		}

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task=new CaricaModificaLavoro(this).execute("http://lavoromatic.altervista.org/getLavoro.php",""+idLavoro);



		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modify_job, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_modify_job,
					container, false);
			return rootView;
		}
	}


	public void caricaLavoro(String result)
	{
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Ritento ", Toast.LENGTH_SHORT).show();
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if(netinfo==null)
			{
				Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
			}
			task=new CaricaModificaLavoro(this).execute("http://lavoromatic.altervista.org/getLavoro.php",""+idLavoro);
		}
		else{
			try{

				JSONObject temp = new JSONObject(result);

				Lavoro lavoro = new Lavoro(temp.getInt("idLavoro"),temp.getInt("percentuale"),temp.getString("Nome"),temp.getString("Descrizione"),temp.getString("Indirizzo"));
				ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);

				EditText edit_nome,edit_descrizione,edit_indirizzo;

				cerchio.setVisibility(View.INVISIBLE);
				edit_nome=(EditText)findViewById(R.id.editText_nome);
				edit_descrizione=(EditText)findViewById(R.id.editText_descrizione);
				edit_indirizzo=(EditText)findViewById(R.id.editText_indirizzo);
				edit_nome.setText(lavoro.getNome());
				edit_descrizione.setText(lavoro.getDescrizione());
				edit_indirizzo.setText(lavoro.getIndirizzo());

				ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
				scroll.setVisibility(View.VISIBLE);

			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}

	}

	public void done(String result)
	{
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Perfavore riprovare.. ", Toast.LENGTH_SHORT).show();
			findViewById(R.id.button1).setEnabled(true);
		}
		else{
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		findViewById(R.id.button1).setEnabled(true);
		Intent intent = new Intent(this,SeeJob.class);
		intent.putExtra("LavoroID", idLavoro);
		startActivity(intent);
		}
	}


	public void modificaLavoro(View view)
	{
		EditText nome,descrizione,indirizzo;

		nome = (EditText)findViewById(R.id.editText_nome);
		descrizione = (EditText)findViewById(R.id.editText_descrizione);
		indirizzo=(EditText)findViewById(R.id.editText_indirizzo);

		String name = nome.getText().toString();
		String description = descrizione.getText().toString();
		String address = indirizzo.getText().toString();

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task2=new it.connessioni.ModificaLavoro(this).execute("http://lavoromatic.altervista.org/modificaLavoro.php",""+idLavoro,name,description,address);
		findViewById(R.id.button1).setEnabled(false);



	}
}
