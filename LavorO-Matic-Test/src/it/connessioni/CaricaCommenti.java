package it.connessioni;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import applicaton.lavoro_matic_test.GestioneCommenti;

public class CaricaCommenti extends AsyncTask<String, String, String> {

	private static GestioneCommenti home;

	public CaricaCommenti(GestioneCommenti home)
	{
		this.home=home;
	}

	@Override
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {

			HttpPost post = new HttpPost(uri[0]);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("idLavoro", uri[1]));
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
		} catch (ClientProtocolException e) {
			//TODO Handle problems..
		} catch (IOException e) {
			//TODO Handle problems..
		}

		return responseString;
	}

	public void onPostExecute(String result)
	{
		super.onPostExecute(result);

		home.caricaCommenti(result);
	}

}
