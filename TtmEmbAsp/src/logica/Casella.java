package logica;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("casella")
public class Casella {

	@Param(0)
	private int riga;
	@Param(1)
	private int colonna;
	
	public Casella() {
		super();
	}
	
	public Casella(int riga,int colonna)
	{
		super();
		this.riga=riga;
		this.colonna=colonna;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public int getColonna() {
		return colonna;
	}

	public void setColonna(int colonna) {
		this.colonna = colonna;
	}
	
}
