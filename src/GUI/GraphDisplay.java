package GUI;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;

import javax.swing.*;

import shared.Logger;
import snomed.SnomedConcept;

import ndfrt.NdfrtConcept;

@SuppressWarnings("serial")
public class GraphDisplay extends JPanel
{
	private Color maincolor;
	private Color backgroundcolor;
	private Color textcolor;
	private int analysislevel = 1;
	private int width;
	private int height;
	private HashMap<NdfrtConcept, Integer> ndfrtconceptmap = new HashMap<NdfrtConcept, Integer>();
	private HashMap<SnomedConcept, Integer> snomedconceptmap = new HashMap<SnomedConcept, Integer>();
	private ArrayList<Node> nodes;
	private ArrayList<NdfrtConcept> ndfrtlist2 = new ArrayList<NdfrtConcept>();
	private ArrayList<SnomedConcept> snomedlist2 = new ArrayList<SnomedConcept>();
	private HashMap<NdfrtConcept, SnomedConcept> correspondence = new HashMap<NdfrtConcept, SnomedConcept>();
	private HashMap<Integer, ArrayList<Integer>> levelnodecorrespondance = new HashMap<Integer, ArrayList<Integer>>();
	private HashSet<Edge> edges;
	private final Logger logger = new Logger(compare.Comparator.getFilename());

	public GraphDisplay()
	{ // Constructor
		nodes = new ArrayList<Node>();
		edges = new HashSet<Edge>();
		width = 640;
		height = 480;
		maincolor = Color.black;
		backgroundcolor = Color.white;
		textcolor = Color.black;
	}

	public GraphDisplay(GraphDisplay old)
	{ // Copy Constructor
		nodes = new ArrayList<Node>();
		nodes.addAll(old.getNodes());
		edges = new HashSet<Edge>();
		edges.addAll(old.getEdges());
		width = old.getWidth();
		height = old.getHeight();
		maincolor = old.getMaincolor();
		backgroundcolor = old.getBackgroundcolor();
		textcolor = old.getTextcolor();
	}

	public GraphDisplay(Color main, Color background, Color text)
	{ // Construct with label
		nodes = new ArrayList<Node>();
		edges = new HashSet<Edge>();
		width = 640;
		height = 480;
		maincolor = main;
		backgroundcolor = background;
		textcolor = text;
	}

	public Color getMaincolor()
	{
		return maincolor;
	}

	public void setMaincolor( Color maincolor )
	{
		this.maincolor = maincolor;
	}

	public Color getBackgroundcolor()
	{
		return backgroundcolor;
	}

	public void setBackgroundcolor( Color backgroundcolor )
	{
		this.backgroundcolor = backgroundcolor;
	}

	public Color getTextcolor()
	{
		return textcolor;
	}

	public void setTextcolor( Color textcolor )
	{
		this.textcolor = textcolor;
	}

	public int getAnalysislevel()
	{
		return analysislevel;
	}

