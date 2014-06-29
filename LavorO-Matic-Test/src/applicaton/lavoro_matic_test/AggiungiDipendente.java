package applicaton.lavoro_matic_test;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.UpnpServiceResponseListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AggiungiDipendente extends ActionBarActivity {
	private static int idAzienda;
	private static boolean started;
	private static AggiungiDipendente myself;
	private static EditText nome,cognome,email,password,ruolo;
	private static Button aggiungi;
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
		setContentView(R.layout.activity_aggiungi_dipendente);
		myself=this;
		if(!started)
		{
			Intent intent = getIntent();
			idAzienda= intent.getExtras().getInt("IDAZIENDA");
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
		getMenuInflater().inflate(R.menu.aggiungi_dipendente, menu);
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
					R.layout.fragment_aggiungi_dipendente, container, false);

			aggiungi = (Button)rootView.findViewById(R.id.add_button_employee);
			it.listeners.AggiungiDipendente list = new it.listeners.AggiungiDipendente(myself);
			aggiungi.setOnClickListener(list);
			nome=(EditText)rootView.findViewById(R.id.editText_nome);
			cognome=(EditText)rootView.findViewById(R.id.editText_cognome);
			email=(EditText)rootView.findViewById(R.id.editText_email);
			password=(EditText)rootView.findViewById(R.id.editText_password);
			ruolo=(EditText)rootView.findViewById(R.id.editText_ruolo);


			return rootView;
		}
	}

	public void aggiungi()
	{
		String nome,cognome,email,password,ruolo;
		nome=this.nome.getText().toString();
		cognome=this.cognome.getText().toString();
		email = this.email.getText().toString();
		password= this.password.getText().toString();
		ruolo= this.ruolo.getText().toString();
		if(
				nome.equalsIgnoreCase("") || cognome.equalsIgnoreCase("") || email.equalsIgnoreCase("")
				|| password.equalsIgnoreCase("") || ruolo.equalsIgnoreCase(""))
		{
			if(!email.contains("@"))
				Toast.makeText(getApplicationContext(), "L'email non è valida ", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "Nessun campo dev'essere vuoto ", Toast.LENGTH_LONG).show();
		}else{
		task= new it.connessioni.AggiungiDipendente(myself).execute("http://lavoromatic.altervista.org/InserisciDipendente.php",""+idAzienda,nome,cognome,email,password,ruolo);
		aggiungi.setEnabled(false);
		}
	}

	public void aggiunto(String result)
	{
		/*
		aggiungi.setEnabled(true);
		this.nome.setText("");
		this.cognome.setText("");
		this.email.setText("");
		this.password.setText("");
		this.ruolo.setText("");*/
		Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this,HomePage_amm.class);
		intent.putExtra("mySelf_utente", getIntent().getExtras().getSerializable("UTENTE"));
		startActivity(intent);
		
	}

}
