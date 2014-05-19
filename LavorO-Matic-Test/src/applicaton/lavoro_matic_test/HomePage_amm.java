package applicaton.lavoro_matic_test;

import it.connessioni.CaricaDipendenti;
import it.connessioni.CaricaLavori;
import it.interfacce.Impiegato;
import it.interfacce.Lavoro;
import it.interfacce.Utente;
import it.listeners.Cancella;
import it.listeners.StartSeeJob;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomePage_amm extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private static Utente mySelf;
	private static ArrayList<Lavoro> lavori;
	private static ArrayList<Impiegato> dipendenti;
	private static Lavoro lavoro;
	private static boolean started=false;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page_amm);
		if(!started){
		Intent intent = getIntent();
		mySelf=(Utente)intent.getSerializableExtra("mySelf_utente");
		started=true;}
		new CaricaLavori(this).execute("http://lavoromatic.altervista.org/getWorks.php",""+mySelf.getIdAzienda());
		new CaricaDipendenti(this).execute("http://lavoromatic.altervista.org/getDipendenti.php",""+mySelf.getIdAzienda());
		
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page_amm, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
				{
				rootView=inflater.inflate(R.layout.home_page_amm1,container, false);
				}
			else
			{
				rootView=inflater.inflate(R.layout.home_page_amm2,container, false);
			}		
			
			return rootView;
		}
		
		public void prova(View view)
		{
			Toast.makeText(view.getContext(), "funge", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void caricaLavori(String result)
	{
		lavori = new ArrayList<Lavoro>();
		try {
			ArrayList<Button> bottoni = new ArrayList<Button>();
			LinearLayout layout = (LinearLayout)mViewPager.findViewById(R.id.list1);
			JSONArray array = new JSONArray(result);
			int num = array.length();
			for(int i=0;i<num;i++)
			{
				JSONObject temp = array.getJSONObject(i);
				lavoro = new Lavoro(temp.getInt("idLavoro"),temp.getInt("Percentuale"),temp.getString("Nome"),temp.getString("Descrizione"),temp.getString("Indirizzo"));
				lavori.add(lavoro);
				Button bottone = new Button(mViewPager.getContext());
				String tasto = ""+lavoro.getId()+". "+lavoro.getNome();
				bottone.setText(tasto);
				bottone.setWidth(LayoutParams.MATCH_PARENT);
				
				StartSeeJob listener = new StartSeeJob(this, lavoro.getId());
				bottone.setOnClickListener(listener);
				bottoni.add(bottone);
			}
			RelativeLayout caricamento = (RelativeLayout)mViewPager.findViewById(R.id.caricamento1);
			layout.removeView(caricamento);
			num = bottoni.size();
			for(int i=0;i<num;i++)
			{
				layout.addView(bottoni.get(i));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void caricaDipendenti(String result)
	{
		dipendenti = new ArrayList<Impiegato>();
		ArrayList<LinearLayout> ll = new ArrayList<LinearLayout>();
		ArrayList<Button> bottoni = new ArrayList<Button>();
		ArrayList<Button> bottoni_canc = new ArrayList<Button>();
		try {
			LinearLayout layout = (LinearLayout) mViewPager.findViewById(R.id.list2);
			JSONArray array = new JSONArray(result);
			int num= array.length();
			
			for(int i=0;i<num;i++)
			{
				JSONObject temp = array.getJSONObject(i);
				Button bottone = new Button(mViewPager.getContext());
				bottone.setText(""+temp.getInt("idUtente")+". "+temp.getString("Ruolo")+": "+temp.getString("Nome")+" "+temp.getString("Cognome"));
				Button bottone_canc = new Button(mViewPager.getContext());
				bottone_canc.setText("cancella");
				
				LinearLayout linear = new LinearLayout(mViewPager.getContext());
				
				linear.setOrientation(LinearLayout.HORIZONTAL);
				Cancella listener = new Cancella(layout,linear);
				bottone_canc.setOnClickListener(listener);
				linear.addView(bottone);
				linear.addView(bottone_canc);
				ll.add(linear);
			}
			RelativeLayout caricamento = (RelativeLayout)mViewPager.findViewById(R.id.caricamento2);
			layout.removeView(caricamento);
			num = ll.size();
			for(int i=0;i<num;i++)
			{
				layout.addView(ll.get(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
