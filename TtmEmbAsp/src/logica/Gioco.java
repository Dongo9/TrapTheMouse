package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import gui.GiocoPanel;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;

public class Gioco extends Thread{

	public int [][] schema;
	public int dim=9;
	private int vuoto=0,muro=1,mouse=2;
	private Topo topo;
	private ArrayList<Casella> caselle; //PER CONSERVARE TUTTI GLI OGGETTI CASELLA
	private ArrayList<Muro> muri; //PER CONSERVARE TUTTI GLI OGGETTI MURO
	private GiocoPanel gp;
	private boolean gioca=true;

	private static Handler handler=new DesktopHandler(new DLVDesktopService("TtmEmbAsp/dlv.mingw.exe")); //HANDLER PER PASSARE I PARAMETRI AL FILE
	private static String encoding_topo="TtmEmbAsp/IATopo.dl";
	
	public Gioco(GiocoPanel gp)
	{
		schema=new int[dim][dim];
		topo=new Topo(dim/2,dim/2);
		this.gp=gp;
		
		caselle=new ArrayList<Casella>();
		muri=new ArrayList<Muro>();
		
		initSchema();
	}
	
	private void initSchema() {
		// TODO Auto-generated method stub
		for (int i=0; i<dim; i++)
		{
			for (int j=0; j<dim; j++)
			{
				if (Math.random()<=0.09f)
				{
					schema[i][j]=muro;
					muri.add(new Muro(i,j));
				}
				
				else
				{
					schema[i][j]=vuoto;
				}
				
				caselle.add(new Casella(i,j));
			}
		}
		
		schema[topo.getX()][topo.getY()]=mouse;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(gioca)
		{
			ScelgoTopo move=muovi_topo();
			schema[topo.getX()][topo.getY()]=vuoto;
			topo.setX(move.getX());
			topo.setY(move.getY());
			schema[topo.getX()][topo.getY()]=mouse;
			gp.repaint();
		}
	}
	
	public ScelgoTopo muovi_topo()
	{
		handler.addOption(new OptionDescriptor("-filter=scelgoTopo "));
		handler.addOption(new OptionDescriptor("-n=1 "));
		
		//PASSO AL PROGRAMMA L'INPUT CHE HO RACCOLTO
		InputProgram facts=new ASPInputProgram();			
		
		for (Casella c: caselle)
		{
			try {
				facts.addObjectInput(c);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Muro m:muri)
		{
			try {
				facts.addObjectInput(m);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			facts.addObjectInput(topo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(facts.getPrograms().toString());
		
		InputProgram encoding=new ASPInputProgram();
		encoding.addFilesPath(encoding_topo);
		encoding.addProgram(getEncodings(encoding_topo));
		
		//PASSO TUTTO IL MALLOPPO ALL'HANDLER
		handler.addProgram(facts); 
		handler.addProgram(encoding);
		
		try {
			ASPMapper.getInstance().registerClass(ScelgoTopo.class);
		} catch (ObjectNotValidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//GENERO OUTPUT DAL DOCUMENTO
		Output output=handler.startSync();
		AnswerSets answersets=(AnswerSets) output;
		
		ScelgoTopo sc=null;
		
		for (AnswerSet a:answersets.getAnswersets())
		{
			try {
				for (Object o: a.getAtoms())
				{
					if (o instanceof ScelgoTopo)
					{
						sc=(ScelgoTopo) o;
						System.out.println("scelgo("+sc.getX()+","+sc.getY()+")");
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		handler.removeProgram(encoding);
		handler.removeProgram(facts);
		handler.removeOption(0);
		handler.removeOption(1);
	
		return sc;
	}

	private String getEncodings(String encoding_topo2) {
		BufferedReader reader;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(encoding_topo2));
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}
