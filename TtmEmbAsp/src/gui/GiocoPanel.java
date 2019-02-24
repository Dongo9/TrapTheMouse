package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import logica.Gioco;

public class GiocoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private MainFrame mf;
	private Image terreno,muro,topo;
	private Gioco gioco;
	
	public GiocoPanel(MainFrame mf)
	{
		this.mf=mf;
		this.setLayout(null);
		gioco=new Gioco(this);
		terreno=new ImageIcon("src/assets/background.png").getImage();
		muro=new ImageIcon("src/assets/wall.png").getImage();
		topo=new ImageIcon("src/assets/mouse.png").getImage();
		gioco.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		this.setBackground(new Color(173,255,47));
		
		for (int i=0; i<gioco.dim; i++)
		{
			for (int j=0; j<gioco.dim; j++)
			{
				if (gioco.schema[i][j]==0) {
					
						if (i%2==0)
						{
							g.drawImage(terreno, 105 + j * 58, 55 + i * 53, null);
						}
						
						else
						{
							g.drawImage(terreno, 135 + j * 58, 55 + i * 53, null);
						}
				}
				
				else if (gioco.schema[i][j]==1) {
					
					if (i%2==0)
					{
						g.drawImage(muro, 105 + j * 58,
								15 + i * 53, null);
					}
					
					else
					{
						g.drawImage(muro, 135 + j * 58,
								15 + i * 53, null);
					}
					
				}
				
				else if (gioco.schema[i][j]==2) {
					
					if (i%2==0)
					{
						g.drawImage(topo, 105 + j * 58,
								55 + i * 53, null);
					}
					
					else
					{
						g.drawImage(topo, 135 + j * 58,
								55 + i * 53, null);
					}
					
				}
			}
		}
	}
	
}
