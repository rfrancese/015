package it.interfacce;

import java.io.Serializable;



public class Utente implements Serializable{
	
	private static final long serialVersionUID=10L;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private int id,idAzienda;
	
	public int getIdAzienda() {
		return idAzienda;
	}


	public void setIdAzienda(int idAzienda) {
		this.idAzienda = idAzienda;
	}
	private boolean amministratore;
	
	
	public Utente(String n,String c,String e, String p,int i,boolean amm)
	{
		nome=n;
		cognome=c;
		email=e;
		password=p;
		id=i;
		amministratore=amm;
	}
	
	
	public String getNome()
	{
		return nome;
	}
	public boolean getAmministratore()
	{
		return amministratore;
	}
	public String getCognome()
	{
		return cognome;
	}
	
	public String getEmail()
	{
		return email;
	}
	public String getPassword()
	{
		return password;
	}
	public int getId()
	{
		return id;
	}
}
