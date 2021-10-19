package ndfrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DatabaseSearch
{

	/**
	 * @param args
	 */
	public static void main( String[] args ) throws Exception
	{
		// TODO Auto-generated method stub
		File folder = new File("I:\\University\\NDF_RT\\db");
		File[] filelist = folder.listFiles();
		for ( File file : filelist )
		{
			if ( file.getName().toLowerCase().contains(".txt") )
			{
				BufferedReader br = new BufferedReader(new FileReader(
						file.getAbsoluteFile()));
				String line;
				while ( (line = br.readLine()) != null )
				{
					if ( line.toLowerCase().contains("chemical ingredients") )
					{
						System.out.println(file.getAbsolutePath());
						break;
					}
				}
				br.close();
			}
		}
	}

}