	public void setAnalysislevel( int analysislevel )
	{
		this.analysislevel = analysislevel;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth( int width )
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight( int height )
	{
		this.height = height;
	}

	public HashMap<NdfrtConcept, Integer> getNdfrtconceptmap()
	{
		return ndfrtconceptmap;
	}

	public void setNdfrtconceptmap(
			HashMap<NdfrtConcept, Integer> ndfrtconceptmap )
	{
		this.ndfrtconceptmap = ndfrtconceptmap;
	}

	public HashMap<SnomedConcept, Integer> getSnomedconceptmap()
	{
		return snomedconceptmap;
	}

	public void setSnomedconceptmap(
			HashMap<SnomedConcept, Integer> snomedconceptmap )
	{
		this.snomedconceptmap = snomedconceptmap;
	}

	public ArrayList<Node> getNodes()
	{
		return nodes;
	}

	public void setNodes( ArrayList<Node> nodes )
	{
		this.nodes = nodes;
	}

	public ArrayList<NdfrtConcept> getNdfrtlist2()
	{
		return ndfrtlist2;
	}

	public void setNdfrtlist2( ArrayList<NdfrtConcept> ndfrtlist2 )
	{
		this.ndfrtlist2 = ndfrtlist2;
	}

	public ArrayList<SnomedConcept> getSnomedlist2()
	{
		return snomedlist2;
	}

	public void setSnomedlist2( ArrayList<SnomedConcept> snomedlist2 )
	{
		this.snomedlist2 = snomedlist2;
	}

	public HashMap<NdfrtConcept, SnomedConcept> getCorrespondence()
	{
		return correspondence;
	}

	public void setCorrespondence(
			HashMap<NdfrtConcept, SnomedConcept> correspondence )
	{
		this.correspondence = correspondence;
	}

	public HashSet<Edge> getEdges()
	{
		return edges;
	}

	public void setEdges( HashSet<Edge> Edges )
	{
		this.edges = Edges;
	}

	public class Node
	{
		int x, y;
		String name;
		String nametocompare;
		Color nodecolor;
		boolean ignore = false;

		public Node(String new_name, int new_x, int new_y)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = new_name;
			nodecolor = backgroundcolor;
		}

		/**
		 * Create a node that has displayed name different from the name that is used to compare it to other nodes.
		 * 
		 * @param new_name
		 *            - displayed name of the node
		 * @param newnametocompare
		 *            - name of the node that should be used to do the comparisons
		 * @param new_x
		 *            - x coordinate of the node
		 * @param new_y
		 *            - y coordinate of the node
		 */
		public Node(String new_name, String newnametocompare, int new_x,
				int new_y)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = newnametocompare;
			nodecolor = backgroundcolor;
		}

