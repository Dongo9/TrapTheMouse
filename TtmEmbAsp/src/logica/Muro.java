package logica;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("muro")
public class Muro{

	@Param(0)
	private int x;
	@Param(1)
	private int y;
	

	public Muro(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Muro() {
		super();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
