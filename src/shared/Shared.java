package shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Shared
{

	/**
	 * This method allows to read the parameters from an external configuration file. It returns a blank ArrayList, if no file has been found
	 * 
	 * @param params
	 *            - ArrayList of parameters that it has to search for in the file. Everything, not present in this list will be ignored
	 * @param file
	 *            - Name of the file
	 * @return - an ArrayList of values. This list contains the corresponding values from the file or blanks, if, for some reason, it was not able to find the
	 *         corresponding information
	 * @throws Exception
	 */
	public static ArrayList<String> loadVariablesFromFile(
			ArrayList<String> params, String file ) throws Exception
	{
		ArrayList<String> paramlist = new ArrayList<String>(params.size());
		ArrayList<String> resultlist = new ArrayList<String>(params.size());
		for ( int i = 0; i < params.size(); i++ )
		{
			paramlist.add(params.get(i).trim().toLowerCase());
			resultlist.add("");
		}
		File checkfile=new File(file);
		if(!checkfile.exists())
		{
			return resultlist;//Unable to read the input file
		}
		BufferedReader br = null;

		try
		{
			String line;
			br = new BufferedReader(new FileReader(file));
			while ( (line = br.readLine()) != null )
			{
				int cutoff = line.indexOf('=');
				if ( cutoff == -1 )
				{
					continue;
				}
				String paramname = line.substring(0, cutoff).trim();
				String value = line.substring(cutoff+1).trim();
				if ( paramlist.contains(paramname.toLowerCase()) )
				{
					resultlist.set(paramlist.indexOf(paramname.toLowerCase()),
							value.trim());
				}
			}
		}
		finally
		{
			if ( br != null )
			{
				br.close();
			}
		}
		return resultlist;
	}
}
