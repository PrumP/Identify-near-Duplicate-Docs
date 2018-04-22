import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class MinHash {
	
	private final String folder;
	private final int numPermutations;
	private final File folderFiles;
	private final String[] file_names;
	private final Set allTerms;
	private final int totalNumTerms;
	private final int prime;
	private final int[][] kHash;
	private final int[][] minHashMartix;
	
	public MinHash(String folder, int numPermutations) throws FileNotFoundException
	{
		this.folder = folder;
		this.numPermutations = numPermutations;
		this.folderFiles = new File(this.folder);
		this.file_names = folderFiles.list();
		this.allTerms = new HashSet <String>();
		findTermList();
		this.totalNumTerms = this.allTerms.size();
		this.prime = findNextPrime();
		this.kHash = new int[this.numPermutations][2];
		this.minHashMartix = new int[this.numPermutations][this.file_names.length];
		setHashParameters();
		
	}
	
	public String[] allDocs()
	{
		return this.file_names;
	}
	
	public int[][] getMinHashMatrix()
	{
		
		return this.minHashMartix;
	}
	
	public int getNumTerms()
	{
		return this.totalNumTerms;
	}
	
	public int getNumPermutations()
	{
		return this.numPermutations;
	}
	
	
	public void findTermList()throws FileNotFoundException
	{
		int totalNumberOfwords = 0;
		ArrayList<String> termList = new ArrayList<String>();
		
		
		for(int i=0; i<this.file_names.length; i++)
		{
			termList = findTerms(this.file_names[i]);
			this.allTerms.addAll(termList);
		}
		
	}
	
	public boolean isPrime(int x)
	{
		for(int i=2; i<x; i++)
		{
			if(x%i == 0)
				return false;
		}
		return true;
	}
	
	public int findNextPrime()
	{
		int nextPrime = this.totalNumTerms;
		boolean found = isPrime(nextPrime);
		
		while(!found)
		{
			nextPrime++;
			found = isPrime(nextPrime);
		}
		
		return nextPrime;
	}
	
	
	
	public String removePunctuation(String t)
	{
	
		t = t.replace(".", "");
		t = t.replace(",", "");
		t = t.replace(":", "");
		t = t.replace(";", "");
		t = t.replace("'", "");
		
		return t;
	}
	
	
	
	public ArrayList<String> findTerms(String file) throws FileNotFoundException
	{
		ArrayList<String> termsList = new ArrayList<String>();
		
		String filePath = this.folder+"/"+file;
		
		File inputFile = new File(filePath);

		Scanner fileIn= new Scanner(inputFile);
		
		String t = "";
		while(fileIn.hasNext())
		{
			t = fileIn.next();
			t = t.toLowerCase();
			if((t.length()>=3) && !(t.equalsIgnoreCase("the")))
			{
				t = removePunctuation(t);
				termsList.add(t);
			}
		}
		
		fileIn.close();
		
		return termsList;
	}
	
	public double exactJaccard(String file1, String file2) throws FileNotFoundException
	{
		double exactJac = 0;
		
		ArrayList<String> s1 = findTerms(file1);
		ArrayList<String> s2 = findTerms(file2);
		
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		Set<String> union = new HashSet<String>();
		Set<String> inter = new HashSet<String>();
		set1.addAll(s1);
		set2.addAll(s2);
		
		union.addAll(set1);
		union.addAll(set2);
		int unionCount = union.size();
		
		inter.addAll(set1);
		inter.retainAll(set2);
		int interCount = inter.size();
		
		exactJac = ((double)interCount)/((double)unionCount);
		
		return exactJac;
	}
	
	
	
	public boolean findDuplicates(int a, int b)
	{
	       
        for(int i=0; i<this.numPermutations; i++)
        {
        	if((this.kHash[i][0] == a) && (this.kHash[i][1] == b))
        	{
        		return true;
        	}
        		
        }
        
        return false;
	}
	
	public void setHashParameters() {
       
        Random rand = new Random();
        int x = 0;
        int y = 0;
        
        for(int i=0; i<this.numPermutations; i++)
        {
        	x = rand.nextInt(this.prime-1);
        	y = rand.nextInt(this.prime-1);
            boolean duplicates = findDuplicates(x,y);
            
            while(duplicates)
            {
            	x = rand.nextInt(this.prime-1);
            	y = rand.nextInt(this.prime-1);
            	duplicates = findDuplicates(x,y);
            }
            
            this.kHash[i][0] = x;
            this.kHash[i][1] = y;
        }
        
    }
	
	public int getHashRandom(String s,int currentNoHash) {
        int hc = s.hashCode();
        int x = this.kHash[currentNoHash][0];
        int y = this.kHash[currentNoHash][1];
        hc = (int)(x*hc + y)%this.prime;
        return hc;
    }
	
	public int[] findKHash(String s)
	{
		int[] kHashValue = new int[this.numPermutations];
		int bitNo = 0;
		
		for(int i=0; i<this.numPermutations; i++)
		{
			kHashValue[i] = getHashRandom(s.toLowerCase(),i);
			
		}
		return kHashValue;
		
		 
	}
	
	public int[] minHashSig(String fileName) throws FileNotFoundException
	{
		int[] minhashSignature = new int[this.numPermutations];
		int[] KHashValue;
		
		ArrayList<String> termList = findTerms(fileName);
		
		for(int i =0; i<termList.size();i++)
		{
			KHashValue = findKHash(termList.get(i));
			
			for(int j=0; j<this.numPermutations; j++)
			{
				if(minhashSignature[j]>KHashValue[j])
				{
					minhashSignature[j]=KHashValue[j];
				}
			}
		}
		
		
		return minhashSignature;
	}
	
	public double approximateJaccard(String file1, String file2) throws FileNotFoundException
	{
		double approximateJac = 0.0;
		int sim = 0;
		
		int[] minHashSigF1 = minHashSig(file1);
		int[] minHashSigF2 = minHashSig(file2);
		
		for(int i=0; i<this.numPermutations;i++)
		{
			if(minHashSigF1[i]== minHashSigF2[i])
			{
				sim++;
			}
		}
		
		approximateJac = ((double)sim)/((double)this.numPermutations);
		
		return approximateJac;
	}
	
	public void findMinHashMatrix() throws FileNotFoundException
	{
		int [] filMinHashSig = new int[this.numPermutations];
		for(int j=0; j<this.file_names.length; j++)
		{
			filMinHashSig = minHashSig(this.file_names[j]);
			for(int i=0; i<this.numPermutations; i++)
			{
				this.minHashMartix[i][j] = filMinHashSig[i];
				System.out.println("MinHashMatrix Creation... ");
			}
		}
		
	}
	

	
	

}
