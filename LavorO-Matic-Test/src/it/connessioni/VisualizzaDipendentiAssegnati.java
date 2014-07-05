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
import android.widget.Toast;

public class VisualizzaDipendentiAssegnati extends AsyncTask<String, String, String> {

	private static applicaton.lavoro_matic_test.VisualizzaDipendentiAssegnati home;
	
	public VisualizzaDipendentiAssegnati(applicaton.lavoro_matic_test.VisualizzaDipendentiAssegnati home)
	{
		this.home=home;
	}
	
	
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {

			HttpPost post = new HttpPost(uri[0]);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("idAzienda", uri[1]));
			nameValuePairs.add(new BasicNameValuePair("idLavoro", uri[2]));
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
			Toast.makeText(home.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(home.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		return responseString;
	}
	
	public void onPostExecute(String result)
	{
		super.onPostExecute(result);
		home.caricaLista(result);
	}

}
