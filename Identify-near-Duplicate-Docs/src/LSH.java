import java.util.ArrayList;
import java.util.Random;

public class LSH 
{
	
	private final String[] docNames;
	private final int[][] minHashMartix;
	private final int bands;
	private final int r;
	private final int k;
	private final int docNum;
	private final HashTable[] bHashTables;
	private final int prime;
	private final int x_hash;
	private final int y_hash;
	
	
	public LSH(int[][] minHashMatrix, String[] docNames, int bands)
	{
		this.minHashMartix = minHashMatrix;
		this.docNames = docNames;
		this.bands = bands;
		this.k = this.minHashMartix.length;
		this.docNum = this.docNames.length;
		this.r = (int)Math.floor((double)this.k/(double)this.bands);
		this.prime = findNextPrime();
		Random rand = new Random();
		this.x_hash = rand.nextInt(this.prime-1);
        this.y_hash = rand.nextInt(this.prime-1);
		
		this.bHashTables = new HashTable[this.bands];
		
		for(int i=0; i<this.bands; i++)
		{
			this.bHashTables[i] = new HashTable(this.prime);
		}
		setBHashTables();
	}
	
	public boolean isPrime(int k)
	{
		for(int i=2; i<k; i++)
		{
			if(k%i == 0)
				return false;
		}
		return true;
	}
	
	public int findNextPrime()
	{
		int nextPrime = this.docNum+1;
		boolean found = isPrime(nextPrime);
		
		while(!found)
		{
			nextPrime++;
			found = isPrime(nextPrime);
		}
		
		return nextPrime;
	}
	
	public int getHashRandom(String s) {
        int hc = s.hashCode();
        hc = (int)(this.x_hash*hc + this.y_hash)%this.prime;
        return hc;
    }
    
	public String getMinHashTuple(int start, int end, int doc)
	{
		String tuple = "";
		
		for(int i=start; i<end; i++)
		{
			tuple =  Integer.toString(this.minHashMartix[i][doc]) + "&";
		}
		
		return tuple;
	}
	
	
	
	public void setBHashTables()
	{
		int start = 0;
		int end = start+this.r;
		String t = "";
		int index = 0;
		
		for(int b=0; b<this.bands;b++)
		{
			for(int j=0; j<this.docNum; j++)
			{
				t = getMinHashTuple(start,end,j);
				index = getHashRandom(t);
				System.out.println("Set Hash Table... Band:"+ b + "\tIndex:" + Math.abs(index));
				this.bHashTables[b].addToRow(Math.abs(index), this.docNames[j]);
				
			}
			
			start = end;
			end += this.r;
			if(end > this.k)
			{
				end = this.k;
			}
			
		}
	
	}
	
	
	public int findDocIndex(String doc)
	{
		int index = 0;
		for(int i=0; i<this.docNum; i++)
		{
			if(this.docNames[i].equals(doc))
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	
	public ArrayList nearDuplicatesOf(String docName)
	{
		ArrayList<String> duplicates = new ArrayList<String>();
		int docIndex = findDocIndex(docName);
		
		int start = 0;
		int end = start+this.r;
		String t = "";
		int index = 0;
		for(int b=0; b<this.bands;b++)
		{
			t = getMinHashTuple(start,end,docIndex);
			index = getHashRandom(t);
			ArrayList<String> atemp = this.bHashTables[b].getRow(Math.abs(index));
            for(int i=0; i<atemp.size();i++)
            {
            	if(!duplicates.contains(atemp.get(i)))
            	{
            			duplicates.add(atemp.get(i));
            	}
            }
			
			start = end;
			end += this.r;
			if(end > this.k)
			{
				end = this.k;
			}
			
			System.out.println("Finding duplicates...");
			
		}
		
		return duplicates;
		
	}
	
	public void printHashTables(int tableNum)
	{
		
		int x= this.bHashTables[tableNum].getNumRows();
		
		System.out.println("number of rows " + x);
		
		for(int i=0;i<x;i++)
		{
			ArrayList<String> r= this.bHashTables[tableNum].getRow(i);
			
			for(String s :r)
				System.out.print(r);
			System.out.println();
		}
	}

	
}
