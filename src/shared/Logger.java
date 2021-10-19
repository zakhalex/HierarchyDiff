package shared;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	private String filename = "";
	private boolean filemode = false;

	public Logger(String filename, boolean append)
	{
		this.filename = filename;
		filemode = true;
		if ( !append )
		{
			try
			{
				File f = new File(filename);
				if ( f.exists() )
				{
					f.delete();
				}
			}
			catch ( Exception e )
			{

			}
		}
	}

	public Logger(String filename)
	{
		this.filename = filename;
		filemode = true;
		try
		{
			File f = new File(filename);
			if ( f.exists() )
			{
				f.delete();
			}
		}
		catch ( Exception e )
		{

		}
	}

	public Logger()
	{

	}

	public synchronized void log( String message )
	{
		String newmessage = sdf.format(new Date()) + message + "\r\n";
		if ( filemode )
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
				pw.write(newmessage);
				pw.close();
			}
			catch ( Exception e )
			{

			}
		}
		System.out.println(newmessage);
	}
}
