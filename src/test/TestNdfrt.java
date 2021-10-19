package test;

import ndfrt.NdfrtConcept;

public class TestNdfrt 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		NdfrtConcept root=new NdfrtConcept("0","Root","");
		System.out.println(ndfrt.Retrieval.height(null));
		System.out.println(ndfrt.Retrieval.height(root));
		NdfrtConcept child=new NdfrtConcept(root,"1","child1","");
		root.addChildConcept(child);
		System.out.println(ndfrt.Retrieval.height(root));
		root.addChildConcept(new NdfrtConcept(root,"1","child1",""));
		System.out.println(ndfrt.Retrieval.height(root));
		NdfrtConcept child2=new NdfrtConcept(child,"2","child2","");
		child.addChildConcept(child2);
		System.out.println(ndfrt.Retrieval.height(root));
		int height=ndfrt.Retrieval.height(root);
		for(int i=1;i<=height;i++)
		System.out.println(ndfrt.Retrieval.returnGivenLevel(root, i));
	}

}
