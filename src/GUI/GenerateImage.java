package GUI;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.Callable;

import GUI.GraphDisplay.Node;
/*
 * This class allows multithreaded redrawing of the image inside the panes.
 */
public class GenerateImage implements Callable<Integer> 
{
	private Graphics g;
	private Node n;
	private Color maincolor;
	private Color backgroundcolor;
	private Color textcolor;
	private int width;
	private FontMetrics f;
	private int nodeHeight;
	
	
	public GenerateImage(Graphics g, Node n, Color maincolor,Color backgroundcolor, Color textcolor, int width, FontMetrics f,int nodeHeight) 
	{
		this.g = g;
		this.n = n;
		this.maincolor = maincolor;
		this.backgroundcolor = backgroundcolor;
		this.textcolor = textcolor;
		this.width = width;
		this.f = f;
		this.nodeHeight = nodeHeight;
	}


	@Override
	public Integer call() throws Exception 
	{
		g.setColor(maincolor);
		int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
		g.setColor(backgroundcolor);
		g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
				nodeWidth, nodeHeight);
		g.setColor(maincolor);
		g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
				nodeWidth, nodeHeight);
		g.setColor(textcolor);
		g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
				n.y+f.getHeight()/2);
		return 0;
	}

	
}
