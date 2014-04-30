package it.interfacce;



public interface IClient{
	
	public void aggiuntoNuovoLavoro(Lavoro nuovo);
	public void aggiuntoNuovoUtente(Utente nuovo);
	public void rimossoUtente(int idUtenteRimosso);
	public void rimossoLavoro(int idLavoroRimosso);
	public void assegnaLavoro(int idLavoro, Utente[] utenti);
	public void revocaLavoro(int idLavoro, Utente[] utenti);
	public void aggiornaLavoro(int idLavoro,String aggiornamento);
	

}
