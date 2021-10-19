package ndfrt;

import java.util.ArrayList;
import java.util.HashSet;

public class NdfrtConcept
{
	private HashSet<NdfrtConcept> parentConcept = new HashSet<NdfrtConcept>();
	private String id;
	private String name;
	private String kind;
	private ArrayList<NdfrtConcept> children = new ArrayList<NdfrtConcept>();

	public HashSet<NdfrtConcept> getparentConcept()
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

	public String getKind()
	{
		return kind;
	}

	public void setKind( String kind )
	{
		this.kind = kind;
	}

	public ArrayList<NdfrtConcept> getChildren()
	{
		return children;
	}

	public void setChildren( ArrayList<NdfrtConcept> children )
	{
		this.children = children;
	}

	public NdfrtConcept(String id, String name, String kind)
	{
		this.id = id;
		this.name = name;
		this.kind = kind;
	}

	public NdfrtConcept(String id, String name, String kind,
			ArrayList<NdfrtConcept> children)
	{
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.children = children;
	}

	public NdfrtConcept(NdfrtConcept parentConcept, String id, String name,
			String kind)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.add(parentConcept);
		}
		this.id = id;
		this.name = name;
		this.kind = kind;
	}

	public NdfrtConcept(NdfrtConcept parentConcept, String id, String name,
			String kind, ArrayList<NdfrtConcept> children)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.add(parentConcept);
		}
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.children = children;
	}

	public NdfrtConcept(HashSet<NdfrtConcept> parentConcept, String id,
			String name, String kind)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.addAll(parentConcept);
		}
		this.id = id;
		this.name = name;
		this.kind = kind;
	}

	public NdfrtConcept(HashSet<NdfrtConcept> parentConcept, String id,
			String name, String kind, ArrayList<NdfrtConcept> children)
	{
		if ( parentConcept != null )
		{
			this.parentConcept.addAll(parentConcept);
		}
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.children = children;
	}

	public void addChildConcept( NdfrtConcept c )
	{
		children.add(c);
	}

	@Override
	public String toString()
	{
		return id + "~_~" + name;
	}

	public NdfrtConcept getChildbyId( String id )
	{
		for ( NdfrtConcept c : children )
		{
			if ( c.getName().equalsIgnoreCase(id) )
			{
				return c;
			}
		}
		return null;
	}

	public NdfrtConcept getChildbyName( String name )
	{
		for ( NdfrtConcept c : children )
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
		NdfrtConcept other = (NdfrtConcept) obj;
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
