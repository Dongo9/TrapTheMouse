package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	
	private MenuPanel mp;
	
	public MainFrame()
	{
		this.setTitle("TRAP THE MOUSE");
		this.setPreferredSize(new Dimension(900,700));
		this.setLayout(null);
		mp=new MenuPanel(this);
		this.setContentPane(mp);
		this.setVisible(true);
		Dimension dimensione_schermo=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) dimensione_schermo.getWidth()/4,(int) dimensione_schermo.getHeight()/4-120);
		this.pack();
		this.revalidate();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String args[])
	{
		MainFrame mf=new MainFrame();
		
	}

}
