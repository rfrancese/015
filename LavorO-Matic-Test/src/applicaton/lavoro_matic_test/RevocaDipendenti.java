package applicaton.lavoro_matic_test;

import java.util.StringTokenizer;

import it.connessioni.CaricaDipendentiAssegnati;
import it.connessioni.CaricaModificaLavoro;
import it.connessioni.Revoca;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RevocaDipendenti extends ActionBarActivity {

	private static AsyncTask<String, String, String> task,task2;
	private static int idLavoro,idAzienda;
	private static String nomeLavoro;
	private static boolean started=false;

	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
		if(task2!=null && task2.getStatus()!=AsyncTask.Status.FINISHED)
			task2.cancel(true);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revoca_dipendenti);
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
		task = new CaricaDipendentiAssegnati(this).execute("http://lavoromatic.altervista.org/getDipendentiAssegnati.php",""+idAzienda,""+idLavoro);


		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.revoca_dipendenti, menu);
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
					R.layout.fragment_revoca_dipendenti, container, false);
			TextView text = (TextView)rootView.findViewById(R.id.nomeLavoro);
			text.setText(nomeLavoro);
			return rootView;
		}
	}


	public void caricaLista(String result)
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
			task = new CaricaDipendentiAssegnati(this).execute("http://lavoromatic.altervista.org/getDipendentiAssegnati.php",""+idAzienda,""+idLavoro);
		}
		else{
			try{
				JSONArray array = new JSONArray(result);
				int num = array.length();

				String[] elements = new String[num];
				for(int i=0;i<num;i++)
				{
					JSONObject obj = array.getJSONObject(i);
					String temp = obj.getString("idUtente")+". "+obj.getString("Nome")+" "+obj.getString("Cognome")+"\n - "+obj.getString("Ruolo");
					elements[i]=temp;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row,R.id.textViewList,elements);
				lista.setAdapter(adapter);


				if(num>0)
				{
					LinearLayout linear = (LinearLayout)findViewById(R.id.LinearAssegna);
					ProgressBar cerchio = (ProgressBar)findViewById(R.id.progressBar1);
					cerchio.setVisibility(View.INVISIBLE);
					linear.setVisibility(View.VISIBLE);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Nessun dipendente Assegnato", Toast.LENGTH_SHORT).show();
					super.onBackPressed();
				}


			}catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

			}
		}
	}

	public void revoca(View view)
	{
		ListView lista =(ListView) findViewById(R.id.listView1);
		Button bottone =(Button)findViewById(R.id.button1);
		bottone.setEnabled(false);
		String daRevocare="";

		int num = lista.getChildCount();
		for(int i=0;i<num;i++)
		{
			LinearLayout viuw =(LinearLayout) lista.getChildAt(i);
			LinearLayout temp2 = (LinearLayout)viuw.getChildAt(1);
			CheckBox box = (CheckBox)temp2.getChildAt(0);
			if(box.isChecked())
			{
				temp2 = (LinearLayout)viuw.getChildAt(0);
				TextView text = (TextView) temp2.getChildAt(0);
				String temp = text.getText().toString();

				StringTokenizer token = new StringTokenizer(temp,".");

				String codice = token.nextToken();


				if(daRevocare.length()==0){
					daRevocare+=""+codice;
				}
				else
					daRevocare+=","+codice;
			}
		}

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso..", Toast.LENGTH_LONG).show();
		}
		task2=new Revoca(this).execute("http://lavoromatic.altervista.org/revocaDipendente.php",daRevocare,""+idLavoro);



	}


	public void done(String result)
	{
		if(result==null)
		{
			Toast.makeText(getApplicationContext(), "Problemi di connessione, Ritento ", Toast.LENGTH_SHORT).show();
			this.revoca(getCurrentFocus());
		}
		else{
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		Button bottone =(Button)findViewById(R.id.button1);
		super.onBackPressed();
		bottone.setEnabled(true);
		}
	}

}