		public Node(String new_name, int new_x, int new_y, Color new_color)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = new_name;
			nodecolor = new_color;
		}

		public Node(String new_name, String newnametocompare, int new_x,
				int new_y, Color new_color)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = newnametocompare;
			nodecolor = new_color;
		}

		public Node(String new_name, int new_x, int new_y, boolean new_ignore)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = new_name;
			nodecolor = backgroundcolor;
			ignore = new_ignore;
		}

		public Node(String new_name, String newnametocompare, int new_x,
				int new_y, boolean new_ignore)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = newnametocompare;
			nodecolor = backgroundcolor;
			ignore = new_ignore;
		}

		public Node(String new_name, int new_x, int new_y, Color new_color,
				boolean new_ignore)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = new_name;
			nodecolor = new_color;
			ignore = new_ignore;
		}

		public Node(String new_name, String newnametocompare, int new_x,
				int new_y, Color new_color, boolean new_ignore)
		{
			x = new_x;
			y = new_y;
			name = new_name;
			nametocompare = newnametocompare;
			nodecolor = new_color;
			ignore = new_ignore;
		}

		public boolean isIgnored()
		{
			return ignore;
		}

		public void setIgnore( boolean ignore )
		{
			this.ignore = ignore;
		}

		public Color getNodecolor()
		{
			return nodecolor;
		}

		public void setNodecolor( Color nodecolor )
		{
			this.nodecolor = nodecolor;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + (ignore ? 1231 : 1237);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((nametocompare == null) ? 0 : nametocompare.hashCode());
			result = prime * result
					+ ((nodecolor == null) ? 0 : nodecolor.hashCode());
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			Node other = (Node) obj;
			if ( !getOuterType().equals(other.getOuterType()) )
				return false;
			if ( ignore != other.ignore )
				return false;
			if ( name == null )
			{
				if ( other.name != null )
					return false;
			}
			else if ( !name.equals(other.name) )
				return false;
			if ( nametocompare == null )
			{
				if ( other.nametocompare != null )
					return false;
			}
			else if ( !nametocompare.equals(other.nametocompare) )
				return false;
			if ( nodecolor == null )
			{
				if ( other.nodecolor != null )
					return false;
			}
			else if ( !nodecolor.equals(other.nodecolor) )
				return false;
			if ( x != other.x )
				return false;
			if ( y != other.y )
				return false;
			return true;
		}

		private GraphDisplay getOuterType()
		{
			return GraphDisplay.this;
		}

	}

	public class Edge
	{
		int i, j;
		boolean visible = true;

		public Edge(int ii, int jj)
		{
			i = ii;
			j = jj;
		}

		/**
		 * Creates a new Edge and allows a user to disable display of it on the image
		 * 
		 * @param ii
		 * @param jj
		 * @param visible
		 */
		public Edge(int ii, int jj, boolean visible)
		{
			i = ii;
			j = jj;
			this.visible = visible;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + i;
			result = prime * result + j;
			return result;
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			Edge other = (Edge) obj;
			if ( !getOuterType().equals(other.getOuterType()) )
				return false;
			if ( i != other.i )
				return false;
			if ( j != other.j )
				return false;
			return true;
		}

		private GraphDisplay getOuterType()
		{
			return GraphDisplay.this;
		}

	}

	public void addAllNodes( NdfrtConcept rootndfrt, boolean invisible,
			boolean edgevisible )
	{
		ndfrtlist2.clear();
		FontMetrics f = this.getFontMetrics(this.getFont());
		int h = ndfrt.Retrieval.height(rootndfrt);
		int i;
		int pos = -1;
		for ( i = 1; i <= h; i++ )
		{
			ArrayList<NdfrtConcept> ndfrtlist = ndfrt.Retrieval
					.returnGivenLevel(rootndfrt, i);
			pos = Math.max(pos, ndfrtlist.size());
		}
		if ( pos != -1 )
		{
			ndfrtconceptmap.clear();
			int formwidth = 0;
			int formheight = 0;
			for ( i = 1; i <= h; i++ )
			{
				ArrayList<NdfrtConcept> ndfrtlist = ndfrt.Retrieval
						.returnGivenLevel(rootndfrt, i);
				for ( int j = 0; j < ndfrtlist.size(); j++ )
				{
					NdfrtConcept c = ndfrtlist.get(j);
					Node n = new Node(
							c.getId() + " " + c.getName(),
							c.getName(),
							((int) (((double) j + 1)
									* (double) (((double) pos)
											/ ndfrtlist.size() + 1) * 200)) + 10,
							(i - 1) * (f.getHeight() + 100), invisible);
					if ( !ndfrtconceptmap.containsKey(ndfrtlist.get(j)) )
					{
						ndfrtlist2.add(c);
						nodes.add(n);
						width = Math.max(width, n.x + f.stringWidth(n.name));
						height = Math.max(height, n.y + f.getHeight());
						ndfrtconceptmap.put(ndfrtlist.get(j), nodes.size() - 1);

					}

					formwidth = Math
							.max(n.x + f.stringWidth(n.name), formwidth);
					formheight = Math.max(n.y + f.getHeight(), formheight);
				}
			}
			Iterator<NdfrtConcept> it = ndfrtconceptmap.keySet().iterator();
			while ( it.hasNext() )
			{
				NdfrtConcept local = it.next();
				int first_node = ndfrtconceptmap.get(local);
				for ( NdfrtConcept child : local.getChildren() )
				{
					try
					{

						edges.add(new Edge(first_node, ndfrtconceptmap
								.get(child), edgevisible));
					}
					catch ( Exception e )
					{
						logger.log("Unable to add the edge: " + local + "==>"
								+ local.getChildren());
					}
				}
			}
			logger.log("Current width of the form is: " + formwidth);
			Dimension d = this.getPreferredSize();
			if ( d != null )
			{
				this.setPreferredSize(new Dimension((int) Math.max(formwidth,
						d.getWidth()),
						(int) Math.max(formheight, d.getHeight())));
			}
			else
			{
				this.setPreferredSize(new Dimension(formwidth, formheight));
			}
			this.repaint();
		}
	}

	/**
	 * Loads all the SNOMED-CT nodes and adds them to the list of nodes displayed
	 * 
	 * @param rootsnomed
	 *            - root SNOMED-CT node
	 * @param invisible
	 *            - if set to true, the edges between the nodes will not be displayed
	 */
	public void addAllNodes( SnomedConcept rootsnomed, boolean invisible,
			boolean edgevisible )
	{
		snomedlist2.clear();
		FontMetrics f = this.getFontMetrics(this.getFont());
		int h = snomed.Retrieval.height(rootsnomed);
		int i;
		int pos = -1;
		int maxconceptlength = 1;
		for ( i = 1; i <= h; i++ )
		{
			ArrayList<SnomedConcept> snomedlist = snomed.Retrieval
					.returnGivenLevel(rootsnomed, i);
			for ( SnomedConcept s : snomedlist )
			{
				maxconceptlength = Math.max(maxconceptlength, s.getId()
						.length() + s.getName().length());
			}
			pos = Math.max(pos, snomedlist.size());
		}
		logger.log("The height of the concept tree is: " + h);
		if ( pos != -1 )
		{
			snomedconceptmap.clear();
			int formwidth = 0;
			int formheight = 0;
			for ( i = 1; i <= h; i++ )
			{
				ArrayList<SnomedConcept> snomedlist = snomed.Retrieval
						.returnGivenLevel(rootsnomed, i);
				for ( int j = 0; j < snomedlist.size(); j++ )
				{
					SnomedConcept c = snomedlist.get(j);
					Node n = new Node(
							c.getId() + " " + c.getName(),
							c.getName(),
							((int) (((double) j + 1)
									* (double) (((double) pos)
											/ snomedlist.size() + 1) * maxconceptlength)) + 10,
							(i - 1) * (f.getHeight() + 100));
					if ( !snomedconceptmap.containsKey(snomedlist.get(j)) )
					{
						snomedlist2.add(c);
						nodes.add(n);
						width = Math.max(width, n.x);
						height = Math.max(height, n.y);
						snomedconceptmap.put(snomedlist.get(j),
								nodes.size() - 1);
						if ( levelnodecorrespondance.containsKey(n.y) )
						{
							if ( !levelnodecorrespondance.get(n.y).contains(
									nodes.size() - 1) )
							{
								levelnodecorrespondance.get(n.y).add(
										nodes.size() - 1);
							}
						}
						else
						{
							ArrayList<Integer> locallist = new ArrayList<Integer>();
							locallist.add(nodes.size() - 1);
							levelnodecorrespondance.put(n.y, locallist);
						}
					}

					formwidth = Math
							.max(n.x + f.stringWidth(n.name), formwidth);
					formheight = Math.max(n.y + f.getHeight(), formheight);
				}
			}
			Iterator<SnomedConcept> it = snomedconceptmap.keySet().iterator();
			while ( it.hasNext() )
			{
				SnomedConcept local = it.next();
				int first_node = snomedconceptmap.get(local);
				for ( SnomedConcept child : local.getChildren() )
				{
					try
					{

						edges.add(new Edge(first_node, snomedconceptmap
								.get(child), edgevisible));
					}
					catch ( Exception e )
					{
						logger.log("Unable to draw the edge: " + local + "==>"
								+ local.getChildren());
					}
				}
			}
			logger.log("Current width of the form is: " + formwidth);
			Dimension d = this.getPreferredSize();
			if ( d != null )
			{
				this.setPreferredSize(new Dimension((int) Math.max(formwidth,
						d.getWidth()),
						(int) Math.max(formheight, d.getHeight())));
			}
			else
			{
				this.setPreferredSize(new Dimension(formwidth, formheight));
			}
			this.repaint();
		}
	}

	public void addNodes( ArrayList<Node> nodeslist )
	{
		// add a node at pixel (x,y)
		nodes.addAll(nodeslist);
		for ( Node n : nodeslist )
		{
			width = Math.max(width, n.x);
			height = Math.max(height, n.y);
		}
		this.repaint();
	}

	public void addNode( String name, int x, int y )
	{
		// add a node at pixel (x,y)
		nodes.add(new Node(name, x, y));
		this.repaint();
	}

	public void addEdges( ArrayList<Edge> edgelist )
	{
		// add an edge between nodes i and j
		edges.addAll(edgelist);
		this.repaint();
	}

	public void addEdge( int i, int j )
	{
		// add an edge between nodes i and j
		edges.add(new Edge(i, j));
		this.repaint();
	}

	public void removeNodes( ArrayList<Node> nodeslist )
	{
		// add a node at pixel (x,y)
		for ( Node n : nodeslist )
		{
			nodes.remove(n);
		}
		this.repaint();
	}

	public void removeNode( String name, int x, int y )
	{
		// add a node at pixel (x,y)
		nodes.remove(new Node(name, x, y));
		this.repaint();
	}

	public void removeEdges( ArrayList<Edge> edgelist )
	{
		// add an edge between nodes i and j
		for ( Edge n : edgelist )
		{
			edges.remove(n);
		}
		this.repaint();
	}

	public void removeEdge( int i, int j )
	{
		// add an edge between nodes i and j
		edges.remove(new Edge(i, j));
		this.repaint();
	}

	public void calculate()
	{
		HashMap<SnomedConcept, ArrayList<String>> snomedset = new HashMap<SnomedConcept, ArrayList<String>>();
		HashMap<NdfrtConcept, ArrayList<String>> ndfrtset = new HashMap<NdfrtConcept, ArrayList<String>>();

		logger.log("Number of concepts in\r\nsnomed: " + snomedlist2.size()
				+ "\r\nndfrt:" + ndfrtlist2.size() + "\r\n");
		for ( SnomedConcept c : snomedlist2 )
		{
			if ( (c != null) && (!c.getparentConcept().isEmpty()) )
			{
				for ( SnomedConcept p : c.getparentConcept() )
				{
					if ( snomedset.containsKey(p) )
					{
						ArrayList<String> local = snomedset.get(p);
						if ( !local.contains(c.getName()) )
						{
							local.add(c.getName());
							snomedset.put(p, local);
						}

					}
					else
					{
						ArrayList<String> arraylist = new ArrayList<String>();
						arraylist.add(c.getName());

						snomedset.put(p, arraylist);

					}
				}

			}
			for ( NdfrtConcept n : ndfrtlist2 )
			{
				if ( (n != null) && (!n.getparentConcept().isEmpty()) )
				{
					for ( NdfrtConcept p : n.getparentConcept() )
					{
						if ( ndfrtset.containsKey(p) )
						{
							ArrayList<String> local = ndfrtset.get(p);
							if ( !local.contains(n.getName()) )
							{
								local.add(n.getName());
								ndfrtset.put(p, local);
							}
						}
						else
						{
							ArrayList<String> arraylist = new ArrayList<String>();
							arraylist.add(n.getName());
							ndfrtset.put(p, arraylist);

						}

					}
					// snomedset.add(c.getparentConcept()+"==>"+c.getName());

				}
				String snoconcept = c.getName().toLowerCase();
				int location = snoconcept.lastIndexOf("(");
				if ( location != -1 )
				{
					snoconcept = snoconcept.substring(0, location).trim();
				}
				String ndfrtconcept = n.getName().toLowerCase();
				location = snoconcept.indexOf(ndfrtconcept);
				if ( location != -1 )
				{
					if ( location != 0 )
					{
						if ( (!Character.isAlphabetic(snoconcept
								.charAt(location - 1)))
								|| Character.isWhitespace(snoconcept
										.charAt(location - 1)) )
						{
							if ( ((double) ndfrtconcept.length())
									/ ((double) snoconcept.length()) >= 0.75 )
							{
								correspondence.put(n, c);
								if ( ((double) ndfrtconcept.length())
										/ ((double) snoconcept.length()) < 1 )
									logger.log("Partial match: " + c.getName()
											+ " " + n.getName());
								break;
							}
						}
					}
					else
					{
						if ( ((double) ndfrtconcept.length())
								/ ((double) snoconcept.length()) >= 0.75 )
						{
							correspondence.put(n, c);
							if ( ((double) ndfrtconcept.length())
									/ ((double) snoconcept.length()) < 1 )
								logger.log("Partial match: " + c.getName()
										+ " " + n.getName());
							break;
						}
					}
				}
			}
		}
		try
		{
			logger.log("NDF-RT concept pairs list size is: " + ndfrtset.size());
			logger.log("SNOMED-CT concept pairs list size is: "
					+ snomedset.size());
			Iterator<Entry<NdfrtConcept, ArrayList<String>>> itset = ndfrtset
					.entrySet().iterator();
			PrintWriter pw = new PrintWriter("C:\\example.txt");
			pw.write("NDFRT\r\n");
			while ( itset.hasNext() )
			{
				Entry<NdfrtConcept, ArrayList<String>> entry = (itset.next());
				for ( String value : entry.getValue() )
				{
					pw.write(entry.getKey() + "==>" + value + "\r\n");
				}
			}
			pw.write("SNOMED\r\n");
			logger.log("---------------------------------------");
			Iterator<Entry<SnomedConcept, ArrayList<String>>> itset2 = snomedset
					.entrySet().iterator();
			while ( itset2.hasNext() )
			{
				Entry<SnomedConcept, ArrayList<String>> entry = (itset2.next());
				for ( String value : entry.getValue() )
				{
					pw.write(entry.getKey() + "==>" + value + "\r\n");
				}
			}
			pw.close();

			PrintWriter pw2 = new PrintWriter("C:\\example2.txt");
			itset = ndfrtset.entrySet().iterator();
			while ( itset.hasNext() )
			{
				Entry<NdfrtConcept, ArrayList<String>> entry = (itset.next());
				if ( correspondence.containsKey(entry.getKey()) )
				{
					SnomedConcept parentconcept = correspondence.get(entry
							.getKey());
					if ( !snomedset.containsKey(parentconcept) )
					{
						continue;
					}
					for ( String value : snomedset.get(parentconcept) )
					{
						pw2.write("SNOMED " + parentconcept + "==>" + value
								+ "\r\n");
					}
					for ( String value : ndfrtset.get(entry.getKey()) )
					{
						pw2.write("NDFRT " + entry.getKey() + "==>" + value
								+ "\r\n");
					}
				}
			}
			pw2.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		int counter = 0;
		label: for ( NdfrtConcept n : ndfrtlist2 )
		{
			System.out.println(++counter + "/" + ndfrtlist2.size() + "|"
					+ n.toString());
			SnomedConcept s = correspondence.get(n);
			if ( s == null )
			{
				Color paintcolor = Color.green;
				Node node = nodes.get(ndfrtconceptmap.get(n));
				node.setNodecolor(paintcolor);
				node.setIgnore(false);
				if ( levelnodecorrespondance.get(node.y) != null )
				{
					node.y += 50;
				}
				nodes.set(ndfrtconceptmap.get(n), node);
				boolean paint = true;
				LinkedBlockingQueue<NdfrtConcept> q = new LinkedBlockingQueue<NdfrtConcept>();
				q.add(n);
				while ( !q.isEmpty() )
				{
					NdfrtConcept p = q.poll();
					while ( (s == null) && (!p.getparentConcept().isEmpty()) )// granularity level control is added here
					{
						node = nodes.get(ndfrtconceptmap.get(p));
						if ( (node.getNodecolor() == Color.cyan)
								|| (node.getNodecolor() == paintcolor) )
						{
							paint = false;
							break;
						}
						node.setNodecolor(paintcolor);
						node.setIgnore(false);
						if ( levelnodecorrespondance.get(node.y) != null )
						{
							node.y += 50;
						}
						nodes.set(ndfrtconceptmap.get(p), node);
						q.addAll(p.getparentConcept());
						p = q.poll();
						s = correspondence.get(p);
					}
					if ( paint && (!p.getparentConcept().isEmpty()) )
					{
						node = nodes.get(snomedconceptmap.get(s));
						node.setNodecolor(Color.pink);
						node.setIgnore(false);
						nodes.set(ndfrtconceptmap.get(p), node);
						nodes.set(snomedconceptmap.get(s), node);
						// We've found a common concept

					}
				}

			}
			else
			{// Additional granularity

				for ( NdfrtConcept parent : n.getparentConcept() )
				{
					if ( parent == null )
					{
						continue label;
					}
					SnomedConcept parent_s = correspondence.get(parent);
					if ( parent_s != null )
					{

						if ( s.getparentConcept().contains(parent_s) )
						{
							Node node = nodes.get(snomedconceptmap.get(s));
							node.setNodecolor(Color.white);
						}
						continue;
					}
					// if we've reached that point, the hierarchy definitely needs additional granularity
					Node node = nodes.get(ndfrtconceptmap.get(n));
					if ( !node.getNodecolor().equals(Color.white) )
					{
						node.setNodecolor(Color.DARK_GRAY);
						node.setIgnore(false);
					}
					if ( levelnodecorrespondance.get(node.y) != null )
					{
						node.y += 50;
					}
					nodes.set(ndfrtconceptmap.get(n), node);

					LinkedBlockingQueue<NdfrtConcept> q = new LinkedBlockingQueue<NdfrtConcept>();
					q.addAll(n.getparentConcept());
					while ( !q.isEmpty() )
					{
						NdfrtConcept p = q.poll();
						while ( (parent_s == null)
								&& (!p.getparentConcept().isEmpty()) )// granularity level control is added here
						{
							node = nodes.get(ndfrtconceptmap.get(p));
							node.setNodecolor(Color.cyan);
							node.setIgnore(false);
							if ( levelnodecorrespondance.get(node.y) != null )
							{
								node.y += 50;
							}
							nodes.set(ndfrtconceptmap.get(p), node);
							q.addAll(p.getparentConcept());
							p = q.poll();
							parent_s = correspondence.get(p);
						}
						if ( (!p.getparentConcept().isEmpty()) )
						{
							node = nodes.get(snomedconceptmap.get(parent_s));
							node.setNodecolor(Color.pink);// additional hierarchies
							node.setIgnore(false);

							nodes.set(snomedconceptmap.get(parent_s), node);
							// We've found a common concept

						}
					}

				}
			}
		}

	}

	/**
	 * This method allows to draw arrows between the concepts
	 * 
	 * @param g1
	 *            - Graphics object
	 * @param x1
	 *            - x coordinate of the starting point
	 * @param y1
	 *            - y coordinate of the starting point
	 * @param x2
	 *            - x coordinate of the target point
	 * @param y2
	 *            - y coordinate of the target point
	 */
	private void drawArrow( Graphics g1, int x1, int y1, int x2, int y2 )
	{
		int ARR_SIZE = 4;
		Graphics2D g = (Graphics2D) g1.create();

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len },
				new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
	}

	public void paint( Graphics g )
	{
		super.paint(g);

		FontMetrics f = g.getFontMetrics();
		int nodeHeight = f.getHeight() + 20;
		g.clearRect(0, 0, (int) this.getPreferredSize().getWidth(), (int) this
				.getPreferredSize().getHeight());
		for ( Edge e : edges )
		{
			if ( e.visible )
			{
				g.setColor(maincolor);
				if ( nodes.get(e.i).y < nodes.get(e.j).y )
				{
					drawArrow(g, nodes.get(e.i).x, nodes.get(e.i).y,
							nodes.get(e.j).x, nodes.get(e.j).y - nodeHeight / 2);
				}
				else
				{
					drawArrow(g, nodes.get(e.i).x, nodes.get(e.i).y,
							nodes.get(e.j).x, nodes.get(e.j).y + nodeHeight / 2);
				}
			}
		}
		int missingconcept = 0;
		int missinghierarchies = 0;
		int hierarchycount = 0;
		int controlcount = 0;
		int matchingcount = 0;
		for ( Node n : nodes )
		{
			if ( n.isIgnored() )
			{
				continue;
			}
			g.setColor(maincolor);
			int nodeWidth = f.stringWidth(n.name) + 20;

			if ( n.nodecolor.equals(Color.DARK_GRAY) )
			{
				hierarchycount++;
				g.setColor(n.nodecolor);
				g.fillRect(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight);
				g.setColor(maincolor);
				g.drawRect(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight);
				g.setColor(Color.white);
			}
			else if ( n.nodecolor.equals(Color.cyan) )
			{
				missingconcept++;
				missinghierarchies++;
				g.setColor(n.nodecolor);
				g.fillRoundRect(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight, 20, 20);
				g.setColor(maincolor);
				g.drawRoundRect(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight, 20, 20);
				g.setColor(Color.red);
			}
			else
			{
				if ( n.nodecolor.equals(Color.green) )
				{
					missingconcept++;
				}
				else if ( n.nodecolor.equals(Color.pink) )
				{
					controlcount++;
				}
				else if ( n.nodecolor.equals(Color.white) )
				{
					matchingcount++;
				}
				g.setColor(n.nodecolor);
				g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight);
				g.setColor(maincolor);
				g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
						nodeWidth, nodeHeight);
				g.setColor(textcolor);
			}
			g.drawString(n.name, n.x - f.stringWidth(n.name) / 2,
					n.y + f.getHeight() / 2);

		}
		logger.log("We have a total of " + missingconcept
				+ " missing concepts. Out of them " + missinghierarchies
				+ " concepts were added as " + hierarchycount
				+ " dark gray concepts " + controlcount + " red concepts "
				+ matchingcount
				+ " white concepts. The number of similarly-named concepts is "
				+ correspondence.size());
	}

}