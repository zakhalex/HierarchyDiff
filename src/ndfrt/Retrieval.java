package ndfrt;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Retrieval
{
	private static NdfrtConcept hierarchyroot = new NdfrtConcept(
			"N0000000002", "Chemical ingredient", "INGREDIENT_KIND");
	private static HashSet<NdfrtConcept> set = new HashSet<NdfrtConcept>();
	private static int counter = 0;
	private static final char interruptor[] = { 0x0d, 0x0a, 0x0d, 0x0a };

	/**
	 * @param args
	 */
	public static NdfrtConcept parseOutNdfrtConcept( NdfrtConcept c, String html )
	{
		String id = html.substring(0);
		String name = html.substring(0);
		String kind = "";
		for ( int i = 0; i < html.length(); i++ )
		{

		}
		return new NdfrtConcept(c, id, name, kind);
	}

	public static ArrayList<NdfrtConcept> getNdfrtConceptChildren(
			NdfrtConcept c )
	{
		System.out.println((++counter) + " Getting children for: " + c);
		set.add(c);
		String hostname = "mor.nlm.nih.gov";
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
				socket.setSoTimeout(40000);
				// String formdata="METHOD=getAllConceptsByKind&PARAM1="+type+"&PARAM2=&PARAM3=&DISPLAY=Search";
				String formdata = "METHOD=getChildConcepts&PARAM1=" + c.getId()
						+ "&PARAM2=false&PARAM3=&DISPLAY=Search";
				writer = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				writer.println("POST /perl/ndfrt_api_demo.pl HTTP/1.0");
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
				if ( processed.indexOf("no values") != -1 )
				{
					return new ArrayList<NdfrtConcept>();
				}

				int start = processed.indexOf("<hr />");
				if ( start != -1 )
				{
					processed = processed.substring(start);
					start = processed.indexOf(">NUI<");
					if ( start == -1 )
					{
						return new ArrayList<NdfrtConcept>();
					}

					ArrayList<NdfrtConcept> childlist = new ArrayList<NdfrtConcept>();
					while ( start != -1 )
					{
						int end = processed.indexOf("/table", start);
						start = processed.indexOf("<td", start);
						String id = "";
						String name = "";
						String kind = "";
						while ( start < end )
						{
							if ( (processed.charAt(start) == '>')
									&& (processed.charAt(start + 1) != '<') )
							{
								break;
							}
							start++;
						}
						if ( start != end )// found
						{

							id = processed.substring(start + 1,
									processed.indexOf('<', start));

						}
						start = processed.indexOf("NAME", start);
						start = processed.indexOf("<td", start);
						while ( start < end )
						{
							if ( (processed.charAt(start) == '>')
									&& (processed.charAt(start + 1) != '<') )
							{
								break;
							}
							start++;
						}
						if ( start != end )// found
						{
							name = processed.substring(start + 1,
									processed.indexOf('<', start));
						}
						start = processed.indexOf("KIND", start);
						start = processed.indexOf("<td", start);
						while ( start < end )
						{
							if ( (processed.charAt(start) == '>')
									&& (processed.charAt(start + 1) != '<') )
							{
								break;
							}
							start++;
						}
						if ( start != end )// found
						{
							kind = processed.substring(start + 1,
									processed.indexOf('<', start));
						}
						if ( (!id.isEmpty()) && (!name.isEmpty())
								&& (!kind.isEmpty()) )
						{
							childlist.add(new NdfrtConcept(c, id, name, kind));
						}
						start = end;
						start = processed.indexOf(">NUI<", start);
					}

					return childlist;
				}
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
		return new ArrayList<NdfrtConcept>();
	}

	public static void getAllConceptsByKind( String kind ) throws Exception
	{
		// String type="THERAPEUTIC_CATEGORY_KIND";
		String type = kind;
		String file = "I:\\University\\" + type + ".txt";
		String hostname = "mor.nlm.nih.gov";
		int port = 80;
		Socket socket = new Socket(hostname, port);
		socket.setSoTimeout(40000);
		String formdata = "METHOD=getAllConceptsByKind&PARAM1=" + type
				+ "&PARAM2=&PARAM3=&DISPLAY=Search";
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		writer.println("POST /perl/ndfrt_api_demo.pl HTTP/1.0");
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

		InputStream reader = socket.getInputStream();

		byte[] buffer = new byte[102400];
		int bytesread;
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		while ( ((bytesread = reader.read(buffer)) > 0) )
		{
			buf.write(buffer, 0, bytesread);
		}
		buffer = buf.toByteArray();
		int i = 0;
		boolean interruptorFound = false;
		for ( ; i < buffer.length - interruptor.length; i++ )
		{// This block of code leaves us only the header chunks
			if ( buffer[i] == interruptor[0] )
			{
				int j = 1;
				for ( ; j < interruptor.length; j++ )
				{
					if ( buffer[i + j] != interruptor[j] )
					{
						break;
					}
				}
				if ( j >= interruptor.length )
				{// found
					interruptorFound = true;
					i += j;
					break;
				}
			}
		}
		if ( !interruptorFound )
		{
			i = 0;
		}
		FileOutputStream writer2 = new FileOutputStream(file);
		if ( buffer.length > 4096 )
		{
			for ( ; i < buffer.length - 4096; i += 4096 )
			{
				writer2.write(buffer, i, 4096);
			}

		}
		if ( i < buffer.length )
		{
			writer2.write(buffer, i, buffer.length - i);
		}
		writer2.flush();
		writer2.close();
		socket.close();
		writer.close();
		reader.close();
	}

	public static ArrayList<NdfrtConcept> getNdfrtHierarchyFromConcept(
			NdfrtConcept root ) throws Exception
	{
		ArrayList<NdfrtConcept> hierarchy = getNdfrtConceptChildren(root);
		if ( hierarchy.isEmpty() )
		{
			return hierarchy;
		}
		else
		{
			for ( NdfrtConcept c : hierarchy )
			{
				for ( NdfrtConcept k : getNdfrtHierarchyFromConcept(c) )
				{
					c.addChildConcept(k);
				}
			}
		}
		return hierarchy;
	}

	public static void getNdfrtHierarchy( NdfrtConcept root ) throws Exception
	{
		for ( NdfrtConcept c : getNdfrtHierarchyFromConcept(root) )
		{
			root.addChildConcept(c);
		}
	}

	public static void printHierarchy( String filename, NdfrtConcept concept )
			throws Exception
	{
		PrintWriter pw = new PrintWriter(filename);

		pw.write(printLevelOrder(concept));
		pw.write("\r\n");
		pw.close();
	}

	public static void printHierarchyWithTrace( String filename,
			NdfrtConcept concept ) throws Exception
	{
		PrintWriter pw = new PrintWriter(filename);

		pw.write(printLevelOrderWithTrace(concept));
		pw.write("\r\n");
		pw.close();
	}

	public static int height( NdfrtConcept concept )
	{
		if ( concept == null )
		{
			return 0;
		}
		if ( concept.getChildren().isEmpty() )
			return 1;
		else
		{
			int lheight = 0;
			for ( NdfrtConcept c : concept.getChildren() )
			{
				lheight = Math.max(lheight, height(c));
			}
			/* compute the height of each subtree */
			return lheight + 1;
		}
	}

	/* Function to print level order traversal a tree */
	public static String printLevelOrder( NdfrtConcept concept )
	{
		StringBuilder buildstr = new StringBuilder();
		int h = height(concept);
		int i;
		for ( i = 1; i <= h; i++ )
			buildstr.append(printGivenLevel(concept, i) + "\r\n");
		return buildstr.toString();
	}

	/* Function to print level order traversal a tree */
	public static String printLevelOrderWithTrace( NdfrtConcept concept )
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
	public static String printGivenLevel( NdfrtConcept concept, int level )
	{
		StringBuilder buildstr = new StringBuilder();

		if ( level == 1 )
			return concept.toString() + "|";
		else if ( level > 1 )
		{
			if ( concept.getChildren().isEmpty() )
				return "";
			for ( NdfrtConcept c : concept.getChildren() )
			{
				buildstr.append(printGivenLevel(c, level - 1));
			}
		}
		return buildstr.toString();
	}

	public static ArrayList<NdfrtConcept> returnGivenLevel(
			NdfrtConcept concept, int level )
	{
		ArrayList<NdfrtConcept> buildstr = new ArrayList<NdfrtConcept>();

		if ( level == 1 )
		{
			buildstr.add(concept);
			return buildstr;
		}
		else if ( level > 1 )
		{
			if ( concept.getChildren().isEmpty() )
				return buildstr;
			for ( NdfrtConcept c : concept.getChildren() )
			{
				buildstr.addAll(returnGivenLevel(c, level - 1));
			}
		}
		return buildstr;
	}

	/* Print nodes at a given level */
	public static String printGivenLevelWithTrace( NdfrtConcept concept,
			int level, String trace )
	{
		StringBuilder buildstr = new StringBuilder();
		if ( level == 1 )
			return trace + "|";
		else if ( level > 1 )
		{
			if ( concept.getChildren().isEmpty() )
				return "";
			for ( NdfrtConcept c : concept.getChildren() )
			{
				buildstr.append(printGivenLevelWithTrace(c, level - 1, trace
						+ "==>" + c));
			}
		}
		return buildstr.toString();
	}

	public static void main( String[] args ) throws Exception
	{
		System.out.println(System.currentTimeMillis());
		if ( args.length < 2 )
		{
			return;
		}
		getNdfrtHierarchy(hierarchyroot);
		System.out.println("Done");
		printHierarchy(args[0], hierarchyroot);
		printHierarchyWithTrace(args[1], hierarchyroot);
		System.out.println(set.size());
		System.out.println(System.currentTimeMillis());
	}

}
