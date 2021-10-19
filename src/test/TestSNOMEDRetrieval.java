package test;

public class TestSNOMEDRetrieval
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		// TODO Auto-generated method stub
		String[] params = { "I:\\snomed.txt", "I:\\snomedwithtrace.txt" };
		try
		{
			snomed.Retrieval.main(params);
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
