package applicaton.lavoro_matic_test;

import it.connessioni.Login;
import it.connessioni.RegistraAzienda;
import it.interfacce.Impiegato;
import it.interfacce.Utente;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import applicaton.lavoro_matic_test.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoadingActivity extends Activity {
	private static AsyncTask<String, String, String> task,task2;
	private static LoadingActivity meStesso;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;




	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_loading);
		meStesso = this;
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		try {
			InputStream is = getApplicationContext().openFileInput("login");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			EditText temp = (EditText) findViewById(R.id.edit_email_login);
			temp.setText(br.readLine());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

		} catch (IOException e) {
			// TODO Auto-generated catch block

		}

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
		.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView
					.animate()
					.translationY(visible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE
							: View.GONE);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public void aBuonFine(String result)
	{

		try {
			JSONObject me = new JSONObject(result);
			String nome,cognome,email,password;
			int idUtente,idAzienda;
			nome = me.getString("Nome");
			cognome = me.getString("Cognome");
			email = me.getString("Email");
			password = me.getString("Password");
			idUtente = me.getInt("idUtente");
			idAzienda = me.getInt("idAzienda");
			int amministratore = me.getInt("Amministratore");

			Utente mySelf = new Utente(nome,cognome,email,password,idUtente,idAzienda,Boolean.valueOf(me.getString("Amministratore")));
			if(amministratore == 1){
				Intent intent = new Intent(this,HomePage_amm.class);
				intent.putExtra("mySelf_utente", mySelf);
				Button dumpy = (Button)findViewById(R.id.dummy_button);
				dumpy.setEnabled(true);
				EditText temp = (EditText) findViewById(R.id.edit_email_login);
				temp.setEnabled(true);
				temp = (EditText) findViewById(R.id.password_login);
				temp.setEnabled(true);
				Button registra = (Button) findViewById(R.id.registra);
				registra.setEnabled(true);

				startActivity(intent);
				dumpy.setEnabled(true);
				String filename = "login";
				String string = mySelf.getEmail();
				FileOutputStream outputStream;
				try{
					outputStream = openFileOutput(filename, getApplicationContext().MODE_PRIVATE);
					outputStream.write(string.getBytes());
					outputStream.close();
				}catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				Impiegato impiegato = new Impiegato(mySelf.getNome(),mySelf.getCognome(),mySelf.getEmail(),mySelf.getPassword(),mySelf.getId(),mySelf.getIdAzienda(),me.getString("Ruolo"));
				Intent intent = new Intent(this,HomePage_dip.class);
				intent.putExtra("mySelf_impiegato", impiegato);
				Button dumpy = (Button)findViewById(R.id.dummy_button);
				dumpy.setEnabled(true);
				EditText temp = (EditText) findViewById(R.id.edit_email_login);
				temp.setEnabled(true);
				temp = (EditText) findViewById(R.id.password_login);
				temp.setEnabled(true);
				Button registra = (Button) findViewById(R.id.registra);
				registra.setEnabled(true);

				startActivity(intent);


				String filename = "login";
				String string = impiegato.getEmail();
				FileOutputStream outputStream;
				dumpy.setEnabled(true);
				try{
					outputStream = openFileOutput(filename, getApplicationContext().MODE_PRIVATE);
					outputStream.write(string.getBytes());
					outputStream.close();
				}catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Email o psw sbagliata", Toast.LENGTH_SHORT).show();

			Button dumpy = (Button)findViewById(R.id.dummy_button);
			dumpy.setEnabled(true);
			EditText temp = (EditText) findViewById(R.id.edit_email_login);
			temp.setEnabled(true);
			temp = (EditText) findViewById(R.id.password_login);
			temp.setEnabled(true);
		}


	}

	public void doIntent(View view)
	{
		Button dumpy = (Button)findViewById(R.id.dummy_button);
		dumpy.setEnabled(false);
		Button registra = (Button) findViewById(R.id.registra);
		registra.setEnabled(false);
		EditText temp = (EditText) findViewById(R.id.edit_email_login);
		String email = temp.getText().toString();
		temp.setEnabled(false);
		temp = (EditText) findViewById(R.id.password_login);
		temp.setEnabled(false);
		String password = temp.getText().toString();
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if(netinfo==null)
		{
			Toast.makeText(getApplicationContext(), "Ripristino connessione in corso.. attendere..", Toast.LENGTH_LONG).show();
		}
		task=new Login(this).execute("http://lavoromatic.altervista.org/login.php",email,password);
	}

	public void doRegistra(View view)
	{
		LayoutInflater inflater = getLayoutInflater();
		new AlertDialog.Builder(this)
		.setTitle("Registra Azienda")
		.setView(inflater.inflate(R.layout.dialogregistra, null))
		.setMessage("Registra la tua azienda")
		.setPositiveButton("Registra", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Dialog dialog2 = Dialog.class.cast(dialog);
				EditText nomeAzienda,nome,cognome,password,email;
				nomeAzienda = (EditText)dialog2.findViewById(R.id.editTextNomeAzienda);
				nome = (EditText)dialog2.findViewById(R.id.editTextNome);
				cognome = (EditText)dialog2.findViewById(R.id.editTextCognome);
				password = (EditText)dialog2.findViewById(R.id.editTextPassword);
				email = (EditText)dialog2.findViewById(R.id.editTextEmail);

				String nameAzienda,name,surname,psw,imail;
				nameAzienda = nomeAzienda.getText().toString();
				name = nome.getText().toString();
				surname = cognome.getText().toString();
				psw = password.getText().toString();
				imail = email.getText().toString();

				if(nameAzienda.length()>0 && name.length()>0 && surname.length()>0 && psw.length()>0 && imail.length()>0 )
				{
					if(imail.contains("@"))
					{
						ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo netinfo = cm.getActiveNetworkInfo();
						if(netinfo!=null&&netinfo.isConnectedOrConnecting())
						{
							task2= new RegistraAzienda(meStesso).execute("http://lavoromatic.altervista.org/registraAzienda.php",nameAzienda,name,surname,imail,psw);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Connessione assente\n ripristina la connessione e riprova", Toast.LENGTH_SHORT).show();
						}
						Button dummy = (Button) findViewById(R.id.dummy_button);
						dummy.setEnabled(false);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_LONG).show();
					}
				}
				else
					Toast.makeText(getApplicationContext(), "Nessun campo dev'essere vuoto", Toast.LENGTH_LONG).show();


			}
		})
		.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {


			}
		})
		.show();
	}

	public void registrazioneEffettuata(String result)
	{
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		Button dummy = (Button) findViewById(R.id.dummy_button);
		dummy.setEnabled(true);
	}
}
