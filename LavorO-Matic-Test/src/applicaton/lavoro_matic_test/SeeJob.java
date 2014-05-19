package applicaton.lavoro_matic_test;

import it.connessioni.CaricaLavoro;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class SeeJob extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_job);
		Intent intent = getIntent();
		int idLavoro = intent.getIntExtra("LavoroID", -1);
		new CaricaLavoro(this).execute("http://lavoromatic.altervista.org/getLavoro.php",""+idLavoro);
		
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
			View rootView = inflater.inflate(R.layout.fragment_see_job,
					container, false);
			return rootView;
		}
	}
	
	public void caricaLavoro(String result)
	{
		try {
			JSONObject temp = new JSONObject(result);
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			TextView nome,descrizione,indirizzo,val_progresso;
			Button mappa;
			ProgressBar caricamento,progresso;
			nome = (TextView)findViewById(R.id.see_nome);
			descrizione = (TextView)findViewById(R.id.see_descrizione);
			indirizzo = (TextView)findViewById(R.id.see_indirizzo);
			val_progresso=(TextView)findViewById(R.id.see_testo_progresso);
			mappa= (Button)findViewById(R.id.see_Mappa);
			progresso = (ProgressBar)findViewById(R.id.see_progresso);
			caricamento = (ProgressBar)findViewById(R.id.see_caricamento);
			int percentuale = temp.getInt("percentuale");
			nome.setText(temp.getString("Nome"));
			descrizione.setText(temp.getString("Descrizione"));
			indirizzo.setText(temp.getString("Indirizzo"));
			val_progresso.setText("Progresso: "+percentuale);
//			implementare listener button mappa
			progresso.setProgress(percentuale);
			caricamento.setVisibility(View.INVISIBLE);
			nome.setVisibility(View.VISIBLE);
			descrizione.setVisibility(View.VISIBLE);
			indirizzo.setVisibility(View.VISIBLE);
			progresso.setVisibility(View.VISIBLE);
			val_progresso.setVisibility(View.VISIBLE);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
