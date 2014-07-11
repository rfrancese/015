package applicaton.lavoro_matic_test;

import it.connessioni.CaricaLavoriDipendente;
import it.interfacce.Impiegato;
import it.interfacce.Lavoro;
import it.listeners.AdapterLavori;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HomePage_dip extends ActionBarActivity {
	private static AsyncTask<String, String, String> task;
	private static boolean started=false;
	private static Impiegato mySelf;
	private static HomePage_dip meStesso;

	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page_dip);
		meStesso=this;
		if(!started)
		{
			Intent intent = getIntent();
			mySelf = (Impiegato)intent.getSerializableExtra("mySelf_impiegato");
			started = true;
		}
		else
		{
			Intent intent = getIntent();
			Impiegato temp = (Impiegato)intent.getSerializableExtra("mySelf_impiegato");
			if(temp!=null && mySelf.getId()!=temp.getId())
				mySelf=temp;
		}

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task = new CaricaLavoriDipendente(this).execute("http://lavoromatic.altervista.org/getWorksDipendenti.php",""+mySelf.getIdAzienda(),""+mySelf.getId());



		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page_dip, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_home_page_dip,
					container, false);
			return rootView;
		}
	}


	public void lavoriCaricati(String result)
	{
		ListView lista =(ListView) findViewById(R.id.listViewDipendenti);
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Ritento ", Toast.LENGTH_SHORT).show();
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if(netinfo==null)
			{
				Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
			}
			task = new CaricaLavoriDipendente(this).execute("http://lavoromatic.altervista.org/getWorksDipendenti.php",""+mySelf.getIdAzienda(),""+mySelf.getId());
		}
		else{
			try{
				JSONArray array = new JSONArray(result);
				int num = array.length();

				List<Lavoro> list = new LinkedList<Lavoro>();
				if(num<1)
				{
					Toast.makeText(getApplicationContext(), "Non hai nessun lavoro assegnato", Toast.LENGTH_LONG).show();
				}
				for(int i=0;i<num;i++)
				{
					JSONObject obj = array.getJSONObject(i);
					Lavoro temp = new Lavoro(obj.getInt("idLavoro"),obj.getInt("Percentuale"),obj.getString("Nome"),obj.getString("Descrizione"),obj.getString("Indirizzo"));
					list.add(temp);
				}
				AdapterLavori adapter = new AdapterLavori(this, R.layout.rowlavori, list);
				lista.setAdapter(adapter);
				lista.setOnItemClickListener(new OnItemClickListener() {


					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long id) {

						Lavoro l =(Lavoro) adapter.getItemAtPosition(position);
						int idLavoro = l.getId();
						Intent intent = new Intent(meStesso,SeeJob_dip.class);
						intent.putExtra("LavoroID", idLavoro);
						intent.putExtra("mySelf_impiegato", mySelf);
						startActivity(intent);

					}				
				});

				ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
				LinearLayout principale = (LinearLayout) findViewById(R.id.LinearPrincipale);
				cerchio.setVisibility(View.INVISIBLE);
				principale.setVisibility(View.VISIBLE);
			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

}
