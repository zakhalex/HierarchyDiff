package snomed;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class HierarchyRetrieval extends RecursiveAction
{
	private final SnomedConcept root;

	public HierarchyRetrieval(SnomedConcept root)
	{
		this.root = root;
	}

	@Override
	protected void compute()
	{
		ArrayList<SnomedConcept> hierarchy = getSnomedConceptChildren(root);
		List<HierarchyRetrieval> subtasks = new ArrayList<HierarchyRetrieval>(
				hierarchy.size());
		for ( SnomedConcept c : hierarchy )
		{
			subtasks.add(new HierarchyRetrieval(c));
		}
		if ( hierarchy.isEmpty() )
		{
			return;
		}
		else
		{
			invokeAll(subtasks);
			for ( SnomedConcept c : hierarchy )
			{
				root.addChildConcept(c);
			}

		}
	}

	public static ArrayList<SnomedConcept> getSnomedConceptChildren(
			SnomedConcept c )
	{
		System.out.println(Retrieval.increment_counter()
				+ " Getting children for: " + c);
		Retrieval.addItem(c);
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try
				{
					socket.close();
				}
				catch ( Exception e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Here we need to finish parsing html and get the children information
		return new ArrayList<SnomedConcept>();
	}

}
