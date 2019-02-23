package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	
	private MainFrame mf;
	private Image titolo,logo_topo,bottone_play;
	private JButton start;
	
	public MenuPanel(MainFrame mf)
	{
		this.mf=mf;
		this.setLayout(null);
		titolo=new ImageIcon("src/assets/titolo.png").getImage();
		logo_topo=new ImageIcon("src/assets/topotrappola.png").getImage();
		//bottone_play=new ImageIcon("src/assets/playbottone.png").getImage();
		
		start=new JButton();
		start.setBounds(80, 280, 300, 100);
		start.setIcon(new ImageIcon("src/assets/start.png"));
		start.setBorderPainted(false);
		start.setFocusPainted(false);
		start.setContentAreaFilled(false);
		start.setVisible(true);
		

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				GiocoPanel gp=new GiocoPanel(mf);
				gp.setFocusable(true);
				gp.requestFocus();
				mf.setContentPane(gp);
				mf.revalidate();
				gp.setVisible(true);
			}
			
		});
		
		this.add(start);
		
		
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		
		this.setBackground(new Color(173,255,47));
		
		arg0.drawImage(titolo, 140, 25,null);
		arg0.drawImage(logo_topo,630,mf.getHeight()/4+80,null);
	}

}
