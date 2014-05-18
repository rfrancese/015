package it.interfacce;

import java.io.Serializable;
import java.util.ArrayList;

public class Azienda implements Serializable {
	
	private static final long serialVersionUID=15L;
	private String nome;
	private ArrayList<Utente> impiegati = new ArrayList<Utente>();
	private ArrayList<Lavoro> lavori = new ArrayList<Lavoro>();
	private int id;
	
	public Azienda(String n,int i)
	{
		nome = n;
		id=i;
	}
	
	public void caricaImpiegato(Utente x)
	{
		impiegati.add(x);
	}
	
	public void caricaLavoro(Lavoro x)
	{
		lavori.add(x);
	}
	public String getNome()
	{
		return nome;
	}
	public int getId()
	{
		return id;
	}
	
	public Utente cercaImpiegato(int x)
	{
		for(int i=0;i<impiegati.size();i++)
		{
			if(impiegati.get(i).getId()==x)
				return impiegati.get(i);
		}
		return null;
	}
	
	public ArrayList<Lavoro> getLavoro()
	{
		return lavori;
	}
	public ArrayList<Utente> getImpiegati()
	{
		return impiegati;
	}
	
	public void rimuoviImpiegato(int x)
	{
		Utente temp= cercaImpiegato(x);
		if(temp!=null)
		{
			impiegati.remove(temp);
		}
		else
			System.out.println("Utente non trovato!");
	}

}
