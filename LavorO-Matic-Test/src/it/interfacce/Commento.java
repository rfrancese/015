package it.interfacce;

public class Commento {

	
	private int idLavoro,idAggiornamento;


	private String autore,testo;


	public Commento(int idLavoro, int idAggiornamento, String autore,
			String testo) {
		
		this.idLavoro = idLavoro;
		this.idAggiornamento = idAggiornamento;
		this.autore = autore;
		this.testo = testo;
	}

	public int getIdAggiornamento() {
		return idAggiornamento;
	}

	public void setIdAggiornamento(int idAggiornamento) {
		this.idAggiornamento = idAggiornamento;
	}
	public int getIdLavoro() {
		return idLavoro;
	}
	public void setIdLavoro(int idLavoro) {
		this.idLavoro = idLavoro;
	}
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
}
