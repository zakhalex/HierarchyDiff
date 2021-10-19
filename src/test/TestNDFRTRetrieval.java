package test;

public class TestNDFRTRetrieval
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		// TODO Auto-generated method stub
		String[] params = { "I:\\ndfrt.txt", "I:\\ndfrtwithtrace.txt" };
		try
		{
			ndfrt.Retrieval.main(params);
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
