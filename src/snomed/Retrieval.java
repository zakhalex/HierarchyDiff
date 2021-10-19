package snomed;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

public class Retrieval
{
	private static HashSet<SnomedConcept> set = new HashSet<SnomedConcept>();
	private static volatile int counter = 0;

	public static ArrayList<SnomedConcept> getSnomedConceptChildren(
			SnomedConcept c )
	{
		System.out.println((++counter) + " Getting children for: " + c);
		set.add(c);
		String hostname = "vtsl.vetmed.vt.edu";
		int port = 80;
		Socket socket = null;
		PrintWriter writer = null;
		InputStream reader = null;
		String processed = "";
		boolean exceptioncontrol = false;
		while ( !exceptioncontrol )
		{
			exceptioncontrol = true;
			try
			{
				socket = new Socket(hostname, port);
				socket.setSoTimeout(40000);// http://vtsl.vetmed.vt.edu/TerminologyMgt/RF2Browser/isa.cfm
				// String formdata="METHOD=getAllConceptsByKind&PARAM1="+type+"&PARAM2=&PARAM3=&DISPLAY=Search";
				String formdata = "sctid=" + c.getId();
				writer = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				writer.println("POST /TerminologyMgt/RF2Browser/isa.cfm HTTP/1.0");
				writer.println("Host: " + hostname);
				writer.println("Accept-Language: en-US");
				writer.println("Pragma: no-cache");
				writer.println("Cache-Control: no-cache");
				writer.println("Accept: */*");
				writer.println("Content-Type: application/x-www-form-urlencoded");
				writer.println("User-Agent: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; InfoPath.1; .NET4.0C; .NET4.0E)"); // Be
																																																				// honest.
				writer.println("Content-Length: " + formdata.length());
				writer.println(""); // Important, else the server will expect that there's more into the request.
				writer.println(formdata);
				writer.flush();

				reader = socket.getInputStream();

				byte[] buffer = new byte[102400];
				int bytesread;
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				while ( ((bytesread = reader.read(buffer)) > 0) )
				{
					buf.write(buffer, 0, bytesread);
				}
				socket.close();
				writer.close();
				reader.close();
				processed = buf.toString();
				int start = processed.indexOf("Select a child ");

				int end = processed.indexOf("/table", start);
				start = processed.indexOf("<tr>", start);
				ArrayList<SnomedConcept> childlist = new ArrayList<SnomedConcept>();
				if ( (start > end) || (start == -1) )
				{
					return childlist;// no children
				}
				processed = processed.substring(start, end);
				start = processed.indexOf("href");
				if ( start == -1 )
				{
					return childlist;// no children
				}
				while ( start != -1 )
				{
					end = processed.indexOf("/td", start);
					start = processed.indexOf("ConceptID", start);
					if ( start == -1 )
					{
						throw new Exception("No concept ID");
					}
					start += 10;
					String id = processed.substring(start,
							processed.indexOf('"', start));
					start = processed.indexOf('>', start) + 1;

					String name = processed.substring(start,
							processed.indexOf('<', start));
					;

					if ( (!id.isEmpty()) && (!name.isEmpty()) )
					{
						childlist.add(new SnomedConcept(c, id, name));
					}
					start = end;
					start = processed.indexOf("href", start);
				}

				return childlist;

			}
			catch ( Exception e )
			{
				exceptioncontrol = false;
				e.printStackTrace();
				System.out.println("Buffer: " + processed);
			}
			finally
			{
				try
				{
					writer.close();
				}
				catch ( Exception e1 )
				{
					e1.printStackTrace();
				}
				try
				{
					reader.close();
				}
				catch ( Exception e1 )
				{
					e1.printStackTrace();
				}
				try
				{
					socket.close();
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
		// Here we need to finish parsing html and get the children information
		return new ArrayList<SnomedConcept>();
	}

	public static void getSnomedHierarchyFromConcept( SnomedConcept root )
			throws Exception
	{
		ArrayList<SnomedConcept> hierarchy = getSnomedConceptChildren(root);
		if ( hierarchy.isEmpty() )
		{
			return;
		}
		else
		{
			for ( SnomedConcept c : hierarchy )
			{
				getSnomedHierarchyFromConcept(c);
				root.addChildConcept(c);
			}
		}
	}

	/**
	 * @param args
	 */

	public synchronized static int increment_counter()
	{
		++counter;
		return counter;
	}

	public synchronized static void addItem( SnomedConcept c )
	{
		set.add(c);
	}

	public static void printHierarchy( String filename, SnomedConcept concept )
			throws Exception
	{
		/*
		 * PrintWriter pw=new PrintWriter(filename);
		 * 
		 * pw.write(printLevelOrder(concept)); pw.write("\r\n"); pw.close();
		 */
		printLevelOrderToFile(filename, concept);
	}

	public static void printHierarchyWithTrace( String filename,
			SnomedConcept concept ) throws Exception
	{
		// PrintWriter pw=new PrintWriter(filename);
		//
		// pw.write(printLevelOrderWithTrace(concept));
		// pw.write("\r\n");
		// pw.close();
		printLevelOrderWithTraceToFile(filename, concept);
	}

	/* Function to print level order traversal a tree */
	public static void printLevelOrderToFile( String filename,
			SnomedConcept concept ) throws Exception
	{
		int h = height(concept);
		int i;
		PrintWriter pw = new PrintWriter(filename);
		for ( i = 1; i <= h; i++ )
			pw.write(printGivenLevel(concept, i) + "\r\n");
		pw.close();
	}

	/* Function to print level order traversal a tree */
	public static void printLevelOrderWithTraceToFile( String filename,
			SnomedConcept concept ) throws Exception
	{
		int h = height(concept);
		int i;
		PrintWriter pw = new PrintWriter(filename);
		for ( i = 1; i <= h; i++ )
			pw.write(printGivenLevelWithTrace(concept, i, concept.toString())
					+ "\r\n");
		pw.close();
	}

	public static int height( SnomedConcept concept )
	{
		if ( concept == null )
		{
			return 0;
		}
		if ( concept.getChildren().isEmpty() )
		{
			return 1;
		}
		else
		{
			int lheight = 0;
			for ( SnomedConcept c : concept.getChildren() )
			{
				lheight = Math.max(lheight, height(c));
			}
			/* compute the height of each subtree */
			return lheight + 1;
		}
	}

	/* Function to print level order traversal a tree */
	public static String printLevelOrder( SnomedConcept concept )
	{
		StringBuilder buildstr = new StringBuilder();
		int h = height(concept);
		int i;
		for ( i = 1; i <= h; i++ )
			buildstr.append(printGivenLevel(concept, i) + "\r\n");
		return buildstr.toString();
	}

	/* Function to print level order traversal a tree */
	public static String printLevelOrderWithTrace( SnomedConcept concept )
	{
		StringBuilder buildstr = new StringBuilder();
		int h = height(concept);
		int i;
		for ( i = 1; i <= h; i++ )
			buildstr.append(printGivenLevelWithTrace(concept, i,
					concept.toString())
					+ "\r\n");
		return buildstr.toString();
	}

	/* Print nodes at a given level */
	public static String printGivenLevel( SnomedConcept concept, int level )
	{
		StringBuilder buildstr = new StringBuilder();
		if ( concept.getChildren().isEmpty() )
			return concept + "|";
		if ( level == 1 )
			return concept + "|";
		else if ( level > 1 )
		{
			for ( SnomedConcept c : concept.getChildren() )
			{
				buildstr.append(printGivenLevel(c, level - 1));
			}
		}
		return buildstr.toString();
	}

	/* Print nodes at a given level */
	public static String printGivenLevelWithTrace( SnomedConcept concept,
			int level, String trace )
	{
		StringBuilder buildstr = new StringBuilder();
		if ( level == 1 )
			return trace + "|";
		else if ( level > 1 )
		{
			if ( concept.getChildren().isEmpty() )
				return "";
			for ( SnomedConcept c : concept.getChildren() )
			{
				buildstr.append(printGivenLevelWithTrace(c, level - 1, trace
						+ "==>" + c));
			}
		}
		return buildstr.toString();
	}

	public static ArrayList<SnomedConcept> returnGivenLevel(
			SnomedConcept concept, int level )
	{
		ArrayList<SnomedConcept> buildstr = new ArrayList<SnomedConcept>();

		if ( level == 1 )
		{
			buildstr.add(concept);
			return buildstr;
		}
		else if ( level > 1 )
		{
			if ( concept.getChildren().isEmpty() )
				return buildstr;
			for ( SnomedConcept c : concept.getChildren() )
			{
				buildstr.addAll(returnGivenLevel(c, level - 1));
			}
		}
		return buildstr;
	}

	public static void main( String[] args ) throws Exception
	{
		System.out.println(System.currentTimeMillis());
		if ( args.length < 2 )
		{
			return;
		}
		SnomedConcept hierarchyroot = new SnomedConcept("373873005",
				"Pharmaceutical / biologic product (product)");
		ForkJoinPool p = new ForkJoinPool(8);// doing it multithreaded significantly decreases the time, required to load the information
		p.invoke(new HierarchyRetrieval(hierarchyroot));
		System.out.println("Done");
		printHierarchy(args[0], hierarchyroot);
		printHierarchyWithTrace(args[1], hierarchyroot);
		System.out.println(set.size());
		System.out.println(System.currentTimeMillis());
	}

}
