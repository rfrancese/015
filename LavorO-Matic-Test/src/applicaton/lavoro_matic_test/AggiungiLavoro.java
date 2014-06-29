package applicaton.lavoro_matic_test;

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
import android.widget.Toast;

public class AggiungiLavoro extends ActionBarActivity {

	private static boolean started=false;
	private static int idAzienda;
	private static AsyncTask<String, String, String> task;

	
	protected void onDestroy()
	{
		super.onDestroy();
		if(task!=null && task.getStatus()!=AsyncTask.Status.FINISHED)
			task.cancel(true);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aggiungi_lavoro);
		if(!started)
		{
			Intent intent = getIntent();
			idAzienda = intent.getIntExtra("IDAZIENDA", -1);
			started=true;
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aggiungi_lavoro, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_aggiungi_lavoro,
					container, false);
			return rootView;
		}
	}
	
	public void doTask(View view)
	{
		Button aggiungi = (Button)findViewById(R.id.button1);
		aggiungi.setEnabled(false);
		EditText nome,descrizione,indirizzo;
		nome=(EditText)findViewById(R.id.editText_nome);
		descrizione=(EditText)findViewById(R.id.editText_descrizione);
		indirizzo = (EditText)findViewById(R.id.editText_indirizzo);
		String name,description,address;
		name= nome.getText().toString();
		description = descrizione.getText().toString();
		address = indirizzo.getText().toString();
		task=new it.connessioni.AggiungiLavoro(this);
		task.execute("http://lavoromatic.altervista.org/InserisciLavoro.php",""+idAzienda,name,description,address);
	}
	
	public void lavoroAggiunto(String result)
	{
		Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this,HomePage_amm.class);
		intent.putExtra("mySelf_utente", getIntent().getExtras().getSerializable("UTENTE"));
		startActivity(intent);
	}
}
