package it.interfacce;

import java.io.Serializable;

public class Lavoro implements Serializable {
	
	private static final long serialVersionUID=12L;
	private int percentuale;
	private String nome;
	private String descrizione;
	private int id;
	
	public Lavoro(int i,int p, String n, String d)
	{
		id = i;
		percentuale=p;
		nome=n;
		descrizione=d;
	}
	
	public int getPercentuale()
	{
		return percentuale;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public String getDescrizione()
	{
		return descrizione;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void addPercentuale(int p)
	{
		percentuale=p;
	}

}
