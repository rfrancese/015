package it.connessioni;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class AggiungiLavoro extends AsyncTask<String, String, String>{

	private static applicaton.lavoro_matic_test.AggiungiLavoro home;

	public AggiungiLavoro(applicaton.lavoro_matic_test.AggiungiLavoro h)
	{
		home=h;
	}

	protected String doInBackground(String... uri) {
		ConnectivityManager cm = (ConnectivityManager)home.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		while(!(netinfo!=null && netinfo.isConnected()))
		{
			netinfo = cm.getActiveNetworkInfo();
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try{
			HttpPost post = new HttpPost(uri[0]);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("idAzienda", uri[1]));
			nameValuePairs.add(new BasicNameValuePair("Nome", uri[2]));
			nameValuePairs.add(new BasicNameValuePair("Descrizione", uri[3]));
			nameValuePairs.add(new BasicNameValuePair("Indirizzo", uri[4]));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();

			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return responseString;
	}
	
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		home.lavoroAggiunto(result);
	}

}
