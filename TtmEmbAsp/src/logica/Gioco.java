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
	private static String encoding_topo="TtmEmbAsp/prova.dl";
	
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
			//PER ORA MUOVO SOLO IL TOPO
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			muovi_topo();
		}
	}
	
	public void muovi_topo()
	{
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
		
		InputProgram encoding=new ASPInputProgram();
		encoding.addFilesPath(encoding_topo);
		encoding.addProgram(getEncodings(encoding_topo));
		
		//PASSO TUTTO IL MALLOPPO ALL'HANDLER
		handler.addProgram(facts); 
		handler.addProgram(encoding);
		
		try {
			ASPMapper.getInstance().registerClass(Scelgo.class);
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
		
		System.out.println("La grandezza degli answer set e': " + answersets.getAnswersets().size());
		
		ArrayList<Scelgo> sca=new ArrayList<Scelgo>();
		
		for (AnswerSet a:answersets.getAnswersets())
		{
			try {
				for (Object o: a.getAtoms())
				{
					if (o instanceof Scelgo)
					{
						Scelgo sc=(Scelgo) o;
						System.out.println("scelgo("+sc.getX()+","+sc.getY()+")");
						sca.add(sc);
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		schema[topo.getX()][topo.getY()]=vuoto;
		topo.setX(sca.get(0).getX());
		topo.setY(sca.get(0).getY());
		schema[topo.getX()][topo.getY()]=mouse;
		System.out.println("Nuova posizione topo: " +"<"+topo.getX()+","+topo.getY()+">");
		gp.repaint();
	
		handler.removeProgram(encoding);
		handler.removeProgram(facts);
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
