package snomed;

import java.util.ArrayList;
import java.util.HashSet;

public class SnomedConcept
{
	private HashSet<SnomedConcept> parentConcept = new HashSet<SnomedConcept>();;
	private String id;
	private String name;
	private ArrayList<SnomedConcept> children = new ArrayList<SnomedConcept>();

	public HashSet<SnomedConcept> getparentConcept()
	{
		return parentConcept;
	}

	public String getId()
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public ArrayList<SnomedConcept> getChildren()
	{
		return children;
	}

	public void setChildren( ArrayList<SnomedConcept> children )
	{
		this.children = children;
	}

	public SnomedConcept(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public SnomedConcept(String id, String name,
			ArrayList<SnomedConcept> children)
	{
		this.id = id;
		this.name = name;
		this.children = children;
	}

	public SnomedConcept(SnomedConcept parentConcept, String id, String name)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.add(parentConcept);
		}
		// this.parentConcept = parentConcept;
		this.id = id;
		this.name = name;
	}

	public SnomedConcept(SnomedConcept parentConcept, String id, String name,
			ArrayList<SnomedConcept> children)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.add(parentConcept);
		}
		// this.parentConcept = parentConcept;
		this.id = id;
		this.name = name;
		this.children = children;
	}

	public SnomedConcept(HashSet<SnomedConcept> parentConcept, String id,
			String name)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.addAll(parentConcept);
		}
		// this.parentConcept = parentConcept;
		this.id = id;
		this.name = name;
	}

	public SnomedConcept(HashSet<SnomedConcept> parentConcept, String id,
			String name, ArrayList<SnomedConcept> children)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.addAll(parentConcept);
		}
		// this.parentConcept = parentConcept;
		this.id = id;
		this.name = name;
		this.children = children;
	}

	public void addChildConcept( SnomedConcept c )
	{
		children.add(c);
	}

	@Override
	public String toString()
	{
		return id + "~_~" + name;
	}

	public SnomedConcept getChildbyId( String id )
	{
		for ( SnomedConcept c : children )
		{
			if ( c.getName().equalsIgnoreCase(id) )
			{
				return c;
			}
		}
		return null;
	}

	public SnomedConcept getChildbyName( String name )
	{
		for ( SnomedConcept c : children )
		{
			if ( c.getName().equalsIgnoreCase(name) )
			{
				return c;
			}
		}
		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SnomedConcept other = (SnomedConcept) obj;
		if ( id == null )
		{
			if ( other.id != null )
				return false;
		}
		else if ( !id.equals(other.id) )
			return false;
		if ( name == null )
		{
			if ( other.name != null )
				return false;
		}
		else if ( !name.equals(other.name) )
			return false;
		return true;
	}

}
