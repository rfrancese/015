package it.interfacce;

import java.io.Serializable;

public class Impiegato extends Utente implements Serializable{

	
	private static final long serialVersionUID = 13L;
	private String ruolo;
	public Impiegato(String n, String c, String e, String p,int x,int ia,String r) {
		super(n, c, e, p,x,ia,false);
		ruolo = r;
		
	}
	
	public String getRuolo()
	{
		return ruolo;
	}

}
