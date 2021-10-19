package compare;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import GUI.GraphDisplay;

import snomed.SnomedConcept;

import ndfrt.NdfrtConcept;

public class Comparator
{
	public static NdfrtConcept rootndfrt = null;
	public static ArrayList<NdfrtConcept> ndfrtset = new ArrayList<NdfrtConcept>();
	public static SnomedConcept rootsnomed = null;
	public static ArrayList<SnomedConcept> snomedset = new ArrayList<SnomedConcept>();
	private static String filename = "output.txt";

	public static String getFilename()
	{
		return filename;
	}

	public static void setFilename( String filename )
	{
		Comparator.filename = filename;
	}

	public static void loadNdfRtHierarchy( String filename ) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String str;
		while ( (str = in.readLine()) != null )
		{
			if ( !str.contains("|") )
			{
				continue;
			}
			String local[] = str.split("\\|");
			for ( String s : local )
			{
				String localconcept[] = s.split("==>");
				NdfrtConcept focusconcept = rootndfrt;
				for ( String conceptTrace : localconcept )
				{

					String id_name[] = conceptTrace.split("~_~");
					if ( rootndfrt == null )
					{
						rootndfrt = new NdfrtConcept(id_name[0], id_name[1],
								"INGREDIENT_KIND");
						focusconcept = rootndfrt;
						ndfrtset.add(rootndfrt);
						continue;
					}
					if ( (id_name[0].equals(focusconcept.getId()))
							&& (id_name[1].equals(focusconcept.getName())) )
					{
						continue;
					}
					NdfrtConcept localndfrtconcept = ndfrtset.get(ndfrtset
							.indexOf(focusconcept));

					NdfrtConcept c = new NdfrtConcept(localndfrtconcept,
							id_name[0], id_name[1], "INGREDIENT_KIND");
					if ( !ndfrtset.contains(c) )
					{
						ndfrtset.add(c);
					}
					if ( !localndfrtconcept.getChildren().contains(c) )
					{
						localndfrtconcept.addChildConcept(c);
					}
					focusconcept = c;
				}
			}
		}
		in.close();
		System.out.println("Loaded NDF-RT");
	}

	public static void loadSnomedHierarchy( String filename ) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String str;
		while ( (str = in.readLine()) != null )
		{
			if ( !str.contains("|") )
			{
				continue;
			}
			String local[] = str.split("\\|");
			for ( String s : local )
			{
				String localconcept[] = s.split("==>");
				SnomedConcept focusconcept = rootsnomed;
				for ( String conceptTrace : localconcept )
				{

					String id_name[] = conceptTrace.split("~_~");
					if ( rootsnomed == null )
					{
						rootsnomed = new SnomedConcept(id_name[0], id_name[1]);
						focusconcept = rootsnomed;
						snomedset.add(rootsnomed);
						continue;
					}
					if ( (id_name[0].equals(focusconcept.getId()))
							&& (id_name[1].equals(focusconcept.getName())) )
					{
						continue;
					}
					SnomedConcept localsnomedconcept = snomedset.get(snomedset
							.indexOf(focusconcept));

					SnomedConcept c = new SnomedConcept(localsnomedconcept,
							id_name[0], id_name[1]);
					if ( !snomedset.contains(c) )
					{
						snomedset.add(c);
					}
					if ( !localsnomedconcept.getChildren().contains(c) )
					{
						localsnomedconcept.addChildConcept(c);
					}
					focusconcept = c;
				}
			}
		}
		in.close();
		System.out.println("Loaded SNOMED-CT, " + snomedset.size()
				+ " concepts loaded");
	}

	/**
	 * @param args
	 */
	public static void main( String[] args ) throws Exception
	{
		ArrayList<String> params = new ArrayList<String>();
		params.add("ndfrtpath");
		params.add("ndfrtpathwithouttrace");
		params.add("snomedctpath");
		params.add("snomedctpathwithouttrace");
		params.add("resultoutputpath");
		params.add("ndfrtedgedisplay");
		params.add("snomedctedgedisplay");

		ArrayList<String> parameters = shared.Shared.loadVariablesFromFile(
				params, "config.ini");

		String ndfrtHierarchyPath = "hroottracendfrt.txt";
		String ndfrtHierarchyPathWithoutTrace = "hrootndfrt.txt";
		if ( !parameters.get(0).isEmpty() )
		{
			ndfrtHierarchyPath = parameters.get(0);
		}
		if ( !parameters.get(1).isEmpty() )
		{
			ndfrtHierarchyPathWithoutTrace = parameters.get(1);
		}
		File checkfile = new File(ndfrtHierarchyPath);
		if ( !checkfile.exists() )
		{
			System.out.println(checkfile.getAbsolutePath());
			String[] retrievalparams = { ndfrtHierarchyPathWithoutTrace,
					ndfrtHierarchyPath };
			ndfrt.Retrieval.main(retrievalparams);
		}

		String snomedHierarchyPath = "hroottracesnomed.txt";
		String snomedHierarchyPathWithoutTrace = "hrootsnomed.txt";
		if ( !parameters.get(2).isEmpty() )
		{
			snomedHierarchyPath = parameters.get(2);
		}
		if ( !parameters.get(3).isEmpty() )
		{
			snomedHierarchyPathWithoutTrace = parameters.get(3);
		}
		checkfile = new File(snomedHierarchyPath);

		if ( !checkfile.exists() )
		{
			System.out.println(checkfile.getAbsolutePath());
			String[] retrievalparams = { snomedHierarchyPathWithoutTrace,
					snomedHierarchyPath };
			snomed.Retrieval.main(retrievalparams);
		}
		if ( !parameters.get(4).isEmpty() )
		{
			filename = parameters.get(4);
		}
		boolean ndfrtvisible = true;
		if ( !parameters.get(5).isEmpty() )
		{
			try
			{
				ndfrtvisible = Boolean.parseBoolean(parameters.get(5));
			}
			catch ( Exception e )
			{

			}
		}
		boolean snomedvisible = true;
		if ( !parameters.get(6).isEmpty() )
		{
			try
			{
				snomedvisible = Boolean.parseBoolean(parameters.get(6));
			}
			catch ( Exception e )
			{

			}
		}
		System.out.println(System.currentTimeMillis());
		loadNdfRtHierarchy(ndfrtHierarchyPath);
		loadSnomedHierarchy(snomedHierarchyPath);

		final JFrame frame = new JFrame();
		frame.setTitle("NDF-RT/SNOMED comparision tool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setBackground(Color.decode("#FAFAFA"));
		final GraphDisplay panel = new GraphDisplay(Color.black, Color.orange,
				Color.decode("#000080"));
		panel.addAllNodes(rootsnomed, false, snomedvisible);
		final GraphDisplay panel_snomed = new GraphDisplay(Color.black,
				Color.orange, Color.decode("#000080"));
		panel_snomed.addAllNodes(rootsnomed, false, snomedvisible);
		final GraphDisplay panel_ndfrt = new GraphDisplay(Color.black,
				Color.orange, Color.decode("#000080"));
		panel.addAllNodes(rootndfrt, true, ndfrtvisible);
		panel_ndfrt.addAllNodes(rootndfrt, false, ndfrtvisible);
		panel.calculate();
		System.out.println("Creating view");
		JTabbedPane jtab = new JTabbedPane();
		JScrollPane scroller = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane scroller2 = new JScrollPane(panel_snomed,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.getViewport().putClientProperty("EnableWindowBlit",
				Boolean.TRUE);
		scroller2.getViewport().putClientProperty("EnableWindowBlit",
				Boolean.TRUE);
		jtab.addTab("Combined View", scroller);
		jtab.addTab("SNOMED-CT", scroller2);
		jtab.addTab("NDF-RT", new JScrollPane(panel_ndfrt,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

		frame.getContentPane().add(jtab);
		frame.getRootPane().addComponentListener(new ComponentAdapter()
		{
			public void componentResized( ComponentEvent evt )
			{
				panel.setWidth(frame.getWidth());
				panel.setHeight(frame.getHeight());
				panel_ndfrt.setWidth(frame.getWidth());
				panel_ndfrt.setHeight(frame.getHeight());
				panel_snomed.setWidth(frame.getWidth());
				panel_snomed.setHeight(frame.getHeight());
			}
		});
		frame.setVisible(true);
		System.out.println("done " + System.currentTimeMillis());
	}

}
