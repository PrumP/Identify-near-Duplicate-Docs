import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MinHashSpeed implements Runnable
{
	
	double exactJac = 0.0;
	double estimateJac = 0.0;
	static int numPermutations = 0;
	static MinHash minH;
	static String[] fileNames;
	
	static int MinHashMatrix[][];
	
	int fileindex=0;
	int l=0;
	
	public MinHashSpeed(int fileindex) 
	{
	     this.fileindex = fileindex;
	}
	
	//for estimate Jaccard from MinHashMatrix
	
	/*
	@Override
	public void run() 
	{
		for(int j=fileindex+1; j<fileNames.length-1; j++)
		{
			for( int i=0; i<numPermutations;i++)
			{
				if( MinHashMatrix[i][j] ==  MinHashMatrix[i][j+1])
				{
					l++;
				}
			}
				
				estimateJac = ((double)l)/((double)numPermutations);
				System.out.println("estimateJac...File1:" + fileindex + " File2:" +j+"--->"+estimateJac);
				
				l=0;
	    }
	}
	*/
	
	//for ExactJaccard
	
	@Override
	public void run() 
	{
		for(int j=fileindex+1; j<fileNames.length; j++)
		{
				try 
				{
					exactJac = minH.exactJaccard(fileNames[fileindex], fileNames[j]);
					System.out.println("exactJaccard...File1:" + fileindex + " File2:" +j+"--->"+exactJac);

				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
								
	    }
	}
	
	public static void main(String arg[]) throws FileNotFoundException, InterruptedException
	{
		Scanner in = new Scanner(System.in);
		String folderName="";	
		
		System.out.print("Name of the folder: ");
		folderName = in.next();
		
		System.out.print("Number of permutations to be used: ");
		numPermutations = in.nextInt();
		
        minH = new MinHash(folderName,numPermutations);
        
        long start = System.currentTimeMillis();
    	
		fileNames = minH.allDocs();
		
		//for estimate Jaccard from MinHashMatrix
		
		/*
		minH.findMinHashMatrix();
		MinHashMatrix=minH.getMinHashMatrix();
		
        List<MinHashSpeed> m= new ArrayList<MinHashSpeed>();
		
		for(int i=0; i<fileNames.length; i++)
			m.add(new MinHashSpeed(i));
	
		
		Thread forEachCol[] = new Thread[m.size()];
		
		for (int j = 0; j < m.size(); j++) 
		{
			forEachCol[j] = new Thread(m.get(j));
			forEachCol[j].start();
		}
		
		for (int j = 0; j <  m.size(); j++) 
			forEachCol[j].join();
	    */
		
		//for ExactJaccard
		
		List<MinHashSpeed> m= new ArrayList<MinHashSpeed>();
		
		for(int i=0; i<fileNames.length; i++)
			m.add(new MinHashSpeed(i));
	
		
		Thread forEachFile[] = new Thread[m.size()];
		
		for (int j = 0; j < m.size(); j++) 
		{
			forEachFile[j] = new Thread(m.get(j));
			forEachFile[j].start();
		}
		
		for (int j = 0; j <  m.size(); j++) 
			forEachFile[j].join();
		
		 long end = System.currentTimeMillis();

	     System.out.println("Time taken in seconds : " + ((end - start) / 1000));
	}
		

}
