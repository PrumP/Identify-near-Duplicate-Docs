import java.util.ArrayList;

public class HashTable 
{
	
	private int numRows;
	private ArrayList<String>[] rows;
	
	
	public HashTable(int numRows)
	{
		this.numRows = numRows;
		this.rows = (ArrayList<String>[])new ArrayList[this.numRows];
		for(int i=0;i<this.numRows; i++)
		{
			this.rows[i] = new ArrayList<String>();
			
		}
	}

	public void resetTable()
	{
		for(int i=0; i<this.numRows; i++)
		{
			this.rows[i].clear();
		}
	}
	public void addToRow(int i, String doc)
	{
		this.rows[i].add(doc);
	}
	
	public ArrayList getRow(int i)
	{
		return this.rows[i];
	}

	public int getNumRows()
	{
		return this.numRows;
	}
		

}
